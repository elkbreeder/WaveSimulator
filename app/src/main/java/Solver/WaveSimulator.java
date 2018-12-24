package Solver;

public class WaveSimulator {
    public static final double g = 9.81;
    public static final double zero_tolerance = 0.001;
    public static final double dx = 1; //size of one cell is 1x1 by default
    public static final double dy =1;

    private double[][] hu;
    private double[][] hv;
    private double[][] h;
    private double[][] objects; //not used so far

    //create example
    public WaveSimulator()
    {
        h = new double[100][100];
        hu= new double[100][100];
        hv= new double[100][100];
        for(int i = 0; i < 100; i++)
        {
            for(int j = 0; j< 100; j++)
            {
                this.hu[i][j] = this.hv[i][j] = 0;
                if(i >= 40 && i< 60 && j >= 40 && j < 60)
                {
                    this.h[i][j] = 15;
                    continue;
                }
                this.h[i][j] = 5;
            }
        }
    }
    public WaveSimulator(double[][] h,double[][] hu,double[][] hv) throws Exception {
        if(!(h.length == hu.length && h[0].length == hu[0].length))
        {
            throw new Exception("h and hu Domain sizes don't match");
        }
        this.h = h;
        this.hu = hu;
        this.hv = hv;
        /*add space for boundary cells;
        for(int i = 0; i < this.h.length;i++)//translate to domain with boundary;
        {
            for(int j = 0;j < this.h[0].length;j++)
            {
                if(i == 0 ||i == h.length) {
                    this.hu[i][j] = 5;
                    this.h[i][j] = 5; //set boundary cells to 5
                }
                if(j == 0 ||j == h[0].length) {
                    this.hu[i][j] = 5;
                    this.h[i][j] = 5;
                }
                this.h[i][j] = h[i-1][j-1];
                this.hu[i][j] = hu[i-1][j-1];
            }
        }*/

    }
    public double get(int x, int y)
    {
        return h[x][y];
    }
    public void calculate_step()
    {
        double maxWaveSpeed = 0;
        double h_update_above[][] = new double[h.length][h[0].length];
        double h_update_below[][] = new double[h.length][h[0].length];
        double hv_update_above[][] = new double[h.length][h[0].length];
        double hv_update_below[][] = new double[h.length][h[0].length];
        double h_update_left[][] = new double[h.length][h[0].length];
        double h_update_right[][] = new double[h.length][h[0].length];
        double hu_update_left[][] = new double[h.length][h[0].length];
        double hu_update_right[][] = new double[h.length][h[0].length];
        //vertical
        for(int i = 1; i < h.length-1; i++)
        {
            for(int j = 1; j< h[0].length;j++) //müsste nicht für erste und letzte spalte berechnet werden da boundary cells
            {
                double update[] =solve_single(h[i][j-1],h[i][j],hv[i][j-1],hv[i][j]);
                h_update_above[i][j] = update[0];
                hv_update_above[i][j] = update[1];
                h_update_below[i][j] = update[2];
                hv_update_below[i][j] = update[3];
                maxWaveSpeed = Math.max(maxWaveSpeed,Math.max(Math.abs(update[4]),Math.abs(update[5])));
            }
        }
        //horizontal
        for(int i = 1; i < h.length; i++)
        {
            for(int j = 1; j< h[0].length-1;j++)
            {
                double update[] =solve_single(h[i-1][j],h[i][j],hu[i-1][j],hu[i][j]);
                h_update_left[i][j] = update[0];
                hu_update_left[i][j] = update[1];
                h_update_right[i][j] = update[2];
                hu_update_right[i][j] = update[3];
                maxWaveSpeed = Math.max(maxWaveSpeed,Math.max(Math.abs(update[4]),Math.abs(update[5])));
            }
        }
        double dt;
        if(maxWaveSpeed > 0.00001)
        {
            dt = Math.min(dx/maxWaveSpeed,dy/maxWaveSpeed)*0.4;
        }
        else
        {
            dt = Double.MAX_VALUE;
        }
        //update cells
        for(int i = 1; i < h.length-1; i++)
        {
            for(int j = 1 ; j <h[0].length-1;j++)
            {
                h[i][j] -= dt/dx * (h_update_right[i-1][j] + h_update_left[i][j]) + dt/dy * (h_update_above[i][j-1]+ h_update_below[i][j]);
                hu[i][j] -= dt/dx *(hu_update_right[i-1][j] + hu_update_left[i][j]);
                hv[i][j] -= dt/dy * (hv_update_above[i][j-1] + hv_update_below[i][j]);
                if(h[i][j]< 0)
                {
                    h[i][j] = hu[i][j]= hv[i][j] = 0;
                }
                else if(h[i][j]<0.1)
                {
                    hu[i][j] = hv[i][j] = 0;
                }
            }
        }
    }
    public static double[] solve_single(double h_left, double h_right, double hu_left, double hu_right)
    {
        if(h_left != h_right){
            double d  = 0;
        }
        //no bathymetry so far
        double h_roe =  0.5 * (h_left + h_right);

        double u_left = hu_left/h_left;
        double u_right = hu_right/h_right;
        double u_roe = (u_left * Math.sqrt(h_left) + u_right * Math.sqrt(h_right))/(Math.sqrt(h_left)+Math.sqrt(h_right));
        double lambda_1 = u_roe-Math.sqrt(g*h_roe);
        double lambda_2 = u_roe+Math.sqrt(g*h_roe);
        double d_f_1= hu_right -hu_left;
        double d_f_2 = (h_right* u_right* u_right + 0.5*g*h_right*h_right)- (h_left *u_left*u_left + 0.5*g*h_right*h_right);
        double alpha_1 = (lambda_1*d_f_1 - d_f_2)*(1/(lambda_2-lambda_1));
        double alpha_2 = (lambda_1*d_f_1+d_f_2)*(1/(lambda_2-lambda_1));

        double h_update_left = 0;
        double h_update_right = 0;
        double hu_update_left = 0;
        double hu_update_right = 0;
        //Calculate left
        if(lambda_1 <0)
        {
            h_update_left += alpha_1;
            hu_update_left += alpha_1*lambda_1;
        }
        if(lambda_2 <0)
        {
            h_update_left += alpha_2;
            hu_update_left += alpha_2*lambda_2;
        }
        //Calculate right
        if(lambda_1 >0)
        {
            h_update_right += alpha_1;
            hu_update_right += alpha_1*lambda_1;
        }
        if(lambda_2 > 0)
        {
            h_update_right += alpha_2;
            hu_update_right += alpha_2*lambda_2;
        }
        double speed_left = lambda_1;
        double speed_right = lambda_2;
        if(speed_left>0 && speed_right > 0)speed_left = 0;
        if(speed_left<0 && speed_right < 0)speed_right = 0;
        double[] ret = { h_update_left,hu_update_left, h_update_right,hu_update_right,speed_left,speed_right};
        return ret;

    }
}

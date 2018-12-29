package Solver;

public class Helper {
    public static float linear_map(float x1, float y1, float x2, float y2,float number)
    {
        float m = ((y2-y1)/(x2-x1));
        float t = y1-m*x1;
        return m*number+t;
    }
    //copied from https://homepages.inf.ed.ac.uk/rbf/HIPR2/flatjavasrc/Convolution.java TODO:implement by ourself
    public static double[][] convolution2D(double[][] input,
                                           int width, int height,
                                           double[][] kernel,
                                           int kernelWidth,
                                           int kernelHeight) {
        int smallWidth = width - kernelWidth + 1;
        int smallHeight = height - kernelHeight + 1;
        double[][] output = new double[smallWidth][smallHeight];
        for (int i = 0; i < smallWidth; ++i) {
            for (int j = 0; j < smallHeight; ++j) {
                output[i][j] = 0;
            }
        }
        for (int i = 0; i < smallWidth; ++i) {
            for (int j = 0; j < smallHeight; ++j) {
                output[i][j] = singlePixelConvolution(input, i, j, kernel,
                        kernelWidth, kernelHeight);
            }
        }
        return output;
    }
    public static double singlePixelConvolution(double[][] input,
                                                int x, int y,
                                                double[][] k,
                                                int kernelWidth,
                                                int kernelHeight) {
        double output = 0;
        for (int i = 0; i < kernelWidth; ++i) {
            for (int j = 0; j < kernelHeight; ++j) {
                output = output + (input[x + i][y + j] * k[i][j]);
            }
        }
        return output;
    }
            /* Start of edge detection implementation
        double[][] input = new double[CPPSimulator.cell_count + 2][CPPSimulator.cell_count + 2];
        for (int i = 0; i < CPPSimulator.cell_count; i++) {
            for (int j = 0; j < CPPSimulator.cell_count; j++) {
                input[i + 1][j + 1] = CPPSimulator.sim.getHeight(i, j);
            }
        }
        double[][] edgedetect_lr = {{1, 1, 1}, {0, 0, 0}, {-1, -1, -1}};
        double[][] edgedetect_tb = {{1, 0, -1}, {1, 0, -1}, {1, 0, -1}};
        double[][] output_lr = Helper.convolution2D(input, CPPSimulator.cell_count + 2, CPPSimulator.cell_count + 2, edgedetect_lr, 3, 3);
        double[][] output_tb = Helper.convolution2D(input, CPPSimulator.cell_count + 2, CPPSimulator.cell_count + 2, edgedetect_tb, 3, 3);
                           /* if(current_height>=4.5 && current_height <= 5.5)
                    {
                        paint.setColor(Color.rgb(255, (int)(11*current_height+50), 200));
                        canvas.drawRect(drawing_rects[i][j], paint);
                    }
                    else{
                        int current = (int) CPPSimulator.sim.getHeight(i, j) * 25;
                        current = (current > 255) ? 255 : current;
                        paint.setColor(Color.rgb(0, 255 - current, 255));
                        canvas.drawRect(drawing_rects[i][j], paint);
                    }*/
}

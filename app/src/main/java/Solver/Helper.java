package Solver;
//! \class Helper \brief a This class contains a helper method that maps a number of the x domain onto the y domain via linearisation;
public class Helper {
    //class for static helper methods
    public static float linear_map(float x1, float y1, float x2, float y2,float number)//maps a number in the x domain to a number in y domain where x1 corresponds to y1 and x2 corresponds to y2
    {
        float m = ((y2-y1)/(x2-x1));
        float t = y1-m*x1;
        return m*number+t;
    }
}

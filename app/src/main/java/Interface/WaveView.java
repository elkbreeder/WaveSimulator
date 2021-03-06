package Interface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.graphics.ColorUtils;
import android.util.AttributeSet;
import android.view.View;
import Solver.CPPSimulator;
import Solver.Helper;

//! \class WaveView \brief a This class is responsible for the Wave visualization
public class WaveView extends View {
    
    private static final float pause_sensitivity = (float)0.01;//!< minimum change which is required to not pause the simulation
    private static Rect[][] drawing_rects;//!< rects which visualize the waveheights
    private Paint paint;//!< paint which visualizes the waveheights

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint = new Paint();

        if (CPPSimulator.sim == null) {
            CPPSimulator.sim = new CPPSimulator();
        }//!> if there is no Simulator running, instantiate a new one
        drawing_rects = new Rect[CPPSimulator.cell_count][CPPSimulator.cell_count];//!> initiate the rectangles

        
        int size_x = getWidth() / CPPSimulator.cell_count;//!<  calculate the position and size of the drawingrects
        int offset_x = (getWidth() - size_x * CPPSimulator.cell_count) / 2;
        int size_y = getHeight() / CPPSimulator.cell_count;
        int offset_y = (getHeight() - size_y * CPPSimulator.cell_count) / 2;
        for (int i = 0; i < CPPSimulator.cell_count; i++) {
            for (int j = 0; j < CPPSimulator.cell_count; j++) {
                drawing_rects[i][j] = new Rect(offset_x + i * size_x, offset_y + j * size_y, offset_x + (i + 1) * size_x, offset_y + (j + 1) * size_y);
            }
        }
        //end calculation
    }

    @Override
    //! called when the viewsize gets changed
    protected void onSizeChanged(int w, int h, int old_w, int old_h) {

        drawing_rects = new Rect[CPPSimulator.cell_count][CPPSimulator.cell_count];//!< calculate the position and size of the drawingrects
        int size_x = w / CPPSimulator.cell_count;
        int offset_x = (w - size_x * CPPSimulator.cell_count) / 2;
        int size_y = h / CPPSimulator.cell_count;
        int offset_y = (h - size_y * CPPSimulator.cell_count) / 2;
        for (int i = 0; i < CPPSimulator.cell_count; i++) {
            for (int j = 0; j < CPPSimulator.cell_count; j++) {
                drawing_rects[i][j] = new Rect(offset_x + i * size_x, offset_y + j * size_y, offset_x + (i + 1) * size_x, offset_y + (j + 1) * size_y);
            }
        }
        //end calculation
    }

    @Override
    //! called when something changes in the domain
    public void onDraw(Canvas canvas) {
        
        boolean nomoremovement = true;
        float last_height = -1;

        for (int i = 0; i < CPPSimulator.cell_count; i++) {
            for (int j = 0; j < CPPSimulator.cell_count; j++) {
                if ((int) CPPSimulator.sim.getBathymetry(i, j) != 0) { //!<  Check if there is an obstacle
                    paint.setColor(Color.rgb(0, 151, 0));
                    canvas.drawRect(drawing_rects[i][j], paint);
                } else { //!> if not draw the water
                    float current_height = CPPSimulator.sim.getHeight(i, j);
                    if(last_height == -1) last_height = current_height;
                    float[] hsl = {220,1,0};
                    //!> determine new color
                    if(current_height< 4)//10%
                    {
                        hsl[2] = Helper.linear_map((float)4,(float)0.1,0,0,current_height);
                    }
                    else if(current_height>=4 && current_height < 4.5){ // 10%
                        hsl[2] = Helper.linear_map((float) 4.5,(float)0.2,(float)4,(float) 0.1,current_height);
                    }
                    else if(current_height>=4.5 && current_height < 5.5){ // 55%
                        hsl[2] = Helper.linear_map((float) 5.5,(float)0.75,(float)4.5,(float) 0.2,current_height);
                    }
                    else if (current_height>=5.5 && current_height < 6.5){ // 15%
                        hsl[2] = Helper.linear_map((float) 6.5,(float)0.9,(float)5.5,(float) 0.75,current_height);
                    }
                    else{ //10%
                        hsl[2] = (float)0.9;
                    }
                    //end determine
                    paint.setColor(ColorUtils.HSLToColor(hsl));
                    canvas.drawRect(drawing_rects[i][j], paint);

                    if(nomoremovement)nomoremovement = (Math.abs(last_height-current_height)< pause_sensitivity);//!< update nomoremovement
                    last_height = current_height;//!< set lasheight to currentheight


                }
            }
        }
        if(nomoremovement){
            MainActivity.getSimulationRunner().stop();//!> stops the simualtion  if there is no more movement
        }
    }

}

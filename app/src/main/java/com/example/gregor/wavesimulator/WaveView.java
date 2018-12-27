package com.example.gregor.wavesimulator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.MotionEvent;


import Solver.CPPSimulator;

public class WaveView extends View {
    private static final int cell_count = 100; //Todo: implement cell_count everywhere (eg in native code)
    private static int width;
    private static int height;
    public static CPPSimulator sim; //have to be static to allow screen rotation
    private Paint paint;
    public WaveView(Context context,AttributeSet attrs) {
        super(context,attrs);
        width = getWidth();
        height = getHeight();
        paint = new Paint();
        if(sim == null) {
            sim = new CPPSimulator();
        }
    }
   /* @Override
    public boolean onTouchEvent (MotionEvent event)
    {
        sim.simulatetimestep();
        invalidate();//used to trigger redraw
        return true;
    }*/
    @Override
    protected void onSizeChanged (int w, int h, int old_w, int old_h)
    {
        width = w;
        height = h;
    }
    @Override
    public void onDraw(Canvas canvas)
    {
        int size_x = width/ cell_count;
        int offset_x = (width - size_x * cell_count)/2;
        int size_y = height/ cell_count;
        int offset_y = (height - size_y * cell_count)/2;
       for(int i = 0; i < cell_count; i++)
        {
            for(int j = 0; j <cell_count; j++)
            {
                int current = (int)sim.getHeight(i,j)*10; //mal 10 weil wir 100 zellen haben, die domain des szenarios jedoch 1000 ist
                paint.setColor(Color.rgb(current,0,0));

                canvas.drawRect(offset_x + i*size_x,offset_y +j*size_y,offset_x +(i+1)*size_x,offset_y +(j+1)*size_y,paint);
            }
        }
    }
}

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

    private static int width;
    private static int height;

    private Paint paint;
    public WaveView(Context context,AttributeSet attrs) {
        super(context,attrs);
        width = getWidth();
        height = getHeight();
        paint = new Paint();
        if(CPPSimulator.sim == null) {
            CPPSimulator.sim = new CPPSimulator();
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
        int size_x = width/ CPPSimulator.cell_count;
        int offset_x = (width - size_x * CPPSimulator.cell_count)/2;
        int size_y = height/ CPPSimulator.cell_count;
        int offset_y = (height - size_y * CPPSimulator.cell_count)/2;
       for(int i = 0; i < CPPSimulator.cell_count; i++)
        {
            for(int j = 0; j <CPPSimulator.cell_count; j++)
            {
                int current = (int)CPPSimulator.sim.getHeight(i,j)*10; //10 is to increase colour intesity
                paint.setColor(Color.rgb(0,0,current));

                canvas.drawRect(offset_x + i*size_x,offset_y +j*size_y,offset_x +(i+1)*size_x,offset_y +(j+1)*size_y,paint);
            }
        }
    }
}

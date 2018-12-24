package com.example.gregor.wavesimulator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.MotionEvent;

import Solver.WaveSimulator;

public class WaveView extends View {
    private boolean touched = false;
    private int width;
    private int height;
    private WaveSimulator sim;
    Paint paint;
    public WaveView(Context context,AttributeSet attrs) {
        super(context,attrs);
        width = getWidth();
        height = getHeight();
        paint = new Paint();
        sim = new WaveSimulator();
    }
    @Override
    public boolean onTouchEvent (MotionEvent event)
    {
        sim.calculate_step();
        invalidate();//used to trigger redraw
        return true;
    }
    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh)
    {
        width = w;
        height = h;
    }
    @Override
    public void onDraw(Canvas canvas)
    {
        for(int i = 0; i < 100; i++)
        {
            for(int j = 0; j <100; j++)
            {
                int current = (int)sim.get(i,j)*10;
                paint.setColor(Color.rgb(current,0,0));
                canvas.drawRect(i*10,j*10,(i+1)*10,(j+1)*10,paint);
            }
        }
    }
}

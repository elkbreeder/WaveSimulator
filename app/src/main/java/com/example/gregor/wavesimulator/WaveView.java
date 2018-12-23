package com.example.gregor.wavesimulator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.MotionEvent;

public class WaveView extends View {
    private boolean touched = false;
    private int width;
    private int height;
    Paint paint;
    public WaveView(Context context,AttributeSet attrs) {
        super(context,attrs);
        width = getWidth();
        height = getHeight();
        paint = new Paint();
    }
    @Override
    public boolean onTouchEvent (MotionEvent event)
    {
        touched = true;
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
        if(touched)
        {
            paint.setColor(Color.BLACK);
            canvas.drawRect(100,100,width-100,height-50,paint);
            //canvas.drawText("Click",width/2-50 , height/2,paint);
        }
    }
}

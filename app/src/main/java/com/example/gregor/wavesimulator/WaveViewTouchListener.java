package com.example.gregor.wavesimulator;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import Solver.CPPSimulator;

public class WaveViewTouchListener implements View.OnTouchListener,GestureDetector.OnGestureListener,GestureDetector.OnDoubleTapListener {
    public boolean drawingmode;
    private GestureDetector gestureDetector;
    private MainActivity context;
    WaveViewTouchListener(MainActivity context)
    {
        gestureDetector = new GestureDetector(context,this);
        this.context = context;
        drawingmode =false;
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(drawingmode)
        {
            if( motionEvent.getAction() == MotionEvent.ACTION_MOVE)
            {
                WaveView waveView = context.findViewById(R.id.waveView);
                float cellsizex = waveView.getWidth()/CPPSimulator.cell_count;
                float cellsizey = waveView.getHeight()/CPPSimulator.cell_count;
                CPPSimulator.sim.placeCircle((int)(motionEvent.getX()/cellsizex),(int)(motionEvent.getY()/cellsizey),4);
                waveView.invalidate();
            }
        }
        else {
            gestureDetector.onTouchEvent(motionEvent);
        }
        return true;
    }
    //Gesture Detector
    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        WaveView waveView = context.findViewById(R.id.waveView);
        float cellsizex = waveView.getWidth()/CPPSimulator.cell_count;
        float cellsizey = waveView.getHeight()/CPPSimulator.cell_count;
        float waveheight = context.getWaveHeightValue();

        CPPSimulator.sim.setWave((int)(motionEvent.getX()/cellsizex),(int)(motionEvent.getY()/cellsizey),10, waveheight);//if you increase the waveheights you also have to adapt the drawing
        context.getSimulationRunner().start();
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent motionEvent) {
        WaveView waveView = context.findViewById(R.id.waveView);
        float cellsizex = waveView.getWidth()/CPPSimulator.cell_count;
        float cellsizey = waveView.getHeight()/CPPSimulator.cell_count;
        CPPSimulator.sim.placeCircle((int)(motionEvent.getX()/cellsizex),(int)(motionEvent.getY()/cellsizey),10);
        waveView.invalidate();
        return true;
    }
    @Override
    public boolean onDown(MotionEvent motionEvent) {

        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
        Log.i("Gest","on Down");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }
    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        if(context.getSimulationRunner().isStarted())
        {
            context.getSimulationRunner().stop();
        }
        else
        {
            context.getSimulationRunner().start();
        }

    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        Log.i("Gest","on Fling");
        return false;
    }
}

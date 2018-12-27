package com.example.gregor.wavesimulator;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import Solver.CPPSimulator;

public class WaveViewTouchListener implements View.OnTouchListener,GestureDetector.OnGestureListener {
    private GestureDetector gestureDetector;
    private MainActivity context;
    WaveViewTouchListener(MainActivity context)
    {
        gestureDetector = new GestureDetector(context,this);
        this.context = context;
    }
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        gestureDetector.onTouchEvent(motionEvent);
        return true;
    }
    //Gesture Detector
    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        WaveView waveView = context.findViewById(R.id.waveView);
        float cellsizex = waveView.getWidth()/CPPSimulator.cell_count;
        float cellsizey = waveView.getHeight()/CPPSimulator.cell_count;
        CPPSimulator.setWave((int)(motionEvent.getX()/cellsizex),(int)(motionEvent.getY()/cellsizey),10,15);
        context.getSimulationRunner().start();

        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {
        context.getSimulationRunner().start();
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
}

package com.example.gregor.wavesimulator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

import Solver.SimulationRunner;

public class MainActivity extends AppCompatActivity {
    private WaveView waveView;
    private Switch boundarySwitch;
    private static SimulationRunner simulationRunner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //section BoundarySwitch init
        boundarySwitch = findViewById(R.id.switch1);
        boundarySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTouchEventSwitch(v);
            }
        });//Defines the onClick  Listener for the switch
        //waveView init
        waveView = findViewById(R.id.waveView);
        waveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTouchEventViewer(v);
            }
        });//Defines the onClick  Listener for the WaveView
        if(simulationRunner == null) {
            simulationRunner = new SimulationRunner(this);
        }
        else
        {
            simulationRunner.changeActivity(this);
        }
    }

    public void onTouchEventViewer(View v) {
        simulationRunner.start();//starts/restarts the simulation
    }

    public void onTouchEventSwitch(View v) {

    }
    public WaveView getWaveView()
    {
        return waveView;
    }
}
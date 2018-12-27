package com.example.gregor.wavesimulator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import Solver.CPPSimulator;
import Solver.SimulationRunner;

public class MainActivity extends AppCompatActivity {
    private WaveView waveView;
    private Switch boundarySwitch;
    private Button reset;
    private static SimulationRunner simulationRunner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boundarySwitch = findViewById(R.id.switch1);
        boundarySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTouchEventSwitch(v);
            }
        });//Defines the onClick  Listener for the switch
        waveView = findViewById(R.id.waveView);
        waveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEventViewer(v);
            }
        });//Defines the onClick  Listener for the WaveView
        reset = findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEventReset(v);
            }
        });//Defines the onClick  Listener for the WaveView
        reset.setText("Reset");
        if(simulationRunner == null) simulationRunner = new SimulationRunner(this);
        else simulationRunner.changeActivity(this);

    }
    public void onClickEventReset(View v) {
        simulationRunner.stop();
        CPPSimulator.sim = new CPPSimulator();//reloads the Simulation;
    }
    public void onClickEventViewer(View v) {
        simulationRunner.start();//starts/stops the simulation
    }

    public void onTouchEventSwitch(View v) {

    }
    public WaveView getWaveView()
    {
        return waveView;
    }
}
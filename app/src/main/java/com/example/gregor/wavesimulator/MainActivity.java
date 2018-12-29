package com.example.gregor.wavesimulator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import Solver.CPPSimulator;
import Solver.SimulationRunner;

public class MainActivity extends AppCompatActivity {
    /*
    TodoList
    Todo: Detect minimum and maximum of the waves in the domain (should work like edge detection) and use it to get better drawing results (high priority)
    Todo: Implement Mode where the user is able to draw obstacles on the screen (high priority)
    Todo: Implement removal of obstacles (high priority)
    Todo: Refactor Layout
    Todo: add a 1d Simulationmode
    Todo: Add a Tutorial

     */
    private WaveView waveView;
    private Switch boundarySwitch;
    private Button reset;
    private WaveViewTouchListener waveViewTouchListener;
    private static SimulationRunner simulationRunner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boundarySwitch = findViewById(R.id.switch1);
        boundarySwitch.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onCheckedSwitch(buttonView,isChecked);
            }
        });//Defines the onClick  Listener for the switch
        waveView = findViewById(R.id.waveView);
        waveViewTouchListener = new WaveViewTouchListener(this);
        waveView.setOnTouchListener(waveViewTouchListener);


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
        CPPSimulator.reset();//reloads the Simulation;
        waveView.invalidate();
    }


    public void onCheckedSwitch(CompoundButton buttonView, boolean isChecked) {
        CPPSimulator.sim.setBoundaryType(isChecked);
    }
    public WaveView getWaveView()
    {
        return waveView;
    }
    public SimulationRunner getSimulationRunner() { return simulationRunner;}
}
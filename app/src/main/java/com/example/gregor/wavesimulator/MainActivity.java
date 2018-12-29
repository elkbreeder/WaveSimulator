package com.example.gregor.wavesimulator;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import java.text.DecimalFormat;

import Solver.CPPSimulator;
import Solver.Helper;
import Solver.SimulationRunner;

public class MainActivity extends AppCompatActivity {
    /*
    TodoList
    Todo: Implement Mode where the user is able to draw obstacles on the screen (high priority)
    Todo: Implement removal of obstacles (high priority)
    Todo: Refactor Layout
    Todo: add a 1d Simulationmode
    Todo: Add a Tutorial

     */
    private WaveView waveView;
    private Switch boundarySwitch;
    private Button reset;
    private Button resetWave;
    private SeekBar waveHeightSeekBar;
    private TextView waveHeightLabel;
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
        });//Defines the onClick  Listener for the Reset Button
        reset.setText("Reset");

        resetWave = findViewById(R.id.resetWave);
        resetWave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickEventResetWave(v);
            }
        });//Defines the onClick  Listener for the WaveView
        resetWave.setText("Reset Waves");

        waveHeightLabel = findViewById(R.id.WaveStrengthLabel);

        waveHeightSeekBar = findViewById(R.id.WaveStrengthseekBar);
        waveHeightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                WaveHeightSeekBarChanged(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        waveHeightSeekBar.setProgress(100);



        if(simulationRunner == null) simulationRunner = new SimulationRunner(this);
        else simulationRunner.changeActivity(this);

    }
    @SuppressLint("SetTextI18n")
    public void WaveHeightSeekBarChanged(int progress)
    {      DecimalFormat df = new DecimalFormat("0.0");
        waveHeightLabel.setText("Wave height: " + ((getWaveHeightValue()==10)?"9.9":df.format(getWaveHeightValue())));
    }
    public float getWaveHeightValue()
    {
        return Helper.linear_map(0,5,100,10,waveHeightSeekBar.getProgress());
    }
    public void onClickEventReset(View v) {
        simulationRunner.stop();
        CPPSimulator.reset();//reloads the Simulation;
        CPPSimulator.sim.setBoundaryType(boundarySwitch.isChecked());
        waveView.invalidate();
    }
    public void onClickEventResetWave(View v) {
        CPPSimulator.resetWaves();
        simulationRunner.stop();
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
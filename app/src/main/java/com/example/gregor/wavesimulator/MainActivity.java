package com.example.gregor.wavesimulator;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import java.text.DecimalFormat;

import Solver.CPPSimulator;
import Solver.Helper;
import Solver.SimulationRunner;

public class MainActivity extends AppCompatActivity {
    /*
    TodoList

    Todo: Implement removal of obstacles (high priority)
    Todo: add a 1d Simulationmode
    Todo: Add a Tutorial
    Todo: remove dependencies from hardcoded domainsize

     */
    private Menu menu;
    private WaveView waveView;
    private Toolbar actionBar;
    private Switch boundarySwitch;
    private SeekBar waveHeightSeekBar;
    private TextView waveHeightLabel;
    private WaveViewTouchListener waveViewTouchListener;
    private static SimulationRunner simulationRunner;

/*    <include
        android:id="@+id/toolbar"
        layout="@layout/toolbar"/>*/


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



        waveHeightLabel = findViewById(R.id.WaveStrengthLabel);

        waveHeightSeekBar = findViewById(R.id.WaveStrengthseekBar);
        waveHeightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                WaveHeightSeekBarChanged();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        waveHeightSeekBar.setProgress(100);


        actionBar = findViewById(R.id.toolbar);
        setSupportActionBar(actionBar);


        if(simulationRunner == null) simulationRunner = new SimulationRunner(this);
        else simulationRunner.changeActivity(this);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings_reset:
                simulationRunner.stop();
                CPPSimulator.reset();//reloads the Simulation;
                CPPSimulator.sim.setBoundaryType(boundarySwitch.isChecked());
                waveView.invalidate();
                break;
            // action with ID action_settings was selected
            case R.id.action_settings_reset_wave:
                CPPSimulator.resetWaves();
                simulationRunner.stop();
                waveView.invalidate();
                break;
            case R.id.action_brush:
                if(waveViewTouchListener.drawingmode)
                {
                    menu.getItem(0).setIcon(R.drawable.ic_brush_stroke_24dp);
                    waveViewTouchListener.drawingmode = false;
                }
                else
                {
                    menu.getItem(0).setIcon(R.drawable.ic_brush_black_24dp);
                    waveViewTouchListener.drawingmode = true;
                }
                break;
            default:
                break;
        }
        return true;
    }

    @SuppressLint("SetTextI18n")
    public void WaveHeightSeekBarChanged()
    {
        DecimalFormat df;
        df = new DecimalFormat("0.0");
        waveHeightLabel.setText("Wave height: " + ((getWaveHeightValue()==10)?"9.9":df.format(getWaveHeightValue())));
    }
    public float getWaveHeightValue()
    {
        return Helper.linear_map(0,5,100,10,waveHeightSeekBar.getProgress());
    }
    public void onCheckedSwitch(CompoundButton buttonView, boolean isChecked) {
        CPPSimulator.sim.setBoundaryType(isChecked);
    }
    public WaveView getWaveView()
    {
        return waveView;
    }
    public static SimulationRunner getSimulationRunner() { return simulationRunner;}
}
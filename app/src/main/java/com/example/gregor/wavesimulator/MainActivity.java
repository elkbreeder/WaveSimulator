package com.example.gregor.wavesimulator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {
    private WaveView waveView;
    private Switch boundarySwitch;
    private Thread simulation_thread;
    private int simulation_steps;
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
    }
    public void onTouchEventViewer(View v)
    {
        if(simulation_steps == 0) {
            simulation_steps = 50;
            simulation_thread = new Thread(new Runnable() {
                public void run() {
                    for (int i = 0; i <simulation_steps; i++) {
                        waveView.simulate_step();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                waveView.invalidate();
                            }

                        });
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            simulation_thread.start();
        }
        else
        {
            simulation_steps = 0;

        }
    }
    public void onTouchEventSwitch (View v)
    {

    }
}

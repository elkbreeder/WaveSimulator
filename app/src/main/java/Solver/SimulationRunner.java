package Solver;

import com.example.gregor.wavesimulator.MainActivity;
import com.example.gregor.wavesimulator.WaveView;
//Class which handles the Threading of a Simulation
public class SimulationRunner {
    private Thread simulation_thread;
    private boolean started;
    private MainActivity currentActivity;
    public SimulationRunner(MainActivity currentActivity)
    {
        this.currentActivity = currentActivity;
        started = false;

    }
    public void changeActivity(MainActivity a)
    {
        currentActivity = a;
    }
    public void start()
    {
        if (!started) {
            started = true;
            simulation_thread = new Thread(new Runnable() {
                public void run() {
                   while(started) {
                        WaveView.sim.simulatetimestep();
                        currentActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                currentActivity.getWaveView().invalidate();
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
            started = false;
        }
    }
}

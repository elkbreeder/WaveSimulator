package Solver;

import wavesimulator.MainActivity;

//Class which handles the Threading of a Simulation
public class SimulationRunner {
    private Thread simulation_thread;
    private boolean started;
    private MainActivity currentActivity;

    public SimulationRunner(MainActivity currentActivity) {
        this.currentActivity = currentActivity;
        started = false;

    }

    public void changeActivity(MainActivity a) {
        currentActivity = a;
    }

    public void stop() {
        started = false;
    }

    public void start() {
        if (!started) {
            started = true;
            simulation_thread = new Thread(new Runnable() {
                public void run() {
                    long start_time;
                    long duration;
                    long sleeptime = 20;
                    while (started) {
                        start_time = java.lang.System.currentTimeMillis();
                        CPPSimulator.sim.simulatetimestep();
                        currentActivity.runOnUiThread(new Runnable() {
                            public void run() {
                                currentActivity.getWaveView().invalidate();
                            }
                        });
                        duration = java.lang.System.currentTimeMillis() - start_time;
                        try {
                            Thread.sleep((sleeptime-duration>0)?sleeptime-duration:0);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            simulation_thread.start();
        }
    }

    public boolean isStarted() {
        return started;
    }
}

package Interface;//! \namespace wavesimulator \brief a The wavesimulator entails all classes dedicated to the visual representation and user interaction

import android.annotation.SuppressLint;
import android.support.design.widget.Snackbar;
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
import gregor.wavesimulator.R;

//! \class MainActivity \brief a This class handles the main-Activities
public class MainActivity extends AppCompatActivity {
    /*
    TodoList

    Todo: add a 1d Simulationmode
    Todo: Add a Tutorial
    Todo: remove dependencies from hardcoded domainsize

     */

    //Javaclass which corresponds to the main activity
    private Menu menu;
    private WaveView waveView;
    private Toolbar actionBar;
    private Switch boundarySwitch;
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
        });//!< Defines the onClick  Listener for the switch
        waveView = findViewById(R.id.waveView);
        waveViewTouchListener = new WaveViewTouchListener(this);
        waveView.setOnTouchListener(waveViewTouchListener);//!< Defines Touchlistener of the Waveview



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
        });//!< set Listener of the waveheight seekbar
        waveHeightSeekBar.setProgress(100);


        actionBar = findViewById(R.id.toolbar);
        setSupportActionBar(actionBar);//!< add the toolbar to the main activity


        if(simulationRunner == null) simulationRunner = new SimulationRunner(this); //!< create a new simulation runner or set the current activity in the existing one
        else simulationRunner.changeActivity(this);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu,menu);//!< sets the custom menu to the actionbar
        return true;
    }
    @Override
    //!gets  called if a menu item is selected
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings_reset: //!< reset is selected
                simulationRunner.stop();
                CPPSimulator.reset();//!< reloads the Simulation;
                CPPSimulator.sim.setBoundaryType(boundarySwitch.isChecked());//!< resetboundary Switch
                waveView.invalidate();//redraw
                break;
            case R.id.action_settings_reset_wave://!< reset wave is selected
                CPPSimulator.resetWaves();
                simulationRunner.stop();
                waveView.invalidate();//redraw
                break;
            case R.id.action_brush://!< brush mode is selected
                if(waveViewTouchListener.drawingmode == WaveViewTouchListener.MODE_DRAW)
                {
                    //!> if its already in the draw mode, disable draw mode
                    menu.getItem(1).setIcon(R.drawable.ic_brush_stroke_24dp);
                    waveViewTouchListener.drawingmode = WaveViewTouchListener.MODE_SIMULATE;
                    Snackbar.make(findViewById(R.id.masterView), R.string.mode_draw_disabled ,Snackbar.LENGTH_SHORT).show();//show snackbar which explains current action
                }
                else
                {
                    //!> if not, disable erase mode and enable drawmode
                    menu.getItem(1).setIcon(R.drawable.ic_brush_black_24dp);
                    menu.getItem(0).setIcon(R.drawable.ic_remove_circle_outline_24dp);
                    waveViewTouchListener.drawingmode =  WaveViewTouchListener.MODE_DRAW;
                    Snackbar.make(findViewById(R.id.masterView), R.string.mode_draw ,Snackbar.LENGTH_SHORT).show();//show snackbar which explains current action
                }
                break;
            case R.id.action_eraser://!< erase mode is selected
                if(waveViewTouchListener.drawingmode == WaveViewTouchListener.MODE_EREASE)
                {
                    //!> if its already in the erase mode, disable erase mode
                    menu.getItem(0).setIcon(R.drawable.ic_remove_circle_outline_24dp);
                    waveViewTouchListener.drawingmode = WaveViewTouchListener.MODE_SIMULATE;
                    Snackbar.make(findViewById(R.id.masterView), R.string.mode_erase_disabled ,Snackbar.LENGTH_SHORT).show();//show snackbar which explains current action
                }
                else
                {
                    //!> if not, disable draw mode and enable erase mode
                    menu.getItem(1).setIcon(R.drawable.ic_brush_stroke_24dp);
                    menu.getItem(0).setIcon(R.drawable.ic_remove_circle_24dp);
                    waveViewTouchListener.drawingmode = WaveViewTouchListener.MODE_EREASE;
                    Snackbar.make(findViewById(R.id.masterView), R.string.mode_erase ,Snackbar.LENGTH_SHORT).show();//!> show snackbar which explains current action
                }
                break;
            default:
                break;
        }
        return true;
    }

    @SuppressLint("SetTextI18n")
    //! gets called when the user uses the waveheight seekbar
    public void WaveHeightSeekBarChanged()
    {
        
        DecimalFormat df;
        df = new DecimalFormat("0.0");
        waveHeightLabel.setText("Wave height: " + ((getWaveHeightValue()==10)?"9.9":df.format(getWaveHeightValue())));//!< inform user about current waveheight
    }
    public float getWaveHeightValue()
    {
        return Helper.linear_map(0,5,100,10,waveHeightSeekBar.getProgress());//!< maps the seekbarvalue(0,100) to the waveheight(5,15)
    }
    //! gets called when the user uses the wall boundary switch
    public void onCheckedSwitch(CompoundButton buttonView, boolean isChecked) {
        
        CPPSimulator.sim.setBoundaryType(isChecked);
    }
    public WaveView getWaveView()
    {
        return waveView;
    }
    public static SimulationRunner getSimulationRunner() { return simulationRunner;}
}
package Interface;
import gregor.wavesimulator.R;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import Solver.CPPSimulator;

//! \class WaveViewTouchListener \brief This class implemets the recation of the displayed content to the users touch-inputs
public class WaveViewTouchListener implements View.OnTouchListener,GestureDetector.OnGestureListener,GestureDetector.OnDoubleTapListener {
    public static final int MODE_SIMULATE = 0;
    public static final int MODE_DRAW = 1;
    public static final int MODE_EREASE = 2;
    private final int thickness_line = 8;
    public int drawingmode;
    private GestureDetector gestureDetector;
    private MainActivity context;
    private int last_drawingX;
    private int last_drawingY;

    WaveViewTouchListener(MainActivity context)
    {
        gestureDetector = new GestureDetector(context,this);
        this.context = context;
        drawingmode = MODE_SIMULATE;
        last_drawingX = last_drawingY = -1;
    }
    @Override
    //! gets called if the waveview is touched
    public boolean onTouch(View view, MotionEvent motionEvent) {
        
        if(drawingmode == MODE_DRAW)//! -> Actions when drawing
        {
            if( motionEvent.getAction() == MotionEvent.ACTION_MOVE)//!< If a Moveevent is detected
            {
                WaveView waveView = context.findViewById(R.id.waveView);
                float cellsizex = waveView.getWidth()/CPPSimulator.cell_count;
                float cellsizey = waveView.getHeight()/CPPSimulator.cell_count;
                int curr_X = (int)(motionEvent.getX()/cellsizex);
                int curr_Y = (int)(motionEvent.getY()/cellsizey); //!< calculate the current finger position in the domain
                if(last_drawingX == -1 || last_drawingY == -1){ //!> if there is no last fingerpoint position, initialize with the currentposition
                    last_drawingX = curr_X;
                    last_drawingY = curr_Y;
                }
                float vector_x = curr_X-last_drawingX;
                float vector_y =  curr_Y -last_drawingY; //!> calculate the vector between last and current position
                float vectorlength =(float)Math.sqrt(vector_x*vector_x+vector_y*vector_y);
                vector_x = vector_x/vectorlength;
                vector_y = vector_y/vectorlength;//!> calculate unit vector
                int steps = (int)vectorlength/(thickness_line/2); //!> calculate the steps
                for(int i = 0; i < steps ; i++)
                {
                    CPPSimulator.sim.placeCircle(last_drawingX+(int) ((thickness_line/2)*vector_x*i),last_drawingY+(int) ((thickness_line/2)*vector_y*i),thickness_line/2);
                    //!> place a circle at every step
                }
                CPPSimulator.sim.placeCircle(curr_X,curr_Y,thickness_line/2);
                last_drawingX = curr_X;
                last_drawingY = curr_Y;
                waveView.invalidate();//!> redraw view
            }
            else
            {
                last_drawingX = last_drawingY = -1;
            }
        }
        else if(drawingmode == MODE_EREASE)//! -> Actions when erasing
        {
            if( motionEvent.getAction() == MotionEvent.ACTION_MOVE)//!< If a Moveevent is detected
            {
                WaveView waveView = context.findViewById(R.id.waveView);
                float cellsizex = waveView.getWidth()/CPPSimulator.cell_count;
                float cellsizey = waveView.getHeight()/CPPSimulator.cell_count;
                int curr_X = (int)(motionEvent.getX()/cellsizex);
                int curr_Y = (int)(motionEvent.getY()/cellsizey); //!>calculate the current finger position in the domain
                if(last_drawingX == -1 || last_drawingY == -1){ //!> if there is no last fingerpoint position, initialize with the currentposition
                    last_drawingX = curr_X;
                    last_drawingY = curr_Y;
                }
                float vector_x = curr_X-last_drawingX;
                float vector_y =  curr_Y -last_drawingY; //!> calculate the vector between last and current position
                float vectorlength =(float)Math.sqrt(vector_x*vector_x+vector_y*vector_y);
                vector_x = vector_x/vectorlength;
                vector_y = vector_y/vectorlength;//!> calculate unit vector
                int steps = (int)vectorlength/(thickness_line/2); //calculate the steps
                for(int i = 0; i < steps ; i++)
                {
                    CPPSimulator.sim.delete(last_drawingX+(int) ((thickness_line/2)*vector_x*i),last_drawingY+(int) ((thickness_line/2)*vector_y*i),thickness_line/2);
                    //!> place a circle at every step
                }
                CPPSimulator.sim.delete(curr_X,curr_Y,thickness_line/2);
                last_drawingX = curr_X;
                last_drawingY = curr_Y;
                waveView.invalidate();//!> redraw view
            }
            else
            {
                last_drawingX = last_drawingY = -1;
            }
        }
        else {
            last_drawingX = last_drawingY = -1;
            gestureDetector.onTouchEvent(motionEvent);
        }
        return true;
    }
    //Gesture Detector
    @Override
    //! adaption at single tap
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        WaveView waveView = context.findViewById(R.id.waveView);
        float cellsizex = waveView.getWidth()/CPPSimulator.cell_count;
        float cellsizey = waveView.getHeight()/CPPSimulator.cell_count;
        float waveheight = context.getWaveHeightValue();

        CPPSimulator.sim.setWave((int)(motionEvent.getX()/cellsizex),(int)(motionEvent.getY()/cellsizey),10, waveheight);
        //!< if you increase the waveheights you also have to adapt the drawing
        context.getSimulationRunner().start();
        return true;
    }

    @Override
    //! adaption at double-tap
    public boolean onDoubleTap(MotionEvent motionEvent) {
        if(context.getSimulationRunner().isStarted())//!< starts or stops the simulation
        {
            context.getSimulationRunner().stop();
        }
        else
        {
            context.getSimulationRunner().start();
        }
        return true;
    }
    @Override
    //! adaption at down-mm
    public boolean onDown(MotionEvent motionEvent) {

        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }
    @Override
    public boolean onDoubleTapEvent(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    //! adaption at press and hold
    public void onLongPress(MotionEvent motionEvent) {
        //!< places a circle at the pressed location
        WaveView waveView = context.findViewById(R.id.waveView);
        float cellsizex = waveView.getWidth()/CPPSimulator.cell_count;
        float cellsizey = waveView.getHeight()/CPPSimulator.cell_count;
        CPPSimulator.sim.placeCircle((int)(motionEvent.getX()/cellsizex),(int)(motionEvent.getY()/cellsizey),10); //set a large circle at finger position
        waveView.invalidate();


    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }
}

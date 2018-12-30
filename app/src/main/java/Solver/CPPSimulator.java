package Solver;


import android.util.Log;

import java.time.Duration;
import java.time.Instant;

//all CPPSimulator Objects uses the Same Native Object!
public class CPPSimulator {
    public static final  float waterlevel = 5;
    public static final int cell_count = 100; /*Todo: implement cell_count everywhere (eg in native code)

    known dependencies:
    Wrapper.cpp  Java_Solver_CPPSimulator_setWave    */
    public static CPPSimulator sim; //have to be static to allow screen rotation

    private static long SWE_Pointer = 0;
    public CPPSimulator(){
        System.loadLibrary("SWELib");
        if(SWE_Pointer == 0) {
            reset();
        }
        //sim.placeCircle(20,20,10);
        //setWave(20,20,5,25);
    }
    public synchronized void setWave(int x, int y, int r, float h)
    {
        setWave(x,y,r,h,SWE_Pointer);

        //SWE_Pointer = setWave(x,y,r,h,SWE_Pointer);
    }
    public synchronized void placeCircle(int x, int y, int r)
    {
        placeCircle(x,y,r,SWE_Pointer);
    }
    public void setBoundaryType(boolean isWall)
    {
        setBoundaryType(isWall,SWE_Pointer);
    }
    public static void reset()
    {
        SWE_Pointer = init();//creates a new SWE_Block Object
        resetWaves();
    }
    public float getHeight(int x, int y)
    {
        return getHeight(x,y,SWE_Pointer);
    }
    public float getBathymetry(int x, int y)
    {
        return getBathymetry(x,y,SWE_Pointer);
    }
    public void delete(int x, int y,int r)
    {
        delete(x,y,r,SWE_Pointer);
    }
    public synchronized void simulatetimestep()
    {

        simulatetimestep(SWE_Pointer);

    }
    public synchronized static void resetWaves()
    {
        resetWaveHeights(waterlevel,SWE_Pointer);
    }
    protected void finalize()
    {
        delete(SWE_Pointer);//free up Memory
    }
    private static native long init();
    private static native void setWave(int x, int y, int r, float h,long ptr);
    private static native void delete(long ptr);
    private static native float getHeight(int x, int y, long ptr);
    private static native float getBathymetry(int x, int y, long ptr);
    private static native void simulatetimestep(long ptr);
    private static native void setBoundaryType(boolean isWall,long ptr);
    private static native void placeCircle(int x, int y, int r, long ptr);
    private static native void placeRect(int left, int right, int top, int bottom, long ptr);
    private static native void resetWaveHeights(float h, long ptr);
    private static native void delete(int x, int y,int r,long ptr);
}

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import Solver.CPPSimulator;


public class SolverTest {
    public boolean checkWaterLevels(){
        for(int i = 0; i < CPPSimulator.cell_count; i++){
            for (int j = 0; j < CPPSimulator.cell_count; j++) {
                if(CPPSimulator.sim.getHeight(i,j)!= CPPSimulator.waterlevel){
                    return false;
                }
            }
        }
        return true;
    }
    @Before
    public void setUp(){

        CPPSimulator.sim = new CPPSimulator();
        CPPSimulator.sim.reset();
    }
    @Test
    public void resetWaveTest(){
        float waveheight = 9;

        CPPSimulator.sim.setWave(50,50,10, waveheight);
        CPPSimulator.sim.simulatetimestep();
        CPPSimulator.sim.resetWaves();
        for(int i = 0; i < CPPSimulator.cell_count; i++){
            for (int j = 0; j < CPPSimulator.cell_count; j++) {
                assert(CPPSimulator.sim.getHeight(i,j)== CPPSimulator.waterlevel);
            }
        }
    }
    @Test
    public void resetWaveWithObstacleTest(){
        float waveheight = 9;
        for(int i = 0; i < CPPSimulator.cell_count; i++){
            CPPSimulator.sim.placeCircle(CPPSimulator.cell_count/2,i,1);
        }
        CPPSimulator.sim.setWave(50,50,10, waveheight);
        CPPSimulator.sim.simulatetimestep();

        CPPSimulator.sim.resetWaves();

        for(int i = 0; i < CPPSimulator.cell_count; i++){
            assert(CPPSimulator.sim.getHeight(CPPSimulator.cell_count/2,i) == 0 && CPPSimulator.sim.getBathymetry(CPPSimulator.cell_count/2,i)> 0);
        }
    }
    @Test
    public void outFlowBoundingConditionTest(){
        float waveheight = 9;

        CPPSimulator.sim.setWave(50,50,10, waveheight);
        CPPSimulator.sim.setBoundaryType(false);
        for (int i = 0; i < 20 ; i++) {
            CPPSimulator.sim.simulatetimestep();
            if(checkWaterLevels())return;
        }
        assert(false);

    }
    @Test
    public void reflectingBoundaryConditionTest(){
        float waveheight = 9;

        CPPSimulator.sim.setWave(50,50,10, waveheight);
        CPPSimulator.sim.setBoundaryType(true);
        for (int i = 0; i < 40 ; i++) {
            CPPSimulator.sim.simulatetimestep();
            assert(!checkWaterLevels());
        }

    }
    @Test
    public void timestampSimulationTest(){

    }
    @Test
    public void obstacleTest(){

    }

}

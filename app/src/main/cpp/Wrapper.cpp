#include "Solver_CPPSimulator.h"
#include "scenarios/SWE_simple_scenarios.hh"
#include "scenarios/SWE_Scenario.hh"
#include "blocks/SWE_WavePropagationBlock.hh"
#include <cmath>
#include "tools/help.hh"
JNIEXPORT jlong JNICALL Java_Solver_CPPSimulator_init(JNIEnv *, jclass)
{
    SWE_RadialDamBreakScenario l_scenario;
    int l_nX = 100;
    int l_nY = 100;
    float l_dX = (l_scenario.getBoundaryPos(BND_RIGHT) - l_scenario.getBoundaryPos(BND_LEFT) )/l_nX;
    float l_dY = (l_scenario.getBoundaryPos(BND_TOP) - l_scenario.getBoundaryPos(BND_BOTTOM) )/l_nY;
    SWE_WavePropagationBlock* block= new SWE_WavePropagationBlock(l_nX,l_nY,l_dX,l_dY);
    float l_originX = l_scenario.getBoundaryPos(BND_LEFT);
    float l_originY = l_scenario.getBoundaryPos(BND_BOTTOM);
    block->initScenario(l_originX, l_originY, l_scenario);
    return (long)(block);
}
JNIEXPORT void JNICALL Java_Solver_CPPSimulator_setWave(JNIEnv *, jclass, jint x, jint y, jint r, jfloat h_wave, jlong ptr)
{
    SWE_WavePropagationBlock *block = (SWE_WavePropagationBlock *)ptr;
    const Float2D &b = block->getBathymetry();
    for(int i = 0;i< 100;i++)
    {
        for(int j = 0;j < 100; j++)
        {
            if(std::sqrt(((float)i-(float)x)*((float)i-(float)x) + ((float)j-(float)y)*((float)j-(float)y)) < (float)r)
            {
                if(b[i+1][j+1]==0) {
                    block->setWaterHeightXY(i+1,j+1,h_wave);
                }

            }
        }
    }
}
JNIEXPORT void JNICALL Java_Solver_CPPSimulator_setBoundaryType(JNIEnv *, jclass, jboolean isWall, jlong ptr){
    SWE_WavePropagationBlock *block = (SWE_WavePropagationBlock *)ptr;
    BoundaryType current =(isWall)? WALL:OUTFLOW;
    block->setBoundaryType(BND_LEFT, current);
    block->setBoundaryType(BND_RIGHT, current);
    block->setBoundaryType(BND_BOTTOM, current);
    block->setBoundaryType(BND_TOP, current);
}
JNIEXPORT void JNICALL Java_Solver_CPPSimulator_placeCircle(JNIEnv *, jclass, jint x, jint y, jint r, jlong ptr)
{
    SWE_WavePropagationBlock *block = (SWE_WavePropagationBlock *)ptr;
    for(int i = 0;i< 100;i++)
    {
        for(int j = 0;j < 100; j++)
        {
            if(std::sqrt(((float)i-(float)x)*((float)i-(float)x) + ((float)j-(float)y)*((float)j-(float)y)) < (float)r)
            {
                block->setBathymetryXY(i+1,j+1,1000);
                block->setWaterHeightXY(i+1,j+1,0);
            }
        }
    }

}
JNIEXPORT void JNICALL Java_Solver_CPPSimulator_placeRect(JNIEnv *, jclass, jint, jint, jint, jint, jlong)
{

}
JNIEXPORT jfloat JNICALL Java_Solver_CPPSimulator_getHeight(JNIEnv *, jclass, jint x, jint y, jlong ptr)
{
    SWE_WavePropagationBlock *block = (SWE_WavePropagationBlock *)ptr;
    const Float2D &h = block->getWaterHeight();
    const float* fret = &h[x+1][y+1];
    return *fret;
}
JNIEXPORT jfloat JNICALL Java_Solver_CPPSimulator_getBathymetry(JNIEnv *, jclass, jint x, jint y, jlong ptr)
{
    SWE_WavePropagationBlock *block = (SWE_WavePropagationBlock *)ptr;
    const Float2D &b = block->getBathymetry();
    const float* fret = &b[x+1][y+1];
    return *fret;
}
JNIEXPORT void JNICALL Java_Solver_CPPSimulator_simulatetimestep(JNIEnv *, jclass, jlong ptr)
{
    SWE_WavePropagationBlock *block = (SWE_WavePropagationBlock *)ptr;
    block->setGhostLayer();
    block->computeNumericalFluxes();
    float l_maxTimeStepWidth = block->getMaxTimestep();
    block->updateUnknowns(l_maxTimeStepWidth);
}
JNIEXPORT void JNICALL Java_Solver_CPPSimulator_delete(JNIEnv *, jclass, jlong ptr)
{
    delete (SWE_WavePropagationBlock *)(ptr);
}


#include "Solver_CPPSimulator.h"
#include "scenarios/SWE_simple_scenarios.hh"
#include "blocks/SWE_WavePropagationBlock.hh"
#include <android/log.h>
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

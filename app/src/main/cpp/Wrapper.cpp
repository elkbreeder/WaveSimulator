#include "Solver_CPPSimulator.h"
#include "scenarios/SWE_simple_scenarios.hh"
#include "blocks/SWE_WavePropagationBlock.hh"
JNIEXPORT jlong JNICALL Java_Solver_CPPSimulator_init(JNIEnv *, jclass)
{
    SWE_RadialDamBreakScenario l_scenario;
    int l_nX = 100;
    int l_nY = 100;
    float l_dX = (l_scenario.getBoundaryPos(BND_RIGHT) - l_scenario.getBoundaryPos(BND_LEFT) )/l_nX;
    float l_dY = (l_scenario.getBoundaryPos(BND_TOP) - l_scenario.getBoundaryPos(BND_BOTTOM) )/l_nY;
    return (long)(new SWE_WavePropagationBlock(l_nX,l_nY,l_dX,l_dY));
}

JNIEXPORT jfloat JNICALL Java_Solver_CPPSimulator_getHeight(JNIEnv *, jobject, jint x, jint y)
{

}
JNIEXPORT jfloat JNICALL Java_Solver_CPPSimulator_getBathymetry(JNIEnv *, jobject, jint x, jint y)
{

}
JNIEXPORT void JNICALL Java_Solver_CPPSimulator_simulatetimestep(JNIEnv *, jobject)
{

}
JNIEXPORT void JNICALL Java_Solver_CPPSimulator_delete(JNIEnv *, jclass, jlong ptr)
{
    delete (SWE_WavePropagationBlock *)(ptr);
}

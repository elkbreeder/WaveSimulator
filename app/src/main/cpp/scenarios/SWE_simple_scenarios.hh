/**
 * @file
 * This file is part of SWE.
 *
 * @author Michael Bader, Kaveh Rahnema, Tobias Schnabel
 * @author Sebastian Rettenberger (rettenbs AT in.tum.de, http://www5.in.tum.de/wiki/index.php/Sebastian_Rettenberger,_M.Sc.)
 *
 * @section LICENSE
 *
 * SWE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SWE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SWE.  If not, see <http://www.gnu.org/licenses/>.
 *
 *
 * @section DESCRIPTION
 *
 * TODO
 */

#ifndef __SWE_SIMPLE_SCENARIOS_H
#define __SWE_SIMPLE_SCENARIOS_H

#include <cmath>
#include "../tools/help.hh"
#include "SWE_Scenario.hh"

/**
 * Scenario "Radial Dam Break":
 * elevated water in the center of the domain
 */
//! \class SWE_FlatScenario \brief This scenario is a test environment that simulates a flat water-surface
class SWE_FlatScenario : public SWE_Scenario {

public:

    float getBathymetry(float x, float y) {
        return 0.f;
    };

    float getWaterHeight(float x, float y) {
        return 5.0f;
        //return ( sqrt( (x-500.f)*(x-500.f) + (y-500.f)*(y-500.f) ) < 100.f ) ? 15.f: 10.0f;
    };

    virtual float endSimulation() { return (float) 15; };

    virtual BoundaryType getBoundaryType(BoundaryEdge edge) { return OUTFLOW; };

    /** Get the boundary positions
     *
     * @param i_edge which edge
     * @return value in the corresponding dimension
     */
    float getBoundaryPos(BoundaryEdge i_edge) {
        if ( i_edge == BND_LEFT )
            return (float)0;
        else if ( i_edge == BND_RIGHT)
            return (float)1000;
        else if ( i_edge == BND_BOTTOM )
            return (float)0;
        else
            return (float)1000;
    };
};
#endif
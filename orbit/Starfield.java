/*
    This file is part of Orbit.

    Orbit is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Orbit is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Orbit.  If not, see <http://www.gnu.org/licenses/>. 
    
    This software was developed by members of Project:Possibility, a software 
    collaboration for the disabled.
    
    For more information, visit http://projectpossibility.org
*/


package orbit;

import java.util.*;

/**
 * Animates the stars
 * @author Henry Yuen
 *
 */
public class Starfield {

	private final int NUM_STARS = 50;	//per layer

	ArrayList<Star> starLayer[];

	public Starfield() {
		starLayer = new ArrayList[3];
		for (int l=0;l<3;l++)
			starLayer[l] = new ArrayList<Star>();

		//generate stars
		generateStars();
	}

	/**
	 * this will randomly populate the screen with stars
	 *
	 */
	private void generateStars() {

		for (int l=0;l<3;l++) {
			for (int s=0;s<NUM_STARS;s++) {
				Star star = new Star(l);
				starLayer[l].add(star);
			}

		}
	}

	/**
	 * updates the individual stars
	 */
	public void update(long timeElapsed,Vector2 dir) {
		for (int l=0;l<3;l++) {
			for (Star s : starLayer[l]) {
				s.update(timeElapsed,dir);
			}
		}
	}

	/**
	 * This returns the stars in the particular layer, 0-2.
	 */
	public ArrayList<Star>[] getStarLayers() {
		return starLayer;
	}

}

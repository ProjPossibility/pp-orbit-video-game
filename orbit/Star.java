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

/**
 * Stars for starfields.
 */
public class Star extends SpaceObject {

	private double speed;

	public Star(int layer) {
		super();

		//randomly generate its position
		pos.x = Math.random();
		pos.y = Math.random();

		vel.x = 0; //there's no horizontal speed

		sprite = "star";

		//based on the layer, calculate the speed at which it should be going
		switch (layer) {
		case 0: {	//this is the stationary layer
			speed = 0.001;
			width = height = 2;
			break;
		}
		case 1: {
			speed = 0.01;
			width = height = 6;
			break;
		}
		case 2: {
			speed = 0.02;
			width = height = 8;
			break;
		}
		}
	}


	/**
	 * This is the update method for the stars. Merely checks if it went off the screen,
	 * after adding the velocity to the position
	 */
	public void update(long timeElapsed,Vector2 dir) {

		vel.x = -dir.x/900;
		vel.y = -dir.y/900;
		vel = vel.scale(speed);

		pos.x += vel.x*timeElapsed*0.02;
		pos.y += vel.y*timeElapsed*0.02;

		//update its position
		if (pos.x < 0) {
			pos.x = 1.0;
		}

		if (pos.y < 0) {
			pos.y = 1.0;
		}

		if (pos.x > 1.0) pos.x = 0;
		if (pos.y > 1.0) pos.y = 0;

	}




}
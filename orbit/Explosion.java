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

/*
 * Models an explosion. An explosion is a space object that can animate but dies after it ends
 * its animation life.
 */
public class Explosion extends SpaceObject{

	public Explosion(Vector2 p, String sprite, double width, double height) {
		super(p, new Vector2(0,0), new Vector2(0,0), sprite, width, height);
		setAnimationProperties(20, 18, true);
		alive = true;
	}
	
	public void animate(int msSinceLastTime) {
		if(currentFrame!=-1) {
			super.nonLoopingAnimate(msSinceLastTime);
		} else {
			looping=false;
			alive = false;
		}
	}
}

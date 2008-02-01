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

public class Powerup extends SpaceObject {

	public static final int EXTRA_LIFE = 0;
	public static final int SPEED_BOOST = 1;
	public static final int NUM_TYPE_POWERUPS = 2;

	private int type;

	public Powerup(int type,Vector2 pos) {
		super(pos,new Vector2(),new Vector2(),null,64,64);

		alive = true;
		this.type = type;
		switch (type) {
		case EXTRA_LIFE:
			sprite = "extralife";
			break;
		case SPEED_BOOST:
			sprite = "speedboost";
			break;
		}
	}

	public int getType() {
		return type;
	}

	public String getAquiredMessage() {

		switch (type) {
		case EXTRA_LIFE:
			return "You got an extra life!";
		case SPEED_BOOST:
			return "You got a speed boost!";
		}

		return null;

	}

}

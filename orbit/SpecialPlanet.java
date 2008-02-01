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

public class SpecialPlanet extends Planet {

	private double mass;
	private boolean tagged;

	public SpecialPlanet() {
		super();
		tagged = false;
		numFrames=2;
	}

	public SpecialPlanet(Vector2 p,Vector2 v,Vector2 a,String sprite,double mass,double radius) {
		super(p,v,a,sprite,radius*2,radius*2);
		this.mass = mass;
		this.radius = radius;
		tagged = false;
		numFrames=2;
	}

	public void setTagged(){
		//System.out.println("\nTAGGED PLANET, YAY!!!!!\n");
		tagged = true;
		// set new image
		setFrame(1);
	}

	public boolean getTagged() {
		 return tagged;
	}

	public double getMass() {
		return mass;
	}

}

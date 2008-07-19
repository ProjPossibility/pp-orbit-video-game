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


/*
	This is the useful 2D vector struct. Everything's public
	to make things easy

*/

package orbit;

/**
 * Models a vectorized quantity.
 * @author Henry Yuen
 *
 */
public class Vector2 {

	public double x;
	public double y;

	public Vector2() {
		x = 0.0;
		y = 0.0;
	}

	public Vector2(double x,double y) {
		this.x = x;
		this.y = y;
	}

	public double getLength() {
		return Math.sqrt(x*x + y*y);
	}

	public Vector2 getNormalized() {
		double length = getLength();
		Vector2 v = new Vector2(x/length,y/length);
		return v;
	}

	public Vector2 addVector(Vector2 v) {
		Vector2 v2 = new Vector2(x + v.x,y+v.y);
		return v2;
	}

	public Vector2 subVector(Vector2 v) {
		return new Vector2(x - v.x,y - v.y);
	}

	public double dot(Vector2 v) {
		return x*v.x + y*v.y;
	}

	public Vector2 scale(double s) {
		return new Vector2(x*s,y*s);
	}
	
	public String toString()
	{
		return "<"+(int)x+", "+(int)y+">";
	}
}
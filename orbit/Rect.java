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
 * Our very own rectangle class. 
 */
public class Rect {
	
	//rectangle placement constants
	public double left;
	public double right;
	public double top;
	public double bottom;
	public double width;
	public double height;
	
	/**
	 * Default constructor (0,0,0,0)
	 */
	public Rect() {
		left = right = top = bottom = width = height = 0.0;
	}

	/**
	 * Actual useful constructor
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 */
	public Rect(double left,double top,double right,double bottom) {
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
		this.width = right-left;
		this.height = bottom-top;
	}
	
	/**
	 * Sets center of rectangle
	 * @param center The new  center
	 */
	public void setCenter(Vector2 center)
	{
		double x=center.x,y=center.y;
		left=x-width/2;
		right=x+width/2;
		top=y-height/2;
		bottom=y+height/2;
	}
	
	/**
	 * Debug function
	 */
	public String toString()
	{
		return "<"+(int)left+", "+(int)top+", "+(int)right+", "+(int)bottom+">";
	}
}
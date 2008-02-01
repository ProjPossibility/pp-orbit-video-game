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

import java.awt.geom.*;
import java.awt.*;

public class ScreenOverlay {

	Color color;
	int width,height;
	Rect area;
	Graphics2D graphics;

	public ScreenOverlay(Graphics2D g,Color c,int width,int height) {
		graphics = g;
		color = c;
		this.width = (int)width;
		this.height = (int)height;
		area=new Rect(0,0,width,height);
	}
	public ScreenOverlay(Graphics2D g,Color c,Rect area) {
		graphics = g;
		color = c;
		this.area=area;
		this.width=(int)area.width;
		this.height=(int)area.height;
	}
	public void paint(float alpha) {
		//Composite comp = graphics.getComposite();
		//graphics.setComposite(
		//		AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
		
		graphics.setColor(color);
		graphics.fillRect((int)area.left,(int)area.top,(int)area.width,(int)area.height);
		//graphics.setComposite(comp);
		System.out.println("Paint minimap "+area);
	}
	public void paint(Graphics2D g,float alpha) {
		//Composite comp = graphics.getComposite();
		//graphics.setComposite(
		//		AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));
		
		g.setColor(color);
		g.fillRect((int)area.left,(int)area.top,(int)area.width,(int)area.height);
		//graphics.setComposite(comp);
		System.out.println("Paint minimap "+area);
	}
	

}

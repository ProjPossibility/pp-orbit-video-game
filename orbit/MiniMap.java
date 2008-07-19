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

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;

/**
 * Class that models minimap in game. Also contains methods to d
 * draw minimap on screen.
 */
public class MiniMap
{
	/**
	 * The world associated with the minimap.
	 */ 
	private World world;
	
	/**
	 * The viewport associated with the minimap. 
	 */
	private Rect viewport;
	
	/**
	 * Dimensions of minimap.
	 */
	private Rect miniScreen;
	
	/*
	 * Dimensions of player view
	 */
	private Rect playerView;
	
	
	private ScreenOverlay overlay;
	
	/**
	 * Scale factor
	 */
	private double objectScale;
	
	/**
	 * Constructor 
	 * 
	 * @param screen The dimensions of the screen
	 * @param view The dimensions of the view
	 * @param realView The dimensions of the world view
	 * @param world The world object associated with the minimap
	 */
	public MiniMap(Rect screen,Rect view,Rect realView,World world)
	{
		this.world=world;
		this.viewport=view;
		this.miniScreen=screen;
		this.playerView=realView;
		objectScale=2.0;
	}
	
	/**
	 * Overriden paint method.
	 * @param g The graphics
	 */
	public void paintComponent(Graphics2D g)
	{
		Composite comp = g.getComposite();

		//draw a rectangle
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,.50f));

		g.setColor(Color.blue);
		g.fillRect((int)miniScreen.left,(int)miniScreen.top,(int)miniScreen.width,(int)miniScreen.height);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,.90f));
		try
		{	
			ArrayList<SpaceObject> objs=world.getSpaceObjects();
			synchronized(objs)
			{
				for(SpaceObject so:objs)
				{
					drawObject(g,so);
				}
			}
		}catch(ConcurrentModificationException e){}
		Vector2 corner0=worldToScreen(playerView.left,playerView.top),corner1=worldToScreen(playerView.right,playerView.top),
			corner3=worldToScreen(playerView.left,playerView.bottom),corner2=worldToScreen(playerView.right,playerView.bottom);
		
		g.setColor(Color.green);
		g.drawLine((int)corner0.x,(int)corner0.y,(int)corner1.x,(int)corner1.y);
		g.drawLine((int)corner2.x,(int)corner2.y,(int)corner1.x,(int)corner1.y);
		g.drawLine((int)corner2.x,(int)corner2.y,(int)corner3.x,(int)corner3.y);
		g.drawLine((int)corner0.x,(int)corner0.y,(int)corner3.x,(int)corner3.y);
		
		g.setComposite(comp);
	}
	
	/**
	 * Sets the viewpoint center point.
	 * @param pos  The new viewpoint center
	 */
	public void centerViewportAbout(Vector2 pos)
	{
		viewport.setCenter(pos);
	}
	
	/**
	 * Draws object on minimap
	 */
	private void drawObject(Graphics2D g,SpaceObject so)
	{
		g.setColor(Color.red);
		double diameter=so.getRadius()*0.06;
		if(so instanceof Spaceship)
		{
			g.setColor(Color.green);
			diameter=7;
		}
		if(so instanceof Asteroid)
		{
			g.setColor(Color.magenta);
			diameter=5;
		}
		if(so instanceof Powerup)
		{
			g.setColor(Color.green);
			diameter=5;
		}
		if(so instanceof SpecialPlanet)
		{
			g.setColor(Color.yellow);
			diameter=12;
		}
		Vector2 screenPos=worldToScreen(so.getPos());
		if(screenPos.x-diameter/2<miniScreen.left||screenPos.x+diameter/2>miniScreen.right)
			return;
		if(screenPos.y-diameter/2<miniScreen.top||screenPos.y+diameter/2>miniScreen.bottom)
			return;
		g.fillOval((int)(screenPos.x-diameter/2),(int)(screenPos.y-diameter/2),(int)diameter,(int)diameter);
	}
	
	/**
	 * Converts from a world position to a screen position.
	 * Coordinate scaling method.
	 * 
	 * @param pos The world pos
	 * @return The scaled screen pos
	 */
	private Vector2 worldToScreen(Vector2 pos)
	{
		return new Vector2(ScrollingScreen.transformSingleDimension(pos.x,viewport.left,viewport.right,miniScreen.left,miniScreen.right),
			ScrollingScreen.transformSingleDimension(pos.y,viewport.top,viewport.bottom,miniScreen.top,miniScreen.bottom));
	}
	
	/**
	 * Another scaling method
	 * @param x The x world coordinate
	 * @param y The y world coordinate
	 * @return The screen coordinate
	 */
	private Vector2 worldToScreen(double x,double y)
	{
		return worldToScreen(new Vector2(x,y));
	}
}
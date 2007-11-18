package orbit;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class MiniMap
{
	private World world;
	private Rect viewport;
	private Rect miniScreen;
	private Rect playerView;
	private ScreenOverlay overlay;
	private double objectScale;
	
	public MiniMap(Rect screen,Rect view,Rect realView,World world)
	{
		this.world=world;
		this.viewport=view;
		this.miniScreen=screen;
		this.playerView=realView;
		objectScale=2.0;
	}
	public void paintComponent(Graphics2D g)
	{
		Composite comp = g.getComposite();

		//draw a rectangle
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,.50f));

		g.setColor(Color.blue);
		g.fillRect((int)miniScreen.left,(int)miniScreen.top,(int)miniScreen.width,(int)miniScreen.height);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,.90f));
		ArrayList<SpaceObject> objs=world.getSpaceObjects();
		synchronized(objs)
		{
			for(SpaceObject so:objs)
			{
				drawObject(g,so);
			}
		}
		
		g.setComposite(comp);
	}
	public void centerViewportAbout(Vector2 pos)
	{
		viewport.setCenter(pos);
	}
	private void drawObject(Graphics2D g,SpaceObject so)
	{
		g.setColor(Color.red);
		double diameter=2;
		if(so instanceof Spaceship)
		{
			g.setColor(Color.green);
			diameter=4;
		}
		Vector2 screenPos=worldToScreen(so.getPos());
		if(screenPos.x+diameter/2<miniScreen.left||screenPos.x-diameter/2>miniScreen.right)
			return;
		if(screenPos.y+diameter/2<miniScreen.top||screenPos.y-diameter/2>miniScreen.bottom)
			return;
		g.fillRect((int)(screenPos.x-diameter/2),(int)(screenPos.y-diameter/2),(int)diameter,(int)diameter);
	}
	private Vector2 worldToScreen(Vector2 pos)
	{
		return new Vector2(ScrollingScreen.transformSingleDimension(pos.x,viewport.left,viewport.right,miniScreen.left,miniScreen.right),
			ScrollingScreen.transformSingleDimension(pos.y,viewport.top,viewport.bottom,miniScreen.top,miniScreen.bottom));
	}
}
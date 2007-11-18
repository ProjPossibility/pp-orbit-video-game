package orbit;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;

public class MiniMap
{
	private World world;
	private Rect viewport;
	private Rect miniScreen;
	private ScreenOverlay overlay;
	
	public MiniMap(Rect screen,Rect view,World world)
	{
		this.world=world;
		this.viewport=view;
		this.miniScreen=screen;
	}
	public void paintComponent(Graphics2D g)
	{
		if(overlay==null)
			overlay=new ScreenOverlay(g,Color.blue,miniScreen);
		
		overlay.paint(g,0.3f);
	}
}
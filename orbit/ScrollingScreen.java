package orbit;

import javax.swing.*;
import java.awt.*;

public class ScrollingScreen extends JPanel
{
	private Rect screen;//in pixels of screen space
	private Rect viewport;//in world space
	private World world;
	
	/**	Make a rendering scroll screen.
	 *	@param screen The Rect representing the actual screen's width,height
	 *	@param view The Rect representing the portal through which the player sees the world
	 *	@param world The world to draw
	 **/
	public ScrollingScreen(Rect screen,Rect view,World world)
	{
		this.screen=screen;
		this.viewport=view;
		this.world=world;
		setPreferredSize(new Dimension((int)screen.width,(int)screen.height));
	}
	public void paintComponent(Graphics g1)
	{
		super.paintComponent(g1);
		System.out.println("Drawing everything");
		Graphics2D g=(Graphics2D)g1;
		g.setColor(Color.black);
		g.fillRect(0,0,(int)screen.width,(int)screen.height);
		for(SpaceObject so:world.getSpaceObjects())
			drawSpaceObject(g,so);
	}
	private void drawSpaceObject(Graphics2D g,SpaceObject so)
	{
		System.out.println("Drawing object");
		Vector2 pos=so.getPos();
		double radius=so.getRadius();
		Vector2 screenPos=transformVector(pos);
		Vector2 screenScale=transformScale(new Vector2(so.getWidth(),so.getHeight()));
		//cull the image if its out of range
		if(screenPos.y+screenScale.y<0||screenPos.y-screenScale.y>screen.height)
			return;
		if(screenPos.x+screenScale.x<0||screenPos.x-screenScale.x>screen.width)
			return;
		Image image=so.getFrame();
		if(image==null)
			return;
		System.out.println("Drawing object - in view, has image");
		g.setColor(Color.blue);
		//g.fillOval((int)(screenPos.x-screenScale.x/2),(int)(screenPos.y-screenScale.y/2),(int)screenScale.x,(int)screenScale.y);
		g.drawImage(image,(int)(screenPos.x-screenScale.x/2),(int)(screenPos.y-screenScale.y/2),(int)screenScale.x,(int)screenScale.y,null);
	}
	
	public Vector2 transformScale(Vector2 vec)
	{
		return new Vector2(vec.x*screen.width/viewport.width,vec.y*screen.height/viewport.height);
	}
	
	/** given a vector2 in world space, transform it to screen coordinates
	 * @param vec The Vector2 of an object in world space
	 *	@return The Vector2 in screen space
	 **/
	public Vector2 transformVector(Vector2 vec)
	{
		return new Vector2(transformSingleDimension(vec.x,viewport.left,viewport.right,viewport.width),
			transformSingleDimension(vec.y,viewport.top,viewport.bottom,viewport.height));
	}
	/** transform a single dimension based on viewport bounds and screen size
	 *
	 **/
	private double transformSingleDimension(double value,double viewRangeFrom,double viewRangeTo,double screenLength)
	{
		return (value-viewRangeFrom)*(screenLength)/(viewRangeTo-viewRangeFrom);
	}
	
}
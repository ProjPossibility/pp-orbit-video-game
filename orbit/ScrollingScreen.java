package orbit;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.awt.geom.*;

public class ScrollingScreen extends JPanel implements MouseListener, KeyListener
{
	private Rect screen;//in pixels of screen space
	private Rect viewport;//in world space
	private World world;
	private BinaryInput binaryInput;
	private MiniMap miniMap;
	private Game game;

	/**	Make a rendering scroll screen.
	 *	@param screen The Rect representing the actual screen's width,height
	 *	@param view The Rect representing the portal through which the player sees the world
	 *	@param world The world to draw
	 **/
	public ScrollingScreen(Game game,Rect screen,Rect view,World world)
	{
		this.game=game;
		this.screen=screen;
		this.viewport=view;
		this.world=world;
		setPreferredSize(new Dimension((int)screen.width,(int)screen.height));
		Rect miniScreen=new Rect(screen.right-screen.width/5,screen.top,screen.right,screen.height/5+screen.top);
		miniMap=new MiniMap(miniScreen,new Rect(0,0,8000,6000),view,world);
		addMouseListener(this);
		addKeyListener(this);
	}
	public void setBinaryInput(BinaryInput binIn)
	{
		binaryInput=binIn;
	}
	/** Override the paint
	 *
	 **/
	public void paintComponent(Graphics g1)
	{
		super.paintComponent(g1);
		//System.out.println("Drawing everything");
		Graphics2D g=(Graphics2D)g1;
		PrintManager.getInstance().setGraphics(g);
		g.setColor(Color.black);
		g.fillRect(0,0,(int)screen.width,(int)screen.height);
		
		try
		{
			for(ArrayList<Star> starfield: world.getStarfield().getStarLayers())
				for(Star star:starfield)
					drawStarfield(g,star);
		}catch(ConcurrentModificationException e){}
		try
		{
			ArrayList<ParticleEffect> parts=world.getParticleSystem().getParticles();
			synchronized(parts)
			{
				for(SpaceObject so:parts)
					drawSpaceObject(g,so);
			}
		}
		catch(ConcurrentModificationException e){}
		try
		{
			ArrayList<SpaceObject> objs=world.getSpaceObjects();
			synchronized(objs)
			{
				for(SpaceObject so:objs)
					drawSpaceObject(g,so);
			}
		}catch(ConcurrentModificationException e){}

		try
		{
			ArrayList<Explosion> expls=world.getExplosions();
			synchronized(expls)
			{
				for(SpaceObject so:expls)
					drawSpaceObject(g,so);
			}
		}catch(ConcurrentModificationException e){}

		miniMap.centerViewportAbout(world.getSpaceship().getPos());
		miniMap.paintComponent(g);

		//print the headsup display
		drawHUD(g);

	}
	/**
	 * draws the headsup display
	 */
	private void drawHUD(Graphics2D graphics) {

		Composite comp = graphics.getComposite();

		Spaceship ship = world.getSpaceship();

		//draw a rectangle
		graphics.setComposite(
				AlphaComposite.getInstance(AlphaComposite.SRC_OVER,.50f));

		graphics.setColor(Color.RED);
		int width = (int)(800*ship.getHealth()/100.0);

		graphics.fillRect(0,580,width,20);
		graphics.setComposite(comp);

		if (game.getState() == Game.DIED_SEQUENCE) {
			PrintManager.getInstance().print("huge","HAHAHA",400,300,Color.RED,PrintManager.CENTER);

			PrintManager.getInstance().print("medium","Press button to restart",400,500,Color.WHITE,PrintManager.CENTER);
		}
	}

	/** Draw an individual SpaceObject.
	 *
	 **/
	private void drawSpaceObject(Graphics2D g,SpaceObject so)
	{
		//System.out.println("Drawing object");
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

		if(so instanceof Spaceship)
		{
			//g.setColor(Color.blue);
			//g.fillOval((int)(screenPos.x-screenScale.x/2),(int)(screenPos.y-screenScale.y/2),(int)screenScale.x,(int)screenScale.y);
			AffineTransform transform=AffineTransform.getScaleInstance(screenScale.x,screenScale.y);
			transform=AffineTransform.getScaleInstance(1,1);
			transform=AffineTransform.getTranslateInstance(screenPos.x-screenScale.x*.455,screenPos.y-screenScale.y*.255);
			transform.concatenate(AffineTransform.getRotateInstance(((Spaceship)so).getAngle()+Math.PI/2,screenScale.x/2,screenScale.y/4));
			transform.concatenate(AffineTransform.getScaleInstance(screenScale.x/image.getWidth(null),screenScale.y/image.getHeight(null)));

			g.drawImage(image,transform,null);
		}
		else if(so instanceof ParticleEffect)
		{
			g.drawImage(image,(int)(screenPos.x-screenScale.x/2),(int)(screenPos.y-screenScale.y/2),(int)screenScale.x,(int)screenScale.y,null);
		}
		else
		{
			g.drawImage(image,(int)(screenPos.x-screenScale.x/2),(int)(screenPos.y-screenScale.y/2),(int)screenScale.x,(int)screenScale.y,null);
		}
	}
	private void drawStarfield(Graphics2D g,Star so)
	{
		Image image=so.getFrame();
		if(image==null)
			return;
		Vector2 pos=so.getPos();
		Vector2 screenScale=transformScale(new Vector2(so.getWidth(),so.getHeight()));
		g.drawImage(image,(int)(pos.x*screen.width-screenScale.x/2),
			(int)(pos.y*screen.height-screenScale.y/2),
			(int)screenScale.x,(int)screenScale.y,null);

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
		return new Vector2(transformSingleDimension(vec.x,viewport.left,viewport.right,screen.width),
			transformSingleDimension(vec.y,viewport.top,viewport.bottom,screen.height));
	}
	/** transform a single dimension based on viewport bounds and screen size
	 *
	 **/
	private double transformSingleDimension(double value,double viewRangeFrom,double viewRangeTo,double screenLength)
	{
		return (value-viewRangeFrom)*(screenLength)/(viewRangeTo-viewRangeFrom);
	}
	public static double transformSingleDimension(double value,double viewRangeFrom,double viewRangeTo,double screenLeft,double screenRight)
	{
		return (value-viewRangeFrom)*(screenRight-screenLeft)/(viewRangeTo-viewRangeFrom)+screenLeft;
	}

	///Whenever an event happens, update the binary input

	public void mousePressed(MouseEvent e)
	{
		if(binaryInput!=null)
			binaryInput.buttonChanged(true);
		requestFocus();
	}
	public void mouseEntered(MouseEvent e){}
	public void mouseReleased(MouseEvent e)
	{
		if(binaryInput!=null)
			binaryInput.buttonChanged(false);
	}
	public void mouseClicked(MouseEvent e){}
	public void mouseExited(MouseEvent e)
	{
		if(binaryInput!=null)
			binaryInput.buttonChanged(false);
	}

	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode()==27)///27=esc, 32=space
			System.exit(0);

		if(binaryInput!=null)
			binaryInput.buttonChanged(true);
	}
	public void keyReleased(KeyEvent e)
	{
		if(binaryInput!=null)
			binaryInput.buttonChanged(false);
	}
	public void keyTyped(KeyEvent e){}

}
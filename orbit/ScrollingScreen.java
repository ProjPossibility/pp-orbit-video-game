package orbit;

import javax.swing.*;

public class ScrollingScreen extends JPanel
{
	private Rect screen;//in pixels of screen space
	private Rect viewport;//in world space
	
	/**	Make a rendering scroll screen.
	 *	@param screen The Rect representing the actual screen's width,height
	 *	@param view The Rect representing the portal through which the player sees the world
	 **/
	public ScrollingScreen(Rect screen,Rect view)
	{
		this.screen=screen;
		this.viewport=view;
	}
	
	
	
	/** given a vector2 in world space, transform it to screen coordinates
	 * @param vec The Vector2 of an object in world space
	 *	@return The Vector2 in screen space
	 **/
	public Vector2 transform(Vector2 vec)
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
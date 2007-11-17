package orbit;

public class ScrollingScreen
{
	private Rect screen;//in pixels of screen space
	private Rect viewport;//in world space
	
	//given a vector2 in world space, transform it to screen coordinates
	public Vector2 transform(Vector2 vec)
	{
		return new Vector2(transformSingleDimension(vec.x,viewport.left,viewport.right,viewport.width),
			transformSingleDimension(vec.y,viewport.top,viewport.bottom,viewport.height));
	}
	//transform a single dimension based on viewport bounds and screen size
	private float transformSingleDimension(float value,float viewRangeFrom,float viewRangeTo,float screenLength)
	{
		return (value-viewRangeFrom)*(screenLength)/(viewRangeTo-viewRangeFrom);
	}
	
}
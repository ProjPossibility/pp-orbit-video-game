public class Rect {
	
	public double left;
	public double right;
	public double top;
	public double bottom;
	public double width;
	public double height;
	
	public Rect() { 
		left = right = top = bottom = width = height = 0.0;
	}
	
	public Rect(double left,double right,double top,double bottom) {
		this.left = left;
		this.right = right;
		this.top = top;
		this.bottom = bottom;
		this.width = right-left;
		this.height = bottom-top;	
	}
	
	
}
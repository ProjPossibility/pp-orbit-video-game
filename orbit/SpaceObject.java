/*
	name: SpaceObject
	description: This is the space object that is the base object for all the 
		objects that reside in the world object there are a lot of object names
		in this object.

*/

public class SpaceObject {
	
	//for mechanics
	public Vector pos;
	public Vector vel;
	public Vector accel;
	
	public double mass;
	
	public String sprite;
	
	//bounds
	public Rect bounds;
	
	public SpaceObject() {
		pos = new Vector();
		vel = new Vector();
		accel = new Vector();
		sprite = new String("null");
		mass = 0.0;
		bounds = new Rect();
	}
	
	public SpaceObject(Vector p,Vector v,Vector a,String sprite,Rect bounds,double mass) {
		pos = p;
		vel = v;
		accel = a;
		this.sprite = sprite;
		this.bounds = bounds;
	}
	
	
	
}



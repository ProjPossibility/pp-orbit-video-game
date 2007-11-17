/*
	name: SpaceObject
	description: This is the space object that is the base object for all the
		objects that reside in the world object there are a lot of object names
		in this object.

*/

package orbit;

public class SpaceObject {

	//for mechanics
	public Vector2 pos;
	public Vector2 vel;
	public Vector2 accel;

	public double mass;

	public String sprite;

	//bounds
	public Rect bounds;

	public SpaceObject() {
		pos = new Vector2();
		vel = new Vector2();
		accel = new Vector2();
		sprite = new String("null");
		mass = 0.0;
		bounds = new Rect();
	}

	public SpaceObject(Vector2 p,Vector2 v,Vector2 a,String sprite,Rect bounds,double mass) {
		pos = p;
		vel = v;
		accel = a;
		this.sprite = sprite;
		this.bounds = bounds;
	}



}



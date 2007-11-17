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

	public double width;
	public double height;

	public double radius;

	public SpaceObject() {
		pos = new Vector2();
		vel = new Vector2();
		accel = new Vector2();
		sprite = new String("null");
		mass = 0.0;
		width = height = radius = 0.0;
	}

	public SpaceObject(Vector2 p,Vector2 v,Vector2 a,String sprite,double width,double height,double mass) {
		pos = p;
		vel = v;
		accel = a;
		this.sprite = sprite;
		this.width = width;
		this.height = height;
		this.radius = Math.sqrt(width*width + height*height)/2.0;
	}



}



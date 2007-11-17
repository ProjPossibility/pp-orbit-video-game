package orbit;

public class Planet extends SpaceObject {

	public double radius;

	public Planet() {
		super();
	}

	public Planet(Vector2 p,Vector2 v,Vector2 a,String sprite,Rect bounds,double mass,double radius) {
		super(p,v,a,sprite,bounds,mass);
		this.radius = radius;
	}






}
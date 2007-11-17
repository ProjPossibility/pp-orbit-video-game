package orbit;

public class Planet extends SpaceObject {

	public double radius;

	public Planet() {
		super();
	}

	public Planet(Vector2 p,Vector2 v,Vector2 a,String sprite,double mass,double radius) {
		super(p,v,a,sprite,radius*2,radius*2,mass);
		this.radius = radius;
	}






}
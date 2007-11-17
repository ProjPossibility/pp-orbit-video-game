public class Planet extends SpaceObject {
	
	public double radius;
	
	public Planet() {
		this.super();
	}
	
	public Planet(Vector p,Vector v,Vector a,String sprite,Rect bounds,double mass,double radius) {
		this.super(p,v,a,sprite,bounds,mass);
		this.radius = radius;
	}
	
	
	
	
	
	
}
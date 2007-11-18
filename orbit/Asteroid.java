package orbit;

public class Asteroid extends Planet {
		
	private double mass;
	
	public Asteroid(Vector2 pos, Vector2 vel, Vector2 acc, String sprite, double mass, double radius) {
		super(pos,vel,acc,sprite,radius*2,radius*2);
		this.mass = mass;
		this.radius = radius;
		alive = true;
		setAnimationProperties(20, 25, true);
	}

	public double getMass() {
		return mass;
	}
}

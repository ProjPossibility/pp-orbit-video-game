package orbit;

import java.util.*;

/**
 * The spaceship object. The protagonist of the story. The hero of the game.
 * Awesomage.
 * @author Henry Yuen
 *
 */
public class Spaceship extends SpaceObject {

	private boolean thrusting;
	private static final double thrustSize = 30;

	public Spaceship() {
		thrusting = false;
	}
	public Spaceship(Vector2 p,Vector2 v,Vector2 a,String sprite,double width,double height) {
		super(p,v,a,sprite,width,height);
	}

	/**
	 * This determines the angle at which the angle is pointed, based
	 * on its velocity vector.
	 * @return angle at which the spaceship is pointed
	 */
	public double getAngle() {
		return Math.atan2(vel.y, vel.x);
	}

	/**
	 * Interacts the spaceship with the list of planets that the main game loop
	 * provides (these are the planets within a certain range).
	 * @param planets
	 */
	public void interact(Planet p) {

		//do the gravity calculations
		//get the distance
		Vector2 R = p.getPos().subVector(pos);
		double dist = R.getLength();
		R = R.getNormalized();
		Vector2 v;
		double radius = p.getRadius();
		if (dist > radius) {
			v = R.scale(p.getMass()/(dist*dist));
		} else {
			v = R.scale(p.getMass() * dist/(radius*radius*radius));
		}

		accel = accel.addVector(v);

	}

	/**
	 * This actually moves the spaceship. If the spaceship is thrusting
	 * there will be an extra large acceleration in the direction that the
	 * spaceship is moving.
	 */
	public void update(long timeElapsed) {
		Vector2 a = accel;

		if (thrusting) {
			double angle = getAngle();
			Vector2 thrust = new Vector2(Math.cos(angle),Math.sin(angle));
			thrust = thrust.scale(thrustSize);

			a=a.addVector(thrust);
		}

		vel = vel.addVector(a.scale((double)timeElapsed*0.007));
		System.out.println("Velocity: "+vel+" Position: "+pos);
		pos = pos.addVector(vel.scale((double)timeElapsed*0.001));
		accel=new Vector2();
	}

	public void setThrusting(boolean t) {
		thrusting = t;
	}



}

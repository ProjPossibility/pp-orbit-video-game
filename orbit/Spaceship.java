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

	public Spaceship() {
		thrusting = false;
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
	public void interact(List<Planet> planets) {

		//do the gravity calculations
		for (Planet p : planets) {
			//get the distance
			Vector2 R = p.getPos().subVector(pos);
			double dist = R.getLength();
			R = R.getNormalized();
			Vector2 v = R.scale(p.getMass()/(dist*dist));

			accel = accel.addVector(v);
		}

	}

	/**
	 * This actually moves the spaceship. If the spaceship is thrusting
	 * there will be an extra large acceleration in the direction that the
	 * spaceship is moving.
	 */
	public void move(long timeElapsed) {
		Vector2 a = accel;

		if (thrusting) {
			double angle = getAngle();
			Vector2 thrust = new Vector2(Math.cos(angle),Math.sin(angle));
		}

		vel = vel.addVector(accel.scale((double)timeElapsed));
		pos = pos.addVector(vel.scale((double)timeElapsed));
	}

	public void setThrusting(boolean t) {
		thrusting = t;
	}



}

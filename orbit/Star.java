package orbit;

public class Star extends SpaceObject {

	public Star(int layer) {
		super();

		//randomly generate its position
		pos.x = Math.random();
		pos.y = Math.random();

		vel.x = 0; //there's no horizontal speed

		sprite = "star";

		//based on the layer, calculate the speed at which it should be going
		switch (layer) {
		case 0: {	//this is the stationary layer
			vel.y = 0;
			width = height = 2;
			break;
		}
		case 1: {
			vel.y = 0.01;
			width = height = 8;
			break;
		}
		case 2: {
			vel.y = 0.02;
			width = height = 16;
			break;
		}
		}
	}


	/**
	 * This is the update method for the stars. Merely checks if it went off the screen,
	 * after adding the velocity to the position
	 */
	public void update(long timeElapsed) {
		pos.x += vel.x*timeElapsed;
		pos.y += vel.y*timeElapsed;

		//update its position
		if (vel.x < 0) {
			vel.x = 1.0;
		}

		if (vel.y < 0) {
			vel.y = 1.0;
		}

		if (vel.x > 1.0) vel.x = 0;
		if (vel.y > 1.0) vel.y = 0;
	}




}
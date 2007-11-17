package orbit;

public class Star extends SpaceObject {
	private int layer;

	public Star(int layer) {
		super();
		this.layer = layer;

		vel.x = 0; //there's no horizontal speed

		//based on the layer, calculate the speed at which it should be going
		switch (layer) {
		case 0: {	//this is the stationary layer
			vel.y = 0;
			break;
		}
		case 1: {
			vel.y = 1;
			break;
		}
		case 2: {
			vel.y = 2;
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
			vel.x = 800;
		}

		if (vel.y < 0) {
			vel.y = 600;
		}

		if (vel.x > 800) vel.x = 0;
		if (vel.y > 600) vel.y = 0;
	}




}
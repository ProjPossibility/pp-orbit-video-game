package orbit;

import java.util.ArrayList;

public class World
{
	private ArrayList<SpaceObject> spaceObjects;
	private Starfield starfield;
	private Spaceship spaceship;

	public World()
	{
		spaceObjects=new ArrayList<SpaceObject>();
		//create the starfield
		starfield = new Starfield();
		//create the spaceship
		spaceship = new Spaceship();
	}
	public ArrayList<SpaceObject> getSpaceObjects()
	{
		return spaceObjects;
	}

	public Starfield getStarfield() {
		return starfield;
	}

	/**
	 * main game loop. This updates the physics of the objects
	 */
	public void update(long timeElapsed) {

		//update the starfield
		starfield.update(timeElapsed);

		for (SpaceObject obj : spaceObjects) {
			obj.update(timeElapsed);
		}

		//interact the spaceship with all the planets
		for (SpaceObject obj : spaceObjects) {
			if (obj instanceof Planet) {
				//check if the planet is within range
				Planet p = (Planet) obj;
				double dist = p.getPos().subVector(spaceship.getPos()).getLength();
				if (dist < 800*2) {
					spaceship.interact(p);

					//see if they collide
					if (dist < p.getRadius()) {
						//collision!

					}
				}
			}

		}

	}

}
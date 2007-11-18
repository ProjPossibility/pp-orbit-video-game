package orbit;

import java.util.ArrayList;

public class World
{
	public final int WORLD_SIZE = 6000;

	private ArrayList<SpaceObject> spaceObjects;
	private ArrayList<SpaceObject> deadObjects;
	private Starfield starfield;
	private Spaceship spaceship;

	private Game game;

	public World(Game g)
	{
		game = g;
		spaceObjects=new ArrayList<SpaceObject>();
		deadObjects = new ArrayList<SpaceObject>();
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
						//splode!

					}
				}
			}

		} //end for

		//go through the garbage can
		for (SpaceObject obj : deadObjects) {
			spaceObjects.remove(obj);
		}

		if (deadObjects.size() > 0)
			deadObjects.clear();

	}

}
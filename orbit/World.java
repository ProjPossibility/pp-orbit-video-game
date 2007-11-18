package orbit;

import java.util.*;

public class World
{
	public final int WORLD_SIZE = 6000;

	private ArrayList<SpaceObject> spaceObjects;
	private ArrayList<SpaceObject> deadObjects;
	private Starfield starfield;
	private Spaceship spaceship;
	private BinaryInput binaryInput;
	private Rect viewport;

	private Game game;

	public World(Game g)
	{
		game = g;
		spaceObjects=new ArrayList<SpaceObject>();
		deadObjects = new ArrayList<SpaceObject>();
		//create the starfield
		starfield = new Starfield();
		//create the spaceship
		//spaceship = new Spaceship();
	}
	public void setSpaceship(Spaceship ship)
	{
		spaceship=ship;
	}
	public void setViewport(Rect view)
	{
		viewport=view;
	}
	public void setBinaryInput(BinaryInput binIn)
	{
		binaryInput=binIn;
	}
	public ArrayList<SpaceObject> getSpaceObjects()
	{
		return spaceObjects;
	}
	/** Add a SpaceObject to the list
	 *
	 **/
	public void add(SpaceObject so)
	{
		spaceObjects.add(so);
	}

	public Starfield getStarfield() {
		return starfield;
	}

	/**
	 * main game loop. This updates the physics of the objects
	 */
	public void update(long timeElapsed) {

		//update the starfield
		starfield.update(timeElapsed,spaceship.getAngle());

		//apply thrusters
		if(spaceship!=null)
		{
			spaceship.setThrusting(binaryInput.getButtonState()==1);
			
			//System.out.println(spaceship.getPos()+" , "+viewport);
		}

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
		
		if(viewport!=null)
			viewport.setCenter(spaceship.getPos());

		//go through the garbage can
		for (SpaceObject obj : deadObjects) {
			spaceObjects.remove(obj);
		}

		if (deadObjects.size() > 0)
			deadObjects.clear();

	}
	/** Populates the world with planets and spaceship based on difficulty.
	 *
	 **/
	public void populate(int level)
	{
		//create the main ship, and add it
		Spaceship ship=new Spaceship(new Vector2(200,300),new Vector2(0,0),new Vector2(0,0),"spaceship",50,50);
		//set the ship
		setSpaceship(ship);
		add(ship);

		Random rand=new Random();
		for(int i=0;i<4;i++)
		{
			int size=rand.nextInt(2)+1;
			SpaceObject so=new Planet(new Vector2(rand.nextInt(1500)-750,rand.nextInt(1500)-750),
				new Vector2(0,0),new Vector2(0,0),"planet"+size,600,50+size*2);
			add(so);
		}
	}

}
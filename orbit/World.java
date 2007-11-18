package orbit;

import java.util.*;

public class World
{
	public final int WORLD_SIZE = 24000;
	public final double MAX_SHIP_SPEED = 2500;
	public final int NUM_ASTEROIDS_IN_UNIVERSE = 20;

	public static final int SMALL_PLANET = 0;
	public static final int MEDIUM_PLANET = 1;
	public static final int BIG_PLANET = 2;

	private ArrayList<SpaceObject> spaceObjects;
	private ArrayList<SpaceObject> deadObjects;
	private ArrayList<Explosion> explosions;
	private int numAsteroids;
	private Starfield starfield;
	private Spaceship spaceship;
	private BinaryInput binaryInput;
	private Rect viewport;
	private String explosionSprite;
	private ParticleSystem particleSystem;
	private Game game;

	public World(Game g)
	{
		game = g;
		spaceObjects=new ArrayList<SpaceObject>();
		deadObjects = new ArrayList<SpaceObject>();
		explosions = new ArrayList<Explosion>();
		//create the starfield
		starfield = new Starfield();
		particleSystem=new ParticleSystem();
		//create the spaceship
		//spaceship = new Spaceship();

	}
	public ParticleSystem getParticleSystem()
	{
		return particleSystem;
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

	public ArrayList<Explosion> getExplosions() {
		return explosions;
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
		starfield.update(timeElapsed,spaceship.getVel());

		//apply thrusters
		if(spaceship!=null&&game.getState()==game.GAME)
		{
			spaceship.setThrusting(binaryInput.getButtonState()==1);

			//System.out.println(spaceship.getPos());
		}

		for (SpaceObject obj : spaceObjects) {


			if (obj instanceof Planet) {
				//check if the planet is within range
				Planet p = (Planet) obj;
				Vector2 pos = spaceship.getPos();
				double dist = p.getPos().subVector(pos).getLength();

				if (dist < 10000) {
					spaceship.interact(p);
					//System.out.println(dist + "," + p.getRadius());
					//System.out.println(dist);
					//see if they collide
					if (dist < p.getRadius()) {
						//collision!
						//splode!
						if(explosions.size()==0) {
							System.out.println("EXPLOSION SHOULD OCCUR");
							Explosion e = new Explosion(spaceship.getPos(), "explosion", p.getWidth(), p.getHeight());
							explosions.add(e);
						}
					}
				}
			}

			/*
			if (obj instanceof Spaceship) {
				Vector2 accel = spaceship.predictAccel();
				Vector2 vel = spaceship.predictVel(timeElapsed, accel);
				Vector2 pos = spaceship.predictPos(timeElapsed, vel);

				if (pos.x < 0) {
					vel.x = -vel.x;
					accel.x = -accel.x;
				}
				if (pos.y < 0){
					vel.y = -vel.y;
					accel.y = -accel.y;
				}
				if (pos.x >= WORLD_SIZE) {
					vel.x = -vel.x;
					accel.x = -accel.x;
				}
				if (pos.y >= WORLD_SIZE) {
					vel.y = -vel.y;
					accel.y = -accel.y;
				}

				spaceship.setVel(vel);
				spaceship.setAccel(accel);
			}
			*/

			obj.update(timeElapsed);

		}

		//maximum speed for spaceship
		Vector2 v = spaceship.getVel();
		if (v.getLength() > MAX_SHIP_SPEED) {
			v = v.getNormalized().scale(MAX_SHIP_SPEED);
			spaceship.setVel(v);
		}

		if(viewport!=null)
			viewport.setCenter(spaceship.getPos());

		for(Explosion e1 : explosions) {
			System.out.println(e1);
			if(e1.getAlive()) {
				e1.animate((int)timeElapsed);
				System.out.println("STEP");
			}
			else {
				deadObjects.add(e1);
			}
		}

		//go through the garbage can
		for (SpaceObject obj : deadObjects) {
			spaceObjects.remove(obj);
			if(obj instanceof Explosion)
				explosions.remove(obj);
		}

		if (deadObjects.size() > 0)
			deadObjects.clear();


	}
	/** Populates the world with planets and spaceship based on difficulty.
	 *
	 **/
	public void populate(int level)
	{
		//clear everything in the spaceObjects
		spaceObjects.clear();

		//create the main ship, and add it
		Spaceship ship=new Spaceship(new Vector2(750,750),new Vector2(0,0),new Vector2(0,0),"spaceship",50,135);
		//set the ship
		setSpaceship(ship);
		add(ship);

		Random rand=new Random();
		rand.setSeed(game.getLevelSeed());

		int numPlanets = 300+(level*20);

		for(int i=0;i<numPlanets;i++)
		{			
			int type = rand.nextInt(3);
			int size = 0;
			int mass = 0;

			switch (type) {
			case SMALL_PLANET: {
				size = 50;
				mass = 8000;
				break;
			}
			case MEDIUM_PLANET: {
				size = 100;
				mass = 10000;
				break;
			}
			case BIG_PLANET: {
				mass = 15000;
				size = 200;
				break;
			}
			}

			int loopCount = 0;
			int half_world = WORLD_SIZE/2;
			int maxLoops = 10;

			Vector2 r = new Vector2(-half_world+rand.nextInt(WORLD_SIZE),-half_world+rand.nextInt(WORLD_SIZE));

			while (true) {
				if (loopCount > maxLoops) {
					break;
				}
				++loopCount;

				r = new Vector2(-half_world+rand.nextInt(WORLD_SIZE),-half_world+rand.nextInt(WORLD_SIZE));

				for (SpaceObject o : spaceObjects) {
					if (o instanceof Planet) {
						Vector2 d = o.getPos().subVector(r);
						if (d.getLength() < size + o.getRadius()) {
							continue;
						}
					}
				}
			}

			System.out.println(r);
			SpaceObject so=new Planet(r,new Vector2(0,0),new Vector2(0,0),"planet"+rand.nextInt(3),mass,size);
			add(so);
		}
		
/*		for(int x=0; x < NUM_ASTEROIDS_IN_UNIVERSE; x++) {
			Asteroid a = new Asteroid(new Vector2(rand.nextInt))
		}*/
		
	}
}
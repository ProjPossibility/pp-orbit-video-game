package orbit;

import java.util.*;

public class World
{
	public final int WORLD_SIZE = 24000;
	public final double MAX_SHIP_SPEED = 2500;
	public final int NUM_ASTEROIDS_IN_UNIVERSE = 20;
	public final int MAX_SPEED_ASTEROID = 1000;
	public final int ASTEROID_MASS = 2000;

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
	private int particleTimer;
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
		particleTimer=0;
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
			if(particleTimer<=0&&spaceship.isThrusting())
			{
				particleSystem.addParticle(spaceship.getPos(),spaceship.getVel().scale(-0.2),"smoke",40);
				particleTimer=30;
			}
			else
				particleTimer-=(int)timeElapsed;
			//System.out.println(spaceship.getPos());
		}
		if(particleSystem!=null)
			particleSystem.update((int)timeElapsed);

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
							Explosion e = new Explosion(spaceship.getPos(), "explosion", spaceship.getWidth(), spaceship.getHeight());
							explosions.add(e);
					}
				}
			}

			if (obj instanceof Spaceship) {
				Vector2 accel = spaceship.predictAccel();
				Vector2 vel = spaceship.predictVel(timeElapsed, accel);
				Vector2 pos = spaceship.predictPos(timeElapsed, vel);

				Vector2 r = new Vector2(-pos.x,-pos.y);

				double blackhole = 100*r.getLength()/(WORLD_SIZE);

				//magnitude of the acceleration
				r = r.getNormalized();
				r = r.scale(blackhole);

				if (pos.x < -WORLD_SIZE/2) {
					accel = r;
				}
				if (pos.y < -WORLD_SIZE/2){
					accel = r;
				}
				if (pos.x >= WORLD_SIZE/2) {
					accel = r;
				}
				if (pos.y >= WORLD_SIZE/2) {
					accel = r;
				}

				spaceship.setAccel(accel);
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
//			System.out.println(e1);
			if(e1.getAlive()) {
				e1.animate((int)timeElapsed);
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

		for(int x=0; x < NUM_ASTEROIDS_IN_UNIVERSE; x++) {
			double randomTheta = rand.nextDouble()*6.18;
			double velX = MAX_SPEED_ASTEROID * Math.cos(randomTheta);
			double velY = MAX_SPEED_ASTEROID * Math.sin(randomTheta);
			Asteroid a = new Asteroid(new Vector2(rand.nextInt(WORLD_SIZE), rand.nextInt(WORLD_SIZE)), new Vector2(velX, velY), new Vector2(0,0), "asteroid", 64, ASTEROID_MASS);
			spaceObjects.add(a);
		}
	}
}
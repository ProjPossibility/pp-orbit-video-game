package orbit;

import java.util.*;

public class World
{
	public final int WORLD_SIZE = 36000;
	public final double MAX_SHIP_SPEED = 2500;
	public final int MIN_ASTEROIDS_IN_UNIVERSE = 200;
	public final int NUM_ASTEROIDS_IN_UNIVERSE = 300;
	public final int MIN_SPEED_ASTEROID = 300;
	public final int MAX_SPEED_ASTEROID = 700;
	public final int ASTEROID_MASS = 2000;

	public static final int SMALL_PLANET = 0;
	public static final int MEDIUM_PLANET = 1;
	public static final int BIG_PLANET = 2;

	private ArrayList<SpaceObject> spaceObjects;
	private ArrayList<SpaceObject> deadObjects;
	private ArrayList<Asteroid> asteroids;
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
		asteroids = new ArrayList<Asteroid>();
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
	public Spaceship getSpaceship() {
		return spaceship;
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
	public void add(int index,SpaceObject so)
	{
		spaceObjects.add(index,so);
	}

	public void addAsteroid(Asteroid a) {
		asteroids.add(a);
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
			//here we check if ze spaceship is dead
			if (spaceship.getHealth() < 0) {
				//oh no!
				spaceship.setAlive(false);
				deadObjects.add(spaceship);
				game.setState(Game.DIED_SEQUENCE);

				//add more explosions

			}

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


		//THE ASTEROID GENERATOR////////////////////////////////////////////////////////////
		//This ensures that there is a certain number of asteroids in the universe
		////////////////////////////////////////////////////////////////////////////////////
		if (asteroids.size() < numAsteroids) {
			generateAsteroid(null);
		}

		for (SpaceObject obj : spaceObjects) {

			if (obj instanceof Planet) {
				//check if the planet is within range
				Planet p = (Planet) obj;
				obj.animate((int) timeElapsed);
				Vector2 pos = spaceship.getPos();
				double dist = p.getPos().subVector(pos).getLength();

				if (spaceship.getAlive()) {
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
							spaceship.takeDamage(1);
						}
					}
				}

				//check if the planet (namely, asteroid) is outside of the universe
				pos = p.getPos();
				if (pos.x < -WORLD_SIZE/2 || pos.y < -WORLD_SIZE/2 || pos.x >= WORLD_SIZE/2||pos.y >= WORLD_SIZE/2) {
					deadObjects.add(p);
				}

			}
			if (obj instanceof Spaceship && spaceship.getAlive())
			{
				Vector2 accel = spaceship.predictAccel();
				Vector2 vel = spaceship.predictVel(timeElapsed, accel);
				Vector2 pos = spaceship.predictPos(timeElapsed, vel);

				Vector2 r = new Vector2(-pos.x,-pos.y);

				double blackhole = 160*r.getLength()/(WORLD_SIZE);

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

			obj.update(timeElapsed);
		}

		//maximum speed for spaceship
		Vector2 v = spaceship.getVel();
		if (v.getLength() > MAX_SHIP_SPEED)
		{
			v = v.getNormalized().scale(MAX_SHIP_SPEED);
			spaceship.setVel(v);
		}

		if(viewport!=null)
			viewport.setCenter(spaceship.getPos());
		for(Explosion e1 : explosions)
		{
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
			if (obj instanceof Asteroid)
				asteroids.remove(obj);
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
		ship.setHealth(100);
		ship.setAlive(true);
		setSpaceship(ship);
		add(ship);

		Random rand=new Random();
		rand.setSeed(game.getLevelSeed());

		int numPlanets = 200+(level*20);

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
						if (d.getLength() < size + o.getRadius() + 500) {
							continue;
						}
					}
				}
			}
			SpaceObject so=new Planet(r,new Vector2(0,0),new Vector2(0,0),"planet"+rand.nextInt(3),mass,size);
			add(0,so);
		}

		numAsteroids = MIN_ASTEROIDS_IN_UNIVERSE+rand.nextInt(NUM_ASTEROIDS_IN_UNIVERSE);

		for(int x=0; x < numAsteroids; x++) {
			generateAsteroid(rand);
		}
	}

	public void generateAsteroid(Random rand)
	{
		double randomTheta = Math.random()*6.18;
		double velX = MAX_SPEED_ASTEROID * Math.cos(randomTheta);
		double velY = MAX_SPEED_ASTEROID * Math.sin(randomTheta);
		int half_world = WORLD_SIZE/2;
		Vector2 r = new Vector2(-half_world+Math.random()*WORLD_SIZE,-half_world+Math.random()*WORLD_SIZE);

		do {

			//check that it isn't near the spaceship
			if (r.subVector(spaceship.getPos()).getLength() >= 500) break;

			r = new Vector2(-half_world+Math.random()*WORLD_SIZE,-half_world+Math.random()*WORLD_SIZE);
			randomTheta = Math.random()*6.18;
			velX = MAX_SPEED_ASTEROID * Math.cos(randomTheta);
			velY = MAX_SPEED_ASTEROID * Math.sin(randomTheta);
		} while (true);

		Vector2 v = new Vector2(velX,velY);


		Asteroid a = new Asteroid(r, new Vector2(velX, velY), new Vector2(0,0), "asteroid", ASTEROID_MASS, 25);
		add(a);
		addAsteroid(a);

	}
}
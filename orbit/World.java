/*
    This file is part of Orbit.

    Orbit is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Orbit is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Orbit.  If not, see <http://www.gnu.org/licenses/>. 
    
    This software was developed by members of Project:Possibility, a software 
    collaboration for the disabled.
    
    For more information, visit http://projectpossibility.org
*/


package orbit;

import java.util.*;
import java.awt.*;

/**
 * 
 * The world is the main container object for the games.
 * Contains all space objects and  helps facilitate their
 * interactions.
 * 
 * @author Henry Yuen
 *
 */
public class World
{
	//universe constants
	public final int WORLD_SIZE = 36000;
	public final double MAX_SHIP_SPEED = 2500;
	public final int MIN_ASTEROIDS_IN_UNIVERSE = 200;
	public final int NUM_ASTEROIDS_IN_UNIVERSE = 300;
	public final int MIN_SPEED_ASTEROID = 300;
	public final int MAX_SPEED_ASTEROID = 700;
	public final int ASTEROID_MASS = 2000;

	//powerups
	public final int NUM_POWERUPS = 200;
	
	//planet constants
	public static final int SMALL_PLANET = 0;
	public static final int MEDIUM_PLANET = 1;
	public static final int BIG_PLANET = 2;
	
	//lists of objects
	private ArrayList<SpaceObject> spaceObjects;
	private ArrayList<SpaceObject> deadObjects;
	private ArrayList<SpecialPlanet> specialPlanets;
	private ArrayList<Asteroid> asteroids;
	private ArrayList<Explosion> explosions;

	//Game state objects
	private int numAsteroids;
	private Starfield starfield;
	private Spaceship spaceship;
	private BinaryInput binaryInput;
	private Rect viewport;
	private String explosionSprite;
	private ParticleSystem particleSystem;
	private int particleTimer;
	private int numToBeTagged;
	
	//fundamental objects
	private Game game;
	private SoundManager soundManager;

	/**
	 * Constructor for world
	 * @param g Associated game object
	 */
	public World(Game g)
	{
		game = g;
		spaceObjects=new ArrayList<SpaceObject>();
		deadObjects = new ArrayList<SpaceObject>();
		specialPlanets = new ArrayList<SpecialPlanet>();
		explosions = new ArrayList<Explosion>();
		asteroids = new ArrayList<Asteroid>();
		//create the starfield
		starfield = new Starfield();
		particleSystem=new ParticleSystem();
		soundManager = new SoundManager();
		//create the spaceship
		//spaceship = new Spaceship();
		particleTimer=0;

		numToBeTagged = 0;
	}
	
	//getters and setters
	
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

	public ArrayList<SpecialPlanet> getSpecialPlanets() {
		return specialPlanets;
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

	public void addSpecial(SpaceObject so)
	{
		specialPlanets.add((SpecialPlanet)so);
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

		if (numToBeTagged <= 0) {
			FlashingText ft = new FlashingText("NEXT LEVEL");
			ft.setColor(Color.CYAN);
			ft.setLife(7000);
			ft.setPos(380, 300);
			NotificationManager.getInstance().addFlashingText(ft);
			game.setState(Game.NEXT_LEVEL);
			return;
		}
		//apply thrusters
		if(spaceship!=null&&game.getState()==game.GAME)
		{
			//here we check if ze spaceship is dead
			if (spaceship.getHealth() < 0) {
				//oh no!
				spaceship.setAlive(false);
				deadObjects.add(spaceship);
				game.setState(Game.DIED_SEQUENCE);
				return;
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

			Vector2 playerpos = spaceship.getPos();

			if (obj instanceof Powerup) {
				double dist = obj.getPos().subVector(playerpos).getLength();

				if (dist < obj.getRadius()*3) {
					//acquire the powerup
					Powerup pup = (Powerup)obj;

					switch (pup.getType()) {
					case Powerup.EXTRA_LIFE: {
						//increase the lives
						FlashingText ft = new FlashingText("Got Extra Life!");
						ft.setColor(Color.GREEN);
						ft.setLife(3000);
						ft.setPos(380, 300);
						NotificationManager.getInstance().addFlashingText(ft);
						game.incrementLife();
						break;
					}
					case Powerup.SPEED_BOOST: {
						FlashingText ft = new FlashingText("Got Speed Boost!");
						ft.setColor(Color.GREEN);
						ft.setLife(3000);
						ft.setPos(380, 300);
						NotificationManager.getInstance().addFlashingText(ft);
						break;
					}
					}

					deadObjects.add(pup);
				}
			}
			//PLANET COLLISION DETECTION//////////////////////////////////////////////
			if (obj instanceof Planet) {
				//check if the planet is within range
				Planet p = (Planet) obj;
				obj.animate((int) timeElapsed);

				double dist = p.getPos().subVector(playerpos).getLength();

				if (spaceship.getAlive()) {
					if (dist < 10000) {
						if (!(obj instanceof Asteroid))
							spaceship.interact(p);
						//System.out.println(dist + "," + p.getRadius());
						//System.out.println(dist);
						//see if they collide
						if ((dist < p.getRadius()+50)&&(obj instanceof SpecialPlanet)) {
							// check boundaries for special planet
							//set planet to be tagged
							SpecialPlanet specP = (SpecialPlanet) obj;

							if (!specP.getTagged()) {
								specP.setTagged();
								numToBeTagged=numToBeTagged-1;

								FlashingText ft = new FlashingText("" + numToBeTagged + " Targets Left");
								ft.setColor(Color.BLUE);
								ft.setLife(3000);
								ft.setPos(370, 200);
								NotificationManager.getInstance().addFlashingText(ft);
							}
						}

						if (dist < p.getRadius()) {

							if (!(p instanceof SpecialPlanet)) {
								//collision!
								//splode!
								Explosion e = new Explosion(spaceship.getPos(), "explosion", spaceship.getWidth(), spaceship.getHeight());

								explosions.add(e);
								spaceship.takeDamage(1);
							}
						}
					}
				}

				//check if the planet (namely, asteroid) is outside of the universe
				Vector2 pos = p.getPos();
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

		//update the notification manager
		NotificationManager.getInstance().update(timeElapsed);
	}

	/** Populates the world with planets and spaceship based on difficulty.
	 *
	 **/
	public void populate(int level)
	{
		int numSpecialPlanets = level;
		numToBeTagged = numSpecialPlanets;

		//clear everything in asteroids
		asteroids.clear();
		explosions.clear();
		specialPlanets.clear();

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

		int half_world = WORLD_SIZE/2;

		//GENERATE THE SPECIAL PLANETS//////////////////////////////////////////////////////////////

		for(int i=0;i<numSpecialPlanets;i++)
		{
			int type = rand.nextInt(3);
			int size = 0;
			int mass = 0;

			switch (type) {
			case SMALL_PLANET: {
				size = 150;
				mass = 16000;
				break;
			}
			case MEDIUM_PLANET: {
				size = 175;
				mass = 20000;
				break;
			}
			case BIG_PLANET: {
				mass = 30000;
				size = 200;
				break;
			}
			} // end switch

			int loopCount = 0;
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

			//System.out.println(r);
			SpaceObject so=new SpecialPlanet(r,new Vector2(0,0),new Vector2(0,0),"planetTarget2",mass,size);
			add(so);
			addSpecial(so);
		} // end special planets loop

		//GENERATE THE PLANETS///////////////////////////////////////////////////////////////////
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

		///GENERATE POWERUPS /////////////////////////////////////////////////////
		for (int i=0;i<NUM_POWERUPS;i++) {
			int type = rand.nextInt(Powerup.NUM_TYPE_POWERUPS);

			Vector2 r = new Vector2(-half_world+Math.random()*WORLD_SIZE,-half_world+Math.random()*WORLD_SIZE);
			Powerup pup = new Powerup(type,r);

			add(pup);
		}
	}
	
	/**
	 * Generates random asteroids in the world
	 * @param rand MathRandom object to use
	 */
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
	
	/**
	 * 
	 * @return Number of special planets that are not yet
	 * tagged.
	 */
	public int getNumTargetsLeft() {
		return this.numToBeTagged;
	}
}
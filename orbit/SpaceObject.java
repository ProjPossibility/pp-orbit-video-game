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


/*
	name: SpaceObject
	description: This is the space object that is the base object for all the
		objects that reside in the world object there are a lot of object names
		in this object.
*/

package orbit;

import java.awt.Image;
import javax.swing.ImageIcon;

/**
 * 
 * Space Object is the base class which all objects in the 
 * game extend. Space Object extensions include planet,
 * spaceships, asteroids, etc.
 * 
 * @author Henry Yuen
 *
 */
public class SpaceObject {

	//for mechanics
	protected Vector2 pos;
	protected Vector2 vel;
	protected Vector2 accel;

	protected String sprite;

	//this is used for image drawing and collision detection
	protected  double width;
	protected  double height;

	protected  double radius;

	protected boolean alive;

	/**
	 * Constructor  (Default)
	 */
	public SpaceObject() {
		pos = new Vector2();
		vel = new Vector2();
		accel = new Vector2();
		sprite = new String("null");
		width = height = radius = 0.0;

		currentFrame = 0;
		numFrames = 0;
		elapsedTime = 0;
		timePerFrame = 0;
		looping = false;

		alive = false;
	}

	/**
	 * Constructor (specified)
	 */
	public SpaceObject(Vector2 p,Vector2 v,Vector2 a,String sprite,double width,double height)
	{
		pos = p;
		vel = v;
		accel = a;
		this.sprite = sprite;
		this.width = width;
		this.height = height;
		this.radius = Math.min(width,height)/2.0;

		alive = false;
	}


	//getters and setters
	public boolean getAlive() { return alive; }
	public void setAlive(boolean alive) { this.alive = alive; }

	public Vector2 getPos() { return pos; }
	public Vector2 getVel() { return vel; }
	public Vector2 getAccel() { return accel; }
	public String getSprite() { return sprite; }
	public double getWidth() { return width; }
	public double getHeight() { return height; }
	public double getRadius() { return radius; }

	public void setPos(Vector2 p) { pos = p; }
	public void setVel(Vector2 v) { vel = v; }
	public void setAccel(Vector2 a) { accel = a; }

	public Image getFrame()
	{
		//return new ImageIcon("build/media/spaceship.jpg").getImage();
		return ResourceManager.getImage(sprite,currentFrame);
	}

	/*
	 * =======ANIMATION CODE BELOW HERE=====================================
	 */
	
	/*
	 * The current frame index
	 */
	protected int currentFrame = 0;
	
	/*
	 * How much time should a frame be given?
	 */
	protected int timePerFrame =0;
	
	/*
	 * How much time has elapsed since the last frame.
	 * 
	 */
	protected int elapsedTime =0;
	
	/*
	 * Number of frames in animation
	 */
	protected int numFrames = 0;
	
	/*
	 * Whether animation loops or is one-time.
	 */
	protected boolean looping = false;

	/**
	 * This sets the initial animation properties
	 * @param timePerFrame	the number of milliseconds per frame
	 * @param numFrames		The total number of frames present
	 * @param looping		Set this to true if you want the animation to repeatedly loop
	 */
	public void setAnimationProperties(int timePerFrame,int numFrames,boolean looping) {
		this.timePerFrame = timePerFrame;
		this.numFrames = numFrames;
		this.looping = looping;
		currentFrame = 0;
		elapsedTime = 0;
	}
	
	/*
	 * The function that handles the animation. Takes
	 * in a clock pulse, and based upon the clock, knows
	 * when to change the frame to the next frame. Animate,
	 * by default, handles looping animations.
	 * 
	 * @param msSinceLastTime how many milliseconds have
	 * passed since animate was last called.
	 */
    public void animate(int msSinceLastTime) {
    	if (!looping) return;

    	elapsedTime+=msSinceLastTime;
    	if(elapsedTime>timePerFrame)
    	{
    		//This code here loops the animation
    		if(currentFrame==numFrames-1) {
            	currentFrame = 0;
    		}
    		else {
    			currentFrame++;
    		}
    		elapsedTime=0; //start counting from 0
    	}
    }
    
    /*
     * Does animate for nonlooping animations.
     * 
     * @param msSinceLastTime how many milliseconds have passed
     * since the last time this function was called.
     */
    public void nonLoopingAnimate(int msSinceLastTime) {
    	elapsedTime+=msSinceLastTime;
    	if(elapsedTime>timePerFrame)
    	{
    		//This code here loops the animation
    		if(currentFrame==numFrames-1) {
            	currentFrame = -1; //signifies end
    		}
    		else {
    			currentFrame++;
    		}
    		elapsedTime=0; //start counting from 0
    	}
    }
    
    /*
     * Whether the animation should loop or not
     * 
     * @param loop To loop or not to loop
     */
    public void setLooping(boolean loop) {
    	looping = loop;
    }

	/**
	 * Sets the frame of the animation
	 * 
	 * @param f The index of the frame to set (indexes are 0-based)
	 */
    public void setFrame(int f) {
    	System.out.println("\nFRAME: "+f+"\n");
    	if (f < numFrames && f >= 0)
    		currentFrame = f;
    }

	/**
	 * Update function - meant to be overridden by specific space objects.
	 * 
	 * @param timeElapsed - How much time has elapsed since last update call.
	 */
    public void update(long timeElapsed)
    {

    }
}



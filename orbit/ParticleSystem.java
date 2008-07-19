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

/**
 * Our very own custom particle effects system.
 */
public class ParticleSystem
{
	/**
	 * Listing of particle effects.
	 */
	private ArrayList<ParticleEffect> particles;
	
	/**
	 * Constructor
	 */
	public ParticleSystem()
	{
		particles=new ArrayList<ParticleEffect>();
	}
	
	/**
	 * Update function
	 * 
	 * @param millis time elasped since update was last called.
	 */
	public void update(int millis)
	{
		for(int i=0;i<particles.size();i++)
		{
			ParticleEffect part=particles.get(i);
			if(part.isAlive())
				part.update(millis);
			else
			{
				particles.remove(i);
				i--;
			}
		}
	}
	
	/**
	 * 
	 * @return particle effects list
	 */
	public ArrayList<ParticleEffect> getParticles()
	{
		return particles;
	}
	public void addParticle(Vector2 pos,Vector2 vel,String sprite,double diameter)
	{
		particles.add(new ParticleEffect(pos,vel,new Vector2(),sprite,diameter,diameter));
	}
	
}

/**
 * 
 * A particle effect is a space object that dies really fast.
 * 
 * @author Aadarsh Patel
 *
 */
class ParticleEffect extends SpaceObject
{
	public ParticleEffect(Vector2 p,Vector2 v,Vector2 a,String sprite,double width,double height)
	{
		super(p,v,a,sprite,width,height);
		setAnimationProperties(90,6,true);
	}
	/** Updates the animation
	 *
	 **/
	public void update(int millis)
	{
		//pos=pos.addVector(vel.scale(millis*0.001));
		animate(millis);
	}
	public boolean isAlive()
	{
		return currentFrame!=numFrames-1;
	}
}
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

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Extension of screen overlay technology to include
 * clocking.
 * @author Aadarsh Patel
 *
 */
public class TimedScreenOverlay extends ScreenOverlay {

	public final static int FADE_IN = 0;
	public final static int FADE_OUT = 0;

	long duration;
	long count;
	int mode;

	public TimedScreenOverlay(Graphics2D g,Color c,int x,int y,int width,int height,long duration,int mode) {
		super(g,c,new Rect(x,y,width,height));
		count = 0;
		this.duration = duration;
		this.mode = mode;
	}

	public void update(long timeElapsed) {
		count += timeElapsed;
	}

	public void paint() {
		float alpha;
		float ratio = (float)count/(float)duration;
		if (mode == FADE_IN) {
			//then it's going from total opacity to total transparency
			alpha = 1.0f - ratio;
		} else {
			alpha = ratio;
		}

		if (alpha < 0) alpha = 0.0f;
		if (alpha > 1.0f) alpha = 1.0f;

		alpha *= 255;

		super.paint(alpha);
	}


}

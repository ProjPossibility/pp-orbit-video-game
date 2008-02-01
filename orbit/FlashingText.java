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

import javax.swing.*;
import java.awt.*;

/**
 * This text flashes for a duration of time.
 * This is helpful:
 * <http://www.apl.jhu.edu/~hall/java/Java2D-Tutorial.html>
 * @author Henry Yuen
 *
 */
public class FlashingText {

	String text;
	long flashLength;
	long life;
	long overallCount;
	long count;
	int x,y;
	int size;
	Color color;
	boolean on;
	boolean alive;


	public FlashingText(String text) {
		this.text = text;
		flashLength = 0;
		life = 0;
		overallCount = 0;
		x = y = size = 0;
		count = 0;
		color = null;
		on = true;
		alive = true;
	}

	public void setFlashLength(long length) {
		flashLength = length;
	}

	public void setLife(long life) {
		this.life = life;
	}

	public void setPos(int x,int y) {
		this.x = x;
		this.y = y;
	}

	public void setColor(Color c) {
		color = c;
	}

	public void setSize(int s) {
		size = s;
	}

	public boolean getAlive() {
		return alive;
	}

	public void update(long timeElapsed) {
		overallCount += timeElapsed;
		if (overallCount >= life) {
			alive = false;
		}
		/*
		count += timeElapsed;
		if (count >= flashLength) {
			count = 0;
			on = on ? false : true;
		}
		*/
	}

	public void paint() {
		if (on == false) return;
		if (alive == false) return;

		PrintManager.getInstance().print("large", text, x, y, color);

	}

}

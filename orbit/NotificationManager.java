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

import java.util.ArrayList;

/**
 * 
 * Manages notifications for the screens. Basically,
 * manages flashing texts on the screen.
 * 
 * @author Henry Yuen, Prateek Tandon
 *
 */
public class NotificationManager {

	/**
	 * Container for flashing texts
	 */
	private ArrayList<FlashingText> flashingTexts;
	private static NotificationManager instance = null;
	
	/**
	 * Singleton pattern
	 * @return singleton instance
	 */
	public static NotificationManager getInstance() {
		if (instance == null) instance = new NotificationManager();
		return instance;
	}

	private NotificationManager() {
		flashingTexts = new ArrayList<FlashingText>();
	}
	
	/**
	 * Add flashing text to manager
	 * @param ft flashing text object to add
	 */
	public void addFlashingText(FlashingText ft) {
		flashingTexts.add(ft);
	}
	
	/**
	 * Update function for notification manager. Basically
	 * kills flashing texts that have outlived their lifetime.
	 * 
	 * @param timeElapsed Time elapsed since update was last called.
	 */
	public void update(long timeElapsed) {
		ArrayList<FlashingText> toRemove = new ArrayList<FlashingText>();

		for (FlashingText ft : flashingTexts) {
			ft.update(timeElapsed);
			if (!ft.getAlive()) {
				toRemove.add(ft);
			}
		}

		for (FlashingText ft : toRemove) flashingTexts.remove(ft);
		toRemove.clear();
	}

	/*
	 * Paint function override
	 */
	public void paint() {
		for (FlashingText ft : flashingTexts) {
			ft.paint();
		}
	}


}

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

/**
 * BinaryInput models a single button and whether it is on or off. The framework that this class provides encapsulates
 * the logic of handling an on/off button. When our software is industrialized, the input for that "button" can be hooked up to any hardware
 * input device -  whether its an eye blink detector, a foot pedal, or some other device for people with disabilities. 
 */
public class BinaryInput
{
	/**
	 * Whether the button is on or off.
	 */
	private boolean buttonOn;
	
	/**
	 * An integer that also stores the state of the button.
	 */
	private int buttonState;
	
	/**
	 * Handles double clicks (future feature).
	 */
	private long timeMark;
	
	/**
	 * Default Constructor
	 */
	public BinaryInput()
	{
		buttonOn=false;
		buttonState=0;
		timeMark=System.currentTimeMillis();
	}
	
	/**
	 * Change button status based on button press.
	 */
	public void buttonChanged(boolean on)
	{
		long current=System.currentTimeMillis();
		
		//if button was off before and now its pressed
		if(buttonState==0&&on)
		{
			buttonOn=true;
			buttonState=1;
			timeMark=current;
		}
		//if button was on and now its released
		else if(buttonState==1&&!on)
		{
			buttonOn=false;
			buttonState=0;
			timeMark=current;
		}
	}
	
	/**
	 * Returns whether button is pressed or not (0 - off, 1 - on)
	 */
	public int getButtonState()
	{
		return buttonState;
	}
}
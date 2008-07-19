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
import sun.audio.*;    //import the sun.audio package
import java.io.*;
import java.applet.*;
import java.util.*;
public class SoundManager {
private static HashMap<String, AudioStream> soundTrackVector;
private java.applet.AudioClip winclip;

/**
 * Handles playing of background music.
 */
public SoundManager(){
	// Open an input stream  to the audio file.
	try{
	
		winclip=java.applet.Applet.newAudioClip(new java.net.URL("file:media/victory.wav"));
		winclip.loop();
	}
	catch(Exception e){
		System.out.println("Error in inputting from file");
	}
}
	
}

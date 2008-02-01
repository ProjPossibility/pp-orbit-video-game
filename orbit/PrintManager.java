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

import java.awt.*;
import java.awt.geom.*;
import java.util.*;

public class PrintManager {

	public static final int LEFT = 0;
	public static final int RIGHT = 0;
	public static final int CENTER = 0;

	private HashMap<String,Font> fonts;
	private String currentFont;
	private Graphics2D graphics;
	private static PrintManager instance = null;

	private PrintManager() {
		fonts = new HashMap<String,Font>();
		currentFont = null;
	}

	public static PrintManager getInstance() {
		if (instance == null) instance = new PrintManager();
		return instance;
	}

	public void setGraphics(Graphics2D g) {
		graphics = g;
	}

	public void addFont(String id,Font f) {
		fonts.put(id,f);
	}

	public void print(String font,String text,int x,int y,Color c,int alignment) {
		if (!fonts.containsKey(font)) return;

		if (alignment == LEFT) {
			print(font,text,x,y,c);
			return;

		}

		Font newfont = fonts.get(font);
		graphics.setFont(newfont);
		FontMetrics fm = graphics.getFontMetrics();
		Rectangle2D rect = fm.getStringBounds(text,graphics);

		if (alignment == CENTER) {
			print(font,text,(int)(x-(rect.getWidth()/2)),y,c);
		} else if (alignment == RIGHT) {
			print(font,text,(int)(x-(rect.getWidth())),y,c);
		}
	}


	public void print(String font,String text,int x,int y,Color c) {
		if (!fonts.containsKey(font)) return;

		Font oldfont,newfont;

		oldfont = graphics.getFont();
		newfont = fonts.get(font);
		graphics.setFont(newfont);
		currentFont = font;


		Composite comp = graphics.getComposite();
		float alpha = (float)c.getAlpha()/255.0f;

		graphics.setComposite(
				AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));

		graphics.setColor(c);

		graphics.drawString(text, x, y);

	//	graphics.setComposite(comp);
		//graphics.setFont(oldfont);

	}



}

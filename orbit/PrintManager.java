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
		FontMetrics fm = graphics.getFontMetrics(newfont);
		Rectangle rect = fm.getStringBounds(text,graphics).getBounds();

		if (alignment == CENTER) {
			print(font,text,x-(rect.width/2),y,c);
		} else if (alignment == RIGHT) {
			print(font,text,x-(rect.width),y,c);
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

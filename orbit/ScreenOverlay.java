package orbit;

import java.awt.geom.*;
import java.awt.*;

public class ScreenOverlay {

	Color color;
	int width,height;
	Graphics2D graphics;

	public ScreenOverlay(Graphics2D g,Color c,int width,int height) {
		graphics = g;
		color = c;
		this.width = width;
		this.height = height;
	}

	public void paint(float alpha) {
		Composite comp = graphics.getComposite();
		graphics.setComposite(
				AlphaComposite.getInstance(AlphaComposite.SRC_OVER,alpha));

		graphics.setColor(color);
		graphics.fillRect(0,0,width,height);
		graphics.setComposite(comp);

	}

}

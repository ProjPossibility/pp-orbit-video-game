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
import java.awt.*;
import java.awt.image.*;

import javax.swing.*;

/** 
 * 
 * The resource manager does loading and presentation of media.
 * Follows the singleton pattern.
 * @author Henry Yuen
 *
 */
public class ResourceManager {
	
	/**
	 * The images in the system.
	 */
	private static HashMap<String, BufferedImage> imageMap;
	
	/**
	 * Singleton instance
	 */
	private static ResourceManager instance = new ResourceManager();

	private ResourceManager() {
		imageMap = new HashMap<String, BufferedImage>();
	}
	
	/**
	 * Convert a image to a buffered image.
	 * @param image original image
	 * @return buffered image object
	 */
	private static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage)image;
        }

        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();

        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(image.getWidth(null), image.getHeight(null), Transparency.TRANSLUCENT );
        } catch (HeadlessException e) {
        	e.printStackTrace();
        }
        if (bimage == null) {
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
        }

        // Copy image to buffered image
        Graphics g = bimage.createGraphics();

        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return bimage;
    }
	
	/**
	 * Add an image sequence to the resource manager
	 * @param imagePath The image file
	 * @param numImages The number of images in the file
	 * @param sequenceName The name of the sequence (for hashing purposes)
	 */
	public static void addImageSequence(String imagePath, int numImages, String sequenceName) {
		Image loadedImage = Toolkit.getDefaultToolkit().getImage(imagePath);
		BufferedImage globalImg = toBufferedImage(loadedImage);
		int parseWidth = globalImg.getWidth() / numImages;
		int height = globalImg.getHeight();
		for(int x=0; x < numImages; x++) {
			BufferedImage i = globalImg.getSubimage(x*parseWidth, 0, parseWidth, height);
			imageMap.put(sequenceName+x, i);
		}
	}
	
	/**
	 * Returns hashed image
	 * @param key The key of the image
	 * @param frame The frame of the image
	 * @return
	 */
	public static Image getImage(String key, int frame) {
		String hashKey = key + frame;
		return imageMap.get(hashKey);
	}
}

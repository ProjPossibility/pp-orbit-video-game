import java.util.*;
import java.awt.*;
import java.awt.image.*;


public class Animation {

	private ArrayList<Image> animationSteps;
	private int currentStep = 0;
	private int timePerFrameMs =0;
	private int elapsedTime =0;
	
	public Animation(String imagePath, int parseWidth, int parseHeight, int timePerFrameMs) {
		animationSteps = new ArrayList<Image>();
		Image loadedImage = Toolkit.getDefaultToolkit().getImage(imagePath);
		System.out.println(loadedImage.getWidth(null));
		BufferedImage globalImg = bufferImage(loadedImage, BufferedImage.TYPE_INT_RGB);
		int numImages = loadedImage.getWidth(null) / parseWidth;		
		for(int x=0; x < numImages; x++) {
			BufferedImage i = globalImg.getSubimage(x*parseWidth, 0, parseWidth, parseHeight);
			animationSteps.add(i);
		}
	}
	
    private BufferedImage bufferImage(Image image, int type) {
        BufferedImage bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        Graphics2D g = bufferedImage.createGraphics();
        g.drawImage(image, 0,0, null);
        g.dispose();
        return bufferedImage;
    }
    
    public Image getCurrentImage() {
    	return animationSteps.get(currentStep);
    }
    
    public void incrementAnimation(int msSinceLastTime) {
    	elapsedTime+=msSinceLastTime;
    	if(elapsedTime>timePerFrameMs)
    	{
    		//This code here loops the animation
    		if(currentStep==animationSteps.size()-1) {
            	currentStep = 0;  			
    		}
    		else {
    			currentStep++;
    		}
    		elapsedTime=0; //start counting from 0
    	}
    }	
}

package orbit;

import javax.swing.*;
import java.util.*;

public class OrbitGame
{
	public static void main(String[] args)
	{
		JFrame frame=new JFrame();

		Rect screen=new Rect(0,0,800,600);
		frame.setSize((int)screen.width,(int)screen.height);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocation(200,100);
		Rect viewport=new Rect(0,0,1000,800);
		Game game=new Game();
		World world=new World(game);
		//Spaceship so=new Spaceship(new Vector2(200,300),new Vector2(0,0),new Vector2(0,0),"spaceship",50,50);
		//world.getSpaceObjects().add(so);
		//world.setSpaceship(so);
		world.populate(1);
		world.setViewport(viewport);


		ResourceManager.addImageSequence("media/rocketS.png",1,"spaceship");
		ResourceManager.addImageSequence("media/star.gif",4,"star");
		ResourceManager.addImageSequence("media/planet1.png",1,"planet1");
		ResourceManager.addImageSequence("media/planet2.png",1,"planet2");

		ScrollingScreen scroll=new ScrollingScreen(screen,viewport,world);

		BinaryInput binIn=new BinaryInput();
		scroll.setBinaryInput(binIn);
		world.setBinaryInput(binIn);



		frame.setContentPane(scroll);
		frame.pack();
		frame.validate();
		frame.repaint();
		scroll.requestFocus();
		long start=System.currentTimeMillis();
		while(true)
		{
			long curr=System.currentTimeMillis();
			long millis=curr-start;
			start=curr;
			world.update(millis);
			frame.repaint();
			try{
				Thread.sleep(15);
			}catch(Exception e){}
		}
	}
}
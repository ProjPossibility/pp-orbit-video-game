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
		
		World world=new World();
		SpaceObject so=new SpaceObject(new Vector2(200,300),new Vector2(0,0),new Vector2(0,0),"spaceship",50,50);
		world.getSpaceObjects().add(so);
		ResourceManager.addImageSequence("media/spaceship0.jpg",1,"spaceship");
		
		ScrollingScreen scroll=new ScrollingScreen(screen,viewport,world);
		frame.setContentPane(scroll);
		frame.pack();
		frame.validate();
		frame.repaint();
	}
}
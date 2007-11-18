package orbit;

import javax.swing.*;

public class Game extends JFrame {

	public static final int START_SCREEN =0;
	public static final int GAME_STARTED =1;
	public static final int LOSS_SEQUENCE =3;
	public static final int LOSS_SCREEN =4;
	public static final int NEXT_LEVEL_SCREEN =5;

	private int state;
	private int currentLevel;
	private int points;
	private World world;

	public Game() {

		state = START_SCREEN;
		currentLevel =0;
		points =0;
		world = new World(this);

		Rect screen=new Rect(0,0,800,600);
		setSize((int)screen.width,(int)screen.height);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLocation(200,100);
		Rect viewport=new Rect(0,0,1500,1200);
		World world=new World(this);
		//Spaceship so=new Spaceship(new Vector2(200,300),new Vector2(0,0),new Vector2(0,0),"spaceship",50,50);
		//world.getSpaceObjects().add(so);
		//world.setSpaceship(so);
		world.populate(1);

		ResourceManager.addImageSequence("media/rocketS.png",1,"spaceship");
		ResourceManager.addImageSequence("media/star.gif",4,"star");
		ResourceManager.addImageSequence("media/planet1.png",1,"planet1");
		ResourceManager.addImageSequence("media/planet2.png",1,"planet2");
		ResourceManager.addImageSequence("media/planet7.png",1,"planet7");
		ResourceManager.addImageSequence("media/explosion.gif", 18, "explosion");

		ScrollingScreen scroll=new ScrollingScreen(screen,viewport,world);

		BinaryInput binIn=new BinaryInput();
		scroll.setBinaryInput(binIn);
		world.setBinaryInput(binIn);
		
		world.setViewport(viewport);

		setContentPane(scroll);
		pack();
		validate();
		repaint();
		
		scroll.requestFocus();

		long start=System.currentTimeMillis();
		while(true)
		{
			long curr=System.currentTimeMillis();
			long millis=curr-start;
			start=curr;
			world.update(millis);
			repaint();
			try{
				Thread.sleep(15);
			}catch(Exception e){}
		}

	}

	public int getState() {
		return state;
	}
	
	public void setState(int state) {
		this.state = state;
		switch(state) {
			case 1:
				System.out.println("STATE:" + state);
			case 2:
				System.out.println("STATE:" + state);
			case 3:
				System.out.println("STATE:" + state);
			case 4:
				System.out.println("STATE:" + state);
			case 5:
				System.out.println("STATE:" + state);
		}
	}
}
package orbit;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Game extends JFrame {

	public static final int START_SCREEN =0;
	public static final int INIT_GAME = 1;
	public static final int GAME  =2;
	public static final int DIED_SEQUENCE =3;
	public static final int LOSS_SCREEN =4;
	public static final int NEXT_LEVEL =5;

	private int state;
	private int currentLevel;
	private int points;
	private int lives;

	private long levelSeed;
	private World world;
	private BinaryInput binIn;

	private ScrollingScreen scroll;
	private Rect viewport;
	private Rect screen;



	public Game() throws Exception {

		//Rect screen=new Rect(0,0,800,620);
		//Rect screen=new Rect(0,0,400,385);

		screen=new Rect(0,0,800,600);

		setSize((int)screen.width,(int)screen.height);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLocation(200,100);

		binIn=new BinaryInput();



		try {
			loadResources();

		} catch (Exception e) {
			throw new Exception(e);
		}

		setState(INIT_GAME);

	}

	public long getLevelSeed() {
		return levelSeed;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
		switch(state) {
			case INIT_GAME:
				setInitGameState();
				System.out.println("STATE:" + state);
				break;
			case GAME:

				setGameState();

				System.out.println("STATE:" + state);
				break;
			case NEXT_LEVEL:
				setNextLevelState();
				System.out.println("STATE:" + state);
				break;
			case DIED_SEQUENCE:
				setDiedSequenceState();
				System.out.println("STATE:" + state);
				break;
		}
	}

	private void loadResources() throws Exception {

		PrintManager.getInstance().setGraphics((Graphics2D)getGraphics());

		ResourceManager.addImageSequence("media/rocketS.png",1,"spaceship");
		ResourceManager.addImageSequence("media/star.gif",4,"star");
		ResourceManager.addImageSequence("media/planet1.png",1,"planet0");
		ResourceManager.addImageSequence("media/planet2.png",1,"planet1");
		ResourceManager.addImageSequence("media/planet7.2.png",1,"planet2");
		ResourceManager.addImageSequence("media/planet7.split.png",2,"planetTarget2");
		ResourceManager.addImageSequence("media/explosion.gif", 18, "explosion");
		ResourceManager.addImageSequence("media/smoke.gif", 6, "smoke");
		ResourceManager.addImageSequence("media/rocketSthrust.png",1,"spaceshipthrust");
		ResourceManager.addImageSequence("media/FinalAsteroid.png", 25, "asteroid");

		//add the fonts of the game
		PrintManager.getInstance().addFont("small", new Font("Comic Sans MS",Font.PLAIN,12));
		PrintManager.getInstance().addFont("medium", new Font("Comic Sans MS",Font.PLAIN,16));
		PrintManager.getInstance().addFont("large", new Font("Comic Sans MS",Font.PLAIN,24));
		PrintManager.getInstance().addFont("huge", new Font("Comic Sans MS",Font.PLAIN,48));
	}


	private void setStartScreenState() {


	}

	private void setLossScreenState() {


	}

	private void setInitGameState() {
		currentLevel = 0;
		points = 0;
		lives = 10;

		viewport=new Rect(0,0,1000,800);
		world = new World(this);

		world.setBinaryInput(binIn);
		world.setViewport(viewport);
		scroll = new ScrollingScreen(this,screen,viewport,world);
		scroll.setBinaryInput(binIn);

		//MainPage mainP = new MainPage();
		//WinPage winP = new WinPage();

		//BinaryInput binIn = new BinaryInput();

		//mainP.setBinaryInput(binIn);
		//winP.setBinaryInput(binIn);

		//scroll.setBinaryInput(binIn);

		setContentPane(scroll);
		pack();
		validate();
		repaint();

		scroll.requestFocus();
		setState(NEXT_LEVEL);
	/*
		for (int i = 0; i<10000; i++){
				for (int j = 0; j<100000; j++){} // busy loop
		}
		while(true){
			setContentPane(mainP);
			repaint();
			validate();
			try {
				Thread.sleep(230);}
			catch(Exception e){	}
		}
		pack();
		validate();
		repaint();

		scroll.requestFocus();

		setState(NEXT_LEVEL);*/
	}

	private void setNextLevelState() {
		++currentLevel;

		levelSeed=System.currentTimeMillis();

		//clear the world of the extant objects, and repopulate
		world.populate(currentLevel);
		setState(GAME);
	}

	private void setGameState() {


		Graphics2D g2d = (Graphics2D)getGraphics();


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

	private void setDiedSequenceState() {
		//decrement lives

		--lives;
		if (lives < 0) {
			setState(LOSS_SCREEN);
			return;
		}

		Graphics2D g2d = (Graphics2D)getGraphics();

		//run through the died sequence state
		long start=System.currentTimeMillis();
		while(true)
		{
			//wait for the user to press the button
			if (binIn.getButtonState() == 1) {
				break;
			}

			long curr=System.currentTimeMillis();
			long millis=curr-start;
			start=curr;
			world.update(millis);
			repaint();
			try{
				Thread.sleep(15);
			}catch(Exception e){}
		}

		world.populate(currentLevel);

		setState(GAME);
	}
}
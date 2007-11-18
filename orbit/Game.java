package orbit;

import javax.swing.*;

import java.awt.Color;
import java.awt.Graphics2D;
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
			case 4:
				System.out.println("STATE:" + state);
		}
	}

	private void loadResources() throws Exception {

		ResourceManager.addImageSequence("media/rocketS.png",1,"spaceship");
		ResourceManager.addImageSequence("media/star.gif",4,"star");
		ResourceManager.addImageSequence("media/planet1.png",1,"planet0");
		ResourceManager.addImageSequence("media/planet2.png",1,"planet1");
		ResourceManager.addImageSequence("media/planet7.2.png",1,"planet2");
		ResourceManager.addImageSequence("media/explosion.gif", 18, "explosion");
		ResourceManager.addImageSequence("media/smoke.gif", 6, "smoke");
		ResourceManager.addImageSequence("media/rocketSthrust.png",1,"spaceshipthrust");
	}


	private void setStartScreenState() {


	}

	private void setInitGameState() {
		currentLevel = 0;
		points = 0;

		viewport=new Rect(0,0,1000,800);
		world = new World(this);
		world.setBinaryInput(binIn);
		world.setViewport(viewport);
		scroll = new ScrollingScreen(screen,viewport,world);
		scroll.setBinaryInput(binIn);

		setContentPane(scroll);
		pack();
		validate();
		repaint();

		scroll.requestFocus();

		setState(NEXT_LEVEL);
	}

	private void setNextLevelState() {
		++currentLevel;

		levelSeed=System.currentTimeMillis();

		//clear the world of the extant objects, and repopulate
		world.populate(currentLevel);
		setState(GAME);
	}

	private void setGameState() {

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

		TimedScreenOverlay tso =
			new TimedScreenOverlay(
					g2d,
					Color.RED,
					(int)screen.width,
					(int)screen.height,
					3000,
					TimedScreenOverlay.FADE_OUT);


		FlashingText ft = new FlashingText(g2d,"PRESS TO CONTINUE");
		ft.setColor(Color.WHITE);
		ft.setLength(500);
		ft.setPos(200, 200);
		ft.setSize(15);

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
			tso.update(millis);
			tso.paint();
			ft.update(millis);
			ft.paint();
			repaint();
			try{
				Thread.sleep(15);
			}catch(Exception e){}
		}

		//reset everything
		world.populate(currentLevel);

		setState(GAME);
	}
}
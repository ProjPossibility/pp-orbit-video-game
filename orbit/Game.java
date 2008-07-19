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

import javax.swing.*;
import java.awt.*;
import java.util.*;

/**
 * This is the main game class the
 * @author Shivani Srivastava, Prateek Tandon, Aadarsh Patel, Candice Zimmerman, Henry Yuen
 *
 */
public class Game implements Runnable{

	//game state machine constants
	public static final int START_SCREEN =0;
	public static final int INIT_GAME = 1;
	public static final int GAME  =2;
	public static final int DIED_SEQUENCE =3;
	public static final int LOSS_SCREEN =4;
	public static final int NEXT_LEVEL =5;

	//game stats
	private int state;
	private int currentLevel;
	private int points;
	private int lives;
	private long levelSeed;

	/**
	 * The actual game frame.
	 */
	private JFrame gameFrame;

	/**
	 * The thread the  game runs in.
	 */
	private Thread thread;

	/**
	 * Container object for world objects
	 */
	private World world;

	/**
	 * Binary input interface used to get input
	 */
	private BinaryInput binIn;

	/**
	 * The scrolling screen
	 */
	private ScrollingScreen scroll;

	/**
	 * The size of the viewport.
	 */
	private Rect viewport;

	/**
	 * The size of the screen
	 */
	private Rect screen;

	/**
	 * The start screen page object
	 */
	private MainPage startScreen;

	/**
	 * The victory page object
	 */
	private WinPage winScreen;

	/*
	 * Constructor throws exception
	 */
	public Game() throws Exception {
		//initialize game basic objects
		gameFrame = new JFrame();
		thread=new Thread(this);
		screen=new Rect(0,0,800,600);

		//prepare game frame
		gameFrame.setSize((int)screen.width,(int)screen.height);
		gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		gameFrame.setLocation(200,100);

		//prepare interface and screens
		binIn=new BinaryInput();
		startScreen = new MainPage(this);
		startScreen.setBinaryInput(binIn);
		winScreen = new WinPage(this);
		winScreen.setBinaryInput(binIn);
		viewport=new Rect(0,0,1000,800);

		//ask resource manager to load resources
		try {
			loadResources();

		} catch (Exception e) {
			throw new Exception(e);
		}

		//show start screen
		gameFrame.setSize(400,400);
		gameFrame.setResizable(false);
		gameFrame.setVisible(true);
		setState(START_SCREEN);
		gameFrame.setContentPane(startScreen);

	}

	/**
	 * Returns level player is on
	 */
	public long getLevelSeed() {
		return levelSeed;
	}

	/**
	 * Returns the number of target planets left to get
	 * in the game.
	 */
	public int getNumToBeTagged() {
		return world.getNumTargetsLeft();
	}

	/**
	 *
	 * @return The number of lives the current player has.
	 */
	public int getLives() {
		return lives;
	}

	/**
	 *
	 * @return The state of the game
	 */
	public int getState() {
		return state;
	}

	/**
	 * Starts the game thread.
	 */
	public void start()
	{
		thread.start();
	}

	/**
	 * Sets the state of the game
	 *
	 * @param state The state to set the game to
	 */
	public void setState(int state) {
		this.state = state;
		//System.out.println("STATE:" + state);
		/*
		switch(state) {
			case START_SCREEN:
				setStartScreenState();
				return;
			case INIT_GAME:
				setInitGameState();
				return;
			case GAME:
				setGameState();
				return;
			case NEXT_LEVEL:
				setNextLevelState();
				return;
			case DIED_SEQUENCE:
				setDiedSequenceState();
				return;
			case LOSS_SCREEN
				setDiedSequenceState();
				return;
		}*/
	}

	/**
	 * Calls resource manager to loda necessary media
	 * and other tidbits.
	 */
	private void loadResources() throws Exception {
		PrintManager.getInstance().setGraphics((Graphics2D)gameFrame.getGraphics());
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

		ResourceManager.addImageSequence("media/speedboost.png",1,"speedboost");
		ResourceManager.addImageSequence("media/extralife.png",1,"extralife");

		ResourceManager.addImageSequence("media/arrow.gif", 1, "pointer");


		//add the fonts of the game
		PrintManager.getInstance().addFont("small", new Font("Comic Sans MS",Font.PLAIN,12));
		PrintManager.getInstance().addFont("medium", new Font("Comic Sans MS",Font.PLAIN,16));
		PrintManager.getInstance().addFont("large", new Font("Comic Sans MS",Font.PLAIN,24));
		PrintManager.getInstance().addFont("huge", new Font("Comic Sans MS",Font.PLAIN,48));
	}

	/**
	 * Refreshes the start screen.
	 */
	private void updateStartScreenState() {
		//gameFrame.setContentPane(startScreen);
		gameFrame.repaint();
		gameFrame.validate();
	}

	/**
	 * Handles the init game  state.
	 **/
	private void updateInitGameState() {
		//System.out.println("GAME INITING: ");
		gameFrame.setVisible(false);
		gameFrame.setSize(800,600);
		gameFrame.setVisible(true);

		currentLevel = 0;
		points = 0;
		lives = 10;
		viewport=new Rect(0,0,1000,800);

		world = new World(this);

		world.setBinaryInput(binIn);
		world.setViewport(viewport);
		//world.populate(currentLevel);
		scroll = new ScrollingScreen(this,screen,viewport,world);
		scroll.setBinaryInput(binIn);
		gameFrame.setContentPane(scroll);
		//gameFrame.setSize(800,600);

		//MainPage mainP = new MainPage();
		//WinPage winP = new WinPage();

		//BinaryInput binIn = new BinaryInput();

		//mainP.setBinaryInput(binIn);
		//winP.setBinaryInput(binIn);

		//scroll.setBinaryInput(binIn);


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

>>>>>>> .r159
		scroll.requestFocus();

		setState(NEXT_LEVEL);*/
	}

	/** Intermediate state that populates world and prepares game for the next level
	 *
	 **/
	private void updateNextLevelState() {
		//System.out.println("next level state");

		++currentLevel;
		levelSeed=System.currentTimeMillis();
		//clear the world of the extant objects, and repopulate
		world.populate(currentLevel);
		//pack();
		//validate();
		//repaint();
		//scroll.requestFocus();
		setState(GAME);
	}

	/** State machine, will continuously run (until esc/windowclose) and update whatever state its in.  States change the game's state by themselves
	 *
	 **/
	public void run()
	{
		long start=System.currentTimeMillis();

		while(true)
		{
			//System.out.println("KO: ");
			long curr=System.currentTimeMillis();
			long millis=curr-start;
			start=curr;
			switch(state) {
				case START_SCREEN:
					updateStartScreenState();
					break;
				case INIT_GAME:
					updateInitGameState();
					break;
				case GAME:
					updateGameState(millis);
					break;
				case NEXT_LEVEL:
					updateNextLevelState();
					break;
				case DIED_SEQUENCE:
					updateDiedSequenceState();
					break;
				case LOSS_SCREEN:
					updateLossScreen();
					break;
			}
			//System.out.println("State: "+state);
			try{
				Thread.sleep(15);
			}catch(Exception e){}
		}
	}
	private void updateLossScreen()
	{

		setState(START_SCREEN);
		gameFrame.setContentPane(startScreen);

	}
	/** Main game loop - updates world and draws it
	 *
	 **/
	private void updateGameState(long millis) {
		//System.out.println("GAME HERE: ");
		world.update(millis);
		//gameFrame.repaint();
		gameFrame.validate();
		//scroll.validate();
		//scroll.paintComponent(scroll.getGraphics());
		scroll.repaint();
/*
		while(true)
		{
			//System.out.println("KO: ");
			long curr=System.currentTimeMillis();
			long millis=curr-start;
			start=curr;
			world.update(millis);

			//repaint();
			//scroll.repaint();
			gameFrame.validate();
			//scroll.paint(scroll.getGraphics());
			//scroll.repaint();
			//gameFrame.repaint();
			try{
				Thread.sleep(1500);
			}catch(Exception e){}
		}
		*/
	}

	public void incrementLife() {
		++lives;
	}
	/** When you die, this state will update
	 *
	 **/
	private void updateDiedSequenceState() {

	//decrement lives

		--lives;
		if (lives < 0) {
			setState(LOSS_SCREEN);
			return;
		}
		Graphics2D g2d = (Graphics2D)gameFrame.getGraphics();
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
			gameFrame.repaint();
			try{
				Thread.sleep(15);
			}catch(Exception e){}
		}
		world.populate(currentLevel);
		setState(GAME);
	}
}
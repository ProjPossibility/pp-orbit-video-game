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
import java.awt.event.*;

/**
 * JPanel object for title screen.
 */
public class MainPage extends JPanel implements MouseListener, KeyListener
{	
	//images
	ImageIcon sceneT = new ImageIcon("media/sceneT.jpg");
	ImageIcon sceneT2 = new ImageIcon("media/sceneT2.jpg");
	
	/**
	 * Binary input object for main page.
	 */
	BinaryInput binaryInput=new BinaryInput();

	/**
	 * Prompt marker
	 */
	int prompt = 0; // set prompt for first page

	/**
	 * Reference to  the game
	 */
	Game game;

	/**
	 * Creates a main page object

	 * @param game Game object associated with main page.
	 */
	public MainPage(Game game)
	{
		this.game = game;
		addMouseListener(this);
		addKeyListener(this);
	}
	
	/**
	 * Binary input object can be set.
	 * 
	 * @param binInput The binary input object
	 */
	public void setBinaryInput(BinaryInput binIn)
	{
		binaryInput=binIn;
	}

	/** Override the paint method
	 *
	 **/
	public void paintComponent(Graphics g1)
	{
		super.paintComponent(g1);
		Graphics2D g=(Graphics2D)g1;
		if (prompt==1){
			g1.drawImage(sceneT2.getImage(),0,0,400,364,null);
			System.out.println("\nPrompt == 1\n");
			prompt=0;
			try {
				Thread.sleep(230);}
			catch(Exception e){	}
		}
		else{
			g1.drawImage(sceneT.getImage(),0,0,400,364,null);
			System.out.println("\nPrompt == 0\n");
			prompt=1;
			try {
				Thread.sleep(230);}
			catch(Exception e){	}
		}

	}

	///Whenever an event happens, update the binary input
	
	public void mousePressed(MouseEvent e)
	{
		if(binaryInput!=null) {
			binaryInput.buttonChanged(true);
			game.setState(Game.INIT_GAME);
		}
			requestFocus();
	}
	public void mouseEntered(MouseEvent e){}
	public void mouseReleased(MouseEvent e)
	{
		if(binaryInput!=null)
			binaryInput.buttonChanged(false);

	}
	public void mouseClicked(MouseEvent e){}
	public void mouseExited(MouseEvent e)
	{
		if(binaryInput!=null)
			binaryInput.buttonChanged(false);
	}

	public void keyPressed(KeyEvent e)
	{
		if(e.getKeyCode()==27)///27=esc, 32=space
			System.exit(0);

		if(binaryInput!=null) {
			binaryInput.buttonChanged(true);
			game.setState(Game.INIT_GAME);
		}
	}
	public void keyReleased(KeyEvent e)
	{
		if(binaryInput!=null)
			binaryInput.buttonChanged(false);
	}
	public void keyTyped(KeyEvent e){}
}
package orbit;


public class Game {
	
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
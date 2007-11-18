package orbit;

public class Explosion extends SpaceObject{

	public Explosion(Vector2 p, String sprite, double width, double height) {
		super(p, new Vector2(0,0), new Vector2(0,0), sprite, width, height);
		setAnimationProperties(20, 18, true);
		alive = true;
	}
	
	public void animate(int msSinceLastTime) {
		if(currentFrame!=-1) {
			super.nonLoopingAnimate(msSinceLastTime);
		} else {
			looping=false;
			alive = false;
		}
	}
}

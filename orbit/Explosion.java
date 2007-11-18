package orbit;

public class Explosion extends SpaceObject{

	public Explosion(Vector2 p, String sprite, double width, double height) {
		super(p, new Vector2(0,0), new Vector2(0,0), sprite, width, height);
	}
	
	public void animate(int msSinceLastTime) {
		if(currentFrame<numFrames) {
			super.animate(msSinceLastTime);
		} else {
			alive = false;
		}
	}
}

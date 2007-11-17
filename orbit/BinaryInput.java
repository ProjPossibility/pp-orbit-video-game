package orbit;

public class BinaryInput
{
	private boolean buttonOn;
	private int buttonState;
	private long timeMark;
	
	public BinaryInput()
	{
		buttonOn=false;
		buttonState=0;
		timeMark=System.currentTimeMillis();
	}
	
	public void buttonChanged(boolean on)
	{
		long current=System.currentTimeMillis();
		
		//if button was off before and now its pressed
		if(buttonState==0&&on)
		{
			buttonOn=true;
			buttonState=1;
			timeMark=current;
		}
		//if button was on and now its released
		else if(buttonState==1&&!on)
		{
			buttonOn=false;
			buttonState=0;
			timeMark=current;
		}
	}
	public int getButtonState()
	{
		return buttonState;
	}
}
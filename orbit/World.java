package orbit;

import java.util.ArrayList;

public class World
{
	private ArrayList<SpaceObject> spaceObjects;
	
	public World()
	{
		spaceObjects=new ArrayList<SpaceObject>(50);
	}
	public ArrayList<SpaceObject> getSpaceObjects()
	{
		return spaceObjects;
	}
}
package compositeObjectDomain;

import java.util.ArrayList;
import java.util.List;

public class MapToWallComp {
	
	public static Wall Map(List<AtomicObject> components)
	{
		int startX, startY, endX, endY;
		List<AtomicObject> doors = new ArrayList<AtomicObject>();
		startX = startY = endX = endY = -1;
		for(AtomicObject a: components)
		{
			int x = (Integer) a.get(CompObjDomain.VAR_X);
			int y = (Integer) a.get(CompObjDomain.VAR_Y);
			if(startX == -1)
			{
				startX = x;
				startY = y;
				endX = x;
				endY = y;
			}
			else{
				if(startX == x)
				{
					if(startY > y)
					{
						startY = y;
					}
				}
				else if(startX > x)
				{
					startX = x;
					startY = y;
				}
				
				if(endX == x)
				{
					if(endY < y)
					{
						endY = y;
					}
				}
				else if(endX < x)
				{
					endX = x;
					endY = y;
				}
			}
			
			if(a.className() == "Door")
			{
				doors.add(a);
			}
		}
		return new Wall(startX, startY, endX, endY, components.size(), doors, "wall");
	}

}

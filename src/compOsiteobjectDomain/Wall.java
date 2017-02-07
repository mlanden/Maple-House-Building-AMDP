package compOsiteobjectDomain;

import java.util.ArrayList;
import java.util.List;

public class Wall {
	
	private List<AtomicObject> doors = new ArrayList<AtomicObject>();
	private int startX, startY, endX, endY, length;
	
	public Wall()
	{
		startX = -1;
		startY = -1;
		endX = -1;
		endY = -1;
	}
	
	public Wall(int startX, int startY, int endX, int endY, int length)
	{
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.length = length;
	}
	
	public Wall(int startX, int startY, int endX, int endY, int length, List<AtomicObject> doors)
	{
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.doors = doors;
		this.length = length;
	}
	
	public int getStartX()
	{
		return startX;
	}
	
	public int getStartY()
	{
		return startY;
	}
	
	public int getEndX()
	{
		return endX;
	}
	
	public int getEndY()
	{
		return endY;
	}
	
	public int length()
	{
		return length;
	}
	
	public List<AtomicObject> getDoors()
	{
		return doors;
	}
	
	public void setStartX(int x)
	{
		startX = x;
	}
	
	public void setStartY(int y)
	{
		startY = y;
	}
	
	public void setEndX(int x)
	{
		endX = x;
	}
	
	public void setEndY(int y)
	{
		endY = y;
	}
	
	public void setDoors(List<AtomicObject> doors)
	{
		this.doors = doors;
	}
	
	public boolean equals(Object o)
	{
		Wall w = (Wall)o;
		if(w.getStartX() == startX && w.getEndX() == endX 
				&& w.getStartY() == startY && w.getEndY() == endY)
			return true;
		return false;
	}

	public Wall copy() {
		// TODO Auto-generated method stub
		return new Wall(startX, startY, endX, endY, length, doors);
	}

}

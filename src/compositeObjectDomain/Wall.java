package compositeObjectDomain;

import java.util.ArrayList; 
import java.util.Arrays;
import java.util.List;

import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.UnknownKeyException;

import static amdp.houseBuilding.level1.domain.L1DomainGenerator.CLASS_WALL;
import static amdp.houseBuilding.level1.domain.L1DomainGenerator.DOORS;
import static amdp.houseBuilding.level1.domain.L1DomainGenerator.START_X;
import static amdp.houseBuilding.level1.domain.L1DomainGenerator.START_Y;
import static amdp.houseBuilding.level1.domain.L1DomainGenerator.END_X;
import static amdp.houseBuilding.level1.domain.L1DomainGenerator.END_Y;
import static amdp.houseBuilding.level1.domain.L1DomainGenerator.LENGTH;

public class Wall implements ObjectInstance{
	
	public List<AtomicObject> doors = new ArrayList<AtomicObject>();
	public int startX, startY, endX, endY, length;
	protected String name;
	private static final List<Object> keys = Arrays.<Object>asList
			(DOORS, START_X, START_Y, END_X, END_Y, LENGTH);
	
	public Wall()
	{
		startX = -1;
		startY = -1;
		endX = -1;
		endY = -1;
	}
	
	public Wall(int startX, int startY, int endX, int endY, int length, String name)
	{
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.length = length;
		this.name = name;
	}
	
	public Wall(int startX, int startY, int endX, int endY, int length, List<AtomicObject> doors, String name)
	{
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.doors = doors;
		this.length = length;
		this.name = name;
	}
		
	public boolean equals(Object o)
	{
		Wall w = (Wall)o;
		if((int) (w.get(START_X)) == startX 
				&& (int) (w.get(END_X)) == endX 
				&& (int) (w.get(START_Y)) == startY && (int) (w.get(END_Y)) == endY)
			return true;
		return false;
	}

	public Wall copy() {
		return new Wall(startX, startY, endX, endY, length, doors, name);
	}

	public Object get(Object variableKey) {
		if(variableKey.equals(DOORS)){
			return doors;
		}else if(variableKey.equals(START_X)){
			return startX;
		}else if(variableKey.equals(START_Y)){
			return startY;
		}else if(variableKey.equals(END_X)){
			return endX;
		}else if(variableKey.equals(END_Y)){
			return endY;
		}else if(variableKey.equals(LENGTH)){
			return length;
		}
		
		throw new UnknownKeyException(variableKey);
	}

	public List<Object> variableKeys() {
		return keys;
	}

	public String className() {
		return CLASS_WALL;
	}

	public Wall copyWithName(String newName) {
		return new Wall(startX, startY, endX, endY, length, newName);
	}

	
	public String name(){
		return name;
	}
	
	
}

package compositeObjectDomain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import burlap.mdp.core.oo.state.OOStateUtilities;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.State;

public class CompObjAgent implements ObjectInstance{
	
	public int x, y;
	protected String name;
	protected ArrayList<AtomicObject> selection;
	protected ArrayList<Wall> walls;
	private final static List<Object> keys = Arrays.<Object>asList(CompObjDomainGenerator.VAR_X, CompObjDomainGenerator.VAR_Y);
	
	public CompObjAgent()
	{
		name = "agent";
		selection = new ArrayList<AtomicObject>();
		walls = new ArrayList<Wall>();
	}
	
	public CompObjAgent(int x, int y)
	{
		this();
		this.x = x;
		this.y = y;
	}
	
	public CompObjAgent(int x, int y, ArrayList<Wall> walls)
	{
		this(x, y);
		this.walls = walls;
	}
	
	public CompObjAgent(int x, int y, String name, ArrayList<Wall> walls)
	{
		this(x, y, name);
		this.walls = walls;
	}
	
	public CompObjAgent(int x, int y, String name)
	{
		this(x, y);
		this.name = name;
	}
	

	@Override
	public List<Object> variableKeys() {
		return keys;
	}

	@Override
	public Object get(Object variableKey) {
		if(!(variableKey instanceof String)){
			throw new RuntimeException("GridAgent variable key must be a string");
		}
		String key = (String)variableKey;
		if(key.equals(CompObjDomainGenerator.VAR_X)){
			return x;
		}
		else if(key.equals(CompObjDomainGenerator.VAR_Y)){
			return y;
		}
		else if(key.equals("Walls"))
			return walls;
		else if(key.equals("selection"))
			return selection;

		throw new RuntimeException("Unknown key " + key);
	}

	@Override
	public CompObjAgent copy() {
		ArrayList<Wall> wallsCopy = new ArrayList<Wall>();
		for(Wall w: walls)
		{
			wallsCopy.add(w.copy());
		}
		return new CompObjAgent(x, y, wallsCopy);
	}

	@Override
	public String className() {
		return CompObjDomainGenerator.CLASS_AGENT;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public CompObjAgent copyWithName(String objectName) {
		ArrayList<Wall> wallsCopy = new ArrayList<Wall>();
		for(Wall w: walls)
		{
			wallsCopy.add(w.copy());
		}
		return new CompObjAgent(x, y, name, wallsCopy);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void addWall(Wall w)
	{
		walls.add(w);
	}
	
	public void clearWalls()
	{
		walls.clear();
	}
	
	public ArrayList<AtomicObject> getSelection()
	{
		return selection;
	}
	
	public void setSelection(ArrayList<AtomicObject> selection)
	{
		this.selection = selection;
	}

	@Override
	public String toString() {
		return OOStateUtilities.objectInstanceToString(this);
	}
	
	public void map(List<AtomicObject> selection)
	{

		Wall addition =  MapToWallComp.Map(selection);
		
		for(Wall w:walls)
		{
			if(w.equals(addition))
				return;
		}
		
		walls.add(addition);
	}



}

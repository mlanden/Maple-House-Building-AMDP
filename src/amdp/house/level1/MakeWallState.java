package amdp.house.level1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import amdp.house.objects.HAgent;
import amdp.house.objects.HBlock;
import amdp.house.objects.HWall;
import burlap.mdp.core.oo.state.MutableOOState;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.MutableState;
import utils.IntPair;

public class MakeWallState implements MutableOOState{

	public static IntPair pair = new IntPair(0, 0);
	
	private HAgent agent;
	private Map<IntPair, HBlock> blocks = new HashMap<IntPair, HBlock>();
	private List<HWall> walls;
	private int width;
	private int height;
	
	public MakeWallState(int width, int height, int agentX, int agentY) {
		this.width = width;
		this.height = height;
		agent = new HAgent(agentX, agentY);
		blocks = new HashMap<IntPair, HBlock> ();
		walls = new ArrayList<HWall>();
	}
	
	public MakeWallState(int width, int height, HAgent agent, Map<IntPair, HBlock> blocks, List<HWall> walls) {
		this.width = width;
		this.height = height;
		this.agent = agent;
		this.blocks = blocks;
		this.walls = walls;
	}
	
	// assumes that 0,0 is the lower left corner
	public boolean isOutOfBounds(int nx, int ny) {
		return nx < 0 || nx >= width || ny < 0 || ny >= height;
	}
	
	public boolean isOpen(int nx, int ny) {
		return !(isOutOfBounds(nx, ny) || isBlocked(nx, ny));
	}

	public boolean isBlocked(int nx, int ny) {
		return blockAt(nx, ny);
//		return blockAt(nx, ny) || doorAt(nx, ny);
	}

	public HBlock getBlockAt(int nx, int ny) {
		pair.x = nx;
		pair.y = ny;
		HBlock found = blocks.get(pair);
		return found;
	}
	
	public boolean blockAt(int nx, int ny) {
		return getBlockAt(nx, ny) != null;
	}
	
//	public boolean doorAt(int nx, int ny) {
//		return hasObjectInListAt(nx, ny, doors);
//	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public HAgent getAgent() {
		return agent;
	}
	
	public int numObjects() {
		int numObjects = 0;
		numObjects += agent != null ? 1 : 0;
		numObjects += blocks.size();
		numObjects += walls.size();
		return numObjects;
	}
	
	public ObjectInstance object(String oname) {
		for (ObjectInstance object : objects()) {
			if (oname.equals(object.name())) {
				return object;
			}
		}
		throw new RuntimeException("not implemented " + oname);
	}
	
	public List<ObjectInstance> objects() {
		List<ObjectInstance> objects = new ArrayList<ObjectInstance>();
		objects.add(agent);
		objects.addAll(blocks.values());
		objects.addAll(walls);
		return objects;
	}
	
	public List<ObjectInstance> objectsOfClass(String oclass) {
		if(HAgent.CLASS_AGENT.equals(oclass)) {
			return Arrays.<ObjectInstance>asList(agent);
		} else if(HBlock.CLASS_BLOCK.equals(oclass)) {
			return new ArrayList<ObjectInstance>(blocks.values());
		} else if(HWall.CLASS_WALL.equals(oclass)) {
			return new ArrayList<ObjectInstance>(walls);
		} else {
			throw new RuntimeException("not implemented");
		}
	}
	
	public int getNumWalls() {
		return walls.size();
	}

	public List<HWall> touchWalls() {
    	this.walls = new ArrayList<HWall>(this.walls);
    	return this.walls;
	}
	
	public HWall touchWall(int index) {
    	HWall copy = (HWall) walls.get(index).copy();
    	touchWalls().remove(index);
    	walls.add(index, copy);
    	return copy;
	}
	
	public int getNumBlocks() {
		return blocks.size();
	}

	public Map<IntPair, HBlock> touchBlocks() {
    	this.blocks = new HashMap<IntPair, HBlock>(this.blocks);
    	return this.blocks;
	}
	
	public HBlock touchBlock(int nx, int ny) {
		IntPair key = new IntPair(nx, ny);
		HBlock copy = (HBlock) blocks.get(key).copy();
		touchBlocks().remove(key);
		blocks.put(key, copy);
		key = null;
		return copy;
	} 
	
	public HAgent touchAgent() {
		this.agent = (HAgent) agent.copy();
		return this.agent;
	}

	public MakeWallState copy() {
		return new MakeWallState(width, height, touchAgent(), touchBlocks(), touchWalls());
	}

	@Override
	public List<Object> variableKeys() {
		throw new RuntimeException("not implemented");
	}

	@Override
	public Object get(Object variableKey) {
		throw new RuntimeException("not implemented");
	}

	@Override
	public MutableState set(Object variableKey, Object value) {
		throw new RuntimeException("not implemented");
	}

	@Override
	public MutableOOState addObject(ObjectInstance o) {
		if (o instanceof HBlock) {
			HBlock block = (HBlock)o;
			int x = (Integer) block.get(HBlock.ATT_X);
			int y = (Integer) block.get(HBlock.ATT_Y);
			IntPair key = new IntPair(x, y);
			touchBlocks().put(key, block);
		} else if (o instanceof HWall) {
			List<HWall> walls = this.touchWalls();
			walls.add((HWall)o);
			this.walls = walls;
		} else {
			throw new RuntimeException("not implemented");
		}
		return this;
	}

	@Override
	public MutableOOState removeObject(String oname) {
		throw new RuntimeException("not implemented");
	}

	@Override
	public MutableOOState renameObject(String objectName, String newName) {
		throw new RuntimeException("not implemented");
	}

	public HWall getFirstWall() {
		if (walls.size() < 1) {
			return null;
		}
		return walls.get(0);
	}
	
}

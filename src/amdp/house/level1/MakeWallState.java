package amdp.house.level1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import amdp.house.objects.HAgent;
import amdp.house.objects.HBlock;
import amdp.house.objects.HPoint;
import amdp.house.objects.HWall;
import burlap.mdp.core.oo.state.MutableOOState;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.MutableState;
import utils.IntPair;

public class MakeWallState implements MutableOOState{

	public static IntPair pair = new IntPair(0, 0);
	
	private HAgent agent;
	private Map<IntPair, HPoint> points;
	private Map<IntPair, HBlock> blocks;
	private List<HWall> walls;
	private int width;
	private int height;
	
	// empty state
	public MakeWallState(int width, int height, int agentX, int agentY) {
		this.width = width;
		this.height = height;
		agent = new HAgent(agentX, agentY);
		points = new HashMap<IntPair, HPoint>();
		blocks = new HashMap<IntPair, HBlock> ();
		walls = new ArrayList<HWall>();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				String name = HPoint.CLASS_POINT+"_"+i+"_"+j;
				addObject(new HPoint(name, i, j, false));
			}
		}
	}
	
	// copy constructor
	public MakeWallState(int width, int height, HAgent agent, Map<IntPair, HBlock> blocks, List<HWall> walls, Map<IntPair,HPoint> points) {
		this.width = width;
		this.height = height;
		this.agent = agent;
		this.blocks = blocks;
		this.walls = walls;
		this.points = points;
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
	
	public HPoint getPointAt(int nx, int ny) {
		pair.x = nx;
		pair.y = ny;
		HPoint found = points.get(pair);
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
		numObjects += points.size();
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
		objects.addAll(points.values());
		objects.addAll(blocks.values());
		objects.addAll(walls);
		return objects;
	}
	
	public List<ObjectInstance> objectsOfClass(String oclass) {
		if(HAgent.CLASS_AGENT.equals(oclass)) {
			return Arrays.<ObjectInstance>asList(agent);
		} else if(HBlock.CLASS_BLOCK.equals(oclass)) {
			return new ArrayList<ObjectInstance>(blocks.values());
		} else if(HPoint.CLASS_POINT.equals(oclass)) {
			return new ArrayList<ObjectInstance>(points.values());
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
	
	public Map<IntPair, HPoint> touchPoints() {
    	this.points = new HashMap<IntPair,HPoint>(this.points);
    	return this.points;
	}
	
	public HPoint touchPoint(int nx, int ny) {
		IntPair key = new IntPair(nx, ny);
		HPoint copy = (HPoint) points.get(key).copy();
		touchPoints().remove(key);
		points.put(key, copy);
		key = null;
    	return copy;
	}

	public HAgent touchAgent() {
		this.agent = (HAgent) agent.copy();
		return this.agent;
	}

	public MakeWallState copy() {
		return new MakeWallState(width, height, touchAgent(), touchBlocks(), touchWalls(), touchPoints());
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
		} else if (o instanceof HPoint) {
			HPoint point = (HPoint)o;
			int x = (Integer) point.get(HPoint.ATT_X);
			int y = (Integer) point.get(HPoint.ATT_Y);
			IntPair key = new IntPair(x, y);
			touchPoints().put(key, point);
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

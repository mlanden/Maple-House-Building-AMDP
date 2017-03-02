package amdp.house.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import amdp.house.objects.HAgent;
import amdp.house.objects.HBlock;
import amdp.house.objects.HPoint;
import amdp.house.objects.HRoom;
import amdp.house.objects.HWall;
import burlap.mdp.core.oo.state.MutableOOState;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.MutableState;
import utils.IntPair;

public class HouseBaseState implements MutableOOState {

	public static IntPair pair = new IntPair(0, 0);
	
	protected HAgent agent;
	protected Map<IntPair, HPoint> points;
	protected Map<IntPair, HBlock> blocks;
	protected List<HWall> walls;
	protected List<HRoom> rooms;
	
	protected HRoom goal;
	
	protected int width;
	protected int height;
	
	public HouseBaseState(int width, int height, HAgent agent, HRoom goal) {
		this.width = width;
		this.height = height;
		this.agent = agent;
		points = new HashMap<IntPair, HPoint>();
		blocks = new HashMap<IntPair, HBlock>();
		walls = new ArrayList<HWall>();
		rooms = new ArrayList<HRoom>();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				String name = HPoint.CLASS_POINT+"_"+i+"_"+j;
				addObject(new HPoint(name, i, j, false));
			}
		}
		this.goal = goal;
	}
	
	// copy constructor
	public HouseBaseState(int width, int height, HAgent agent, Map<IntPair,HPoint> points, Map<IntPair, HBlock> blocks,
			List<HWall> walls, List<HRoom> rooms, HRoom goal) {
		this.width = width;
		this.height = height;
		this.agent = agent;
		this.points = points != null ? points : new HashMap<IntPair,HPoint>();
		this.blocks = blocks != null ? blocks : new HashMap<IntPair,HBlock>();
		this.walls = walls != null ? walls : new ArrayList<HWall>();
		this.rooms = rooms != null ? rooms : new ArrayList<HRoom>();
		this.goal = goal;
	}
	
	public HouseBaseState(int width, int height, int agentX, int agentY) {
		this(width, height, new HAgent(agentX, agentY), null);
	}

	public HouseBaseState(int width, int height) {
		this(width, height, null, null);
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
	
	public Map<IntPair, HPoint> getPoints() {
		return points;
	}

	public Map<IntPair, HBlock> getBlocks() {
		return blocks;
	}

	public List<HWall> getWalls() {
		return walls;
	}

	public List<HRoom> getRooms() {
		return rooms;
	}
	
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
		numObjects += rooms.size();
		numObjects += goal != null ? 1 : 0;
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
		if (agent != null) { objects.add(agent); }
		objects.addAll(points.values());
		objects.addAll(blocks.values());
		objects.addAll(walls);
		objects.addAll(rooms);
		if (goal != null) { objects.add(goal); }
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
		} else if(HRoom.CLASS_ROOM.equals(oclass)) {
			return new ArrayList<ObjectInstance>(rooms);
		} else {
			throw new RuntimeException("not implemented");
		}
	}
	
//	public int getNumWalls() {
//		return walls.size();
//	}

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
	
	public List<HRoom> touchRooms() {
    	this.rooms = new ArrayList<HRoom>(this.rooms);
    	return this.rooms;
	}
	
	public HRoom touchRoom(int index) {
    	HRoom copy = (HRoom) rooms.get(index).copy();
    	touchRooms().remove(index);
    	rooms.add(index, copy);
    	return copy;
	}
	
	public HRoom touchGoal() {
		this.goal = (HRoom) this.goal.copy();
		return this.goal;
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

	public HouseBaseState copy() {
//		return new HouseBaseState(width, height, touchAgent(), touchBlocks(), touchWall(), touchPoints());
		return new HouseBaseState(width, height, touchAgent(), touchPoints(), touchBlocks(), touchWalls(), touchRooms(), touchGoal());
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
		} else if (o instanceof HRoom) {
			List<HRoom> rooms = this.touchRooms();
			rooms.add((HRoom)o);
			this.rooms = rooms;
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
	
	@Override
	public String toString() {
		String out = "";
		out += "points: " + points.size() + ", ";

		out += "blocks: " + blocks.size() + ", ";

		out += "walls: " + walls.size() + ", ";

		out += "rooms: " + rooms.size() + ", ";

		return out;
	}

	public HRoom getGoal() {
		return goal;
	}
	
}
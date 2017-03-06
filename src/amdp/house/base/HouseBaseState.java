package amdp.house.base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import amdp.house.objects.HAgent;
import amdp.house.objects.HBlock;
import amdp.house.objects.HHouse;
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
	
	protected HHouse goalHouse;
//	protected HRoom goalRoom;
//	protected HWall goalWall;
//	protected HBlock goalBlock;
	
	protected int width;
	protected int height;
	
	public HouseBaseState(int width, int height, HAgent agent, HHouse goalHouse) {
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
		this.goalHouse = goalHouse;
//		this.goalRoom = goalRoom;
//		this.goalWall = null;
//		this.goalBlock = null;
	}
	
	// copy constructor
	public HouseBaseState(int width, int height, HAgent agent, Map<IntPair,HPoint> points, Map<IntPair, HBlock> blocks,
			List<HWall> walls, List<HRoom> rooms, HHouse goalHouse) {
//			HRoom goalRoom, HWall goalWall, HBlock goalBlock) {
		this.width = width;
		this.height = height;
		this.agent = agent;
		this.points = points != null ? points : new HashMap<IntPair,HPoint>();
		this.blocks = blocks != null ? blocks : new HashMap<IntPair,HBlock>();
		this.walls = walls != null ? walls : new ArrayList<HWall>();
		this.rooms = rooms != null ? rooms : new ArrayList<HRoom>();
		this.goalHouse = goalHouse;
//		this.goalRoom = goalRoom;
//		this.goalWall = goalWall;
//		this.goalBlock = goalBlock;
	}
	
	public HouseBaseState(int width, int height, int agentX, int agentY) {
		this(width, height, new HAgent(agentX, agentY), null);
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
	
	public boolean blockAt(HPoint corner) {
		int nx = (int) corner.get(HPoint.ATT_X);
		int ny = (int) corner.get(HPoint.ATT_Y);
		return blockAt(nx, ny);
	}
	
	public Map<IntPair, HPoint> getPoints() {
		return points;
	}

	public Map<IntPair, HBlock> getBlocks() {
		return blocks;
	}
	
	public List<HBlock> getBlocksList() {
		return new ArrayList<HBlock>(blocks.values());
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
		numObjects += goalHouse != null ? 1 : 0;
//		numObjects += goalRoom != null ? 1 : 0;
//		numObjects += goalWall != null ? 1 : 0;
//		numObjects += goalBlock != null ? 1 : 0;
		return numObjects;
	}
	
	public ObjectInstance object(String oname) {
		for (ObjectInstance object : objects()) {
			if (oname.equals(object.name())) {
				return object;
			}
		}
		return null;
//		throw new RuntimeException("not implemented " + oname);
	}
	
	public List<ObjectInstance> objects() {
		List<ObjectInstance> objects = new ArrayList<ObjectInstance>();
		if (agent != null) { objects.add(agent); }
		objects.addAll(points.values());
		objects.addAll(blocks.values());
		objects.addAll(walls);
		objects.addAll(rooms);
		if (goalHouse != null) { objects.add(goalHouse); }
//		if (goalRoom != null) { objects.add(goalRoom); }
//		if (goalWall != null) { objects.add(goalWall); }
//		if (goalBlock != null) { objects.add(goalBlock); }
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
		} else if(HHouse.CLASS_HOUSE.equals(oclass)) {
			return Arrays.<ObjectInstance>asList(goalHouse);
		} else {
			throw new RuntimeException("not implemented");
		}
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
	
	public HHouse touchGoalHouse() {
		if (this.goalHouse == null) { return null; }
		this.goalHouse = (HHouse) this.goalHouse.copy();
		return this.goalHouse;
	}

//	public HRoom touchGoalRoom() {
//		if (this.goalRoom == null) { return null; }
//		this.goalRoom = (HRoom) this.goalRoom.copy();
//		return this.goalRoom;
//	}
//	
//	public HWall touchGoalWall() {
//		if (this.goalWall == null) { return null; }
//		this.goalWall = (HWall) this.goalWall.copy();
//		return this.goalWall;
//	}
//	
//	public HBlock touchGoalBlock() {
//		if (this.goalBlock == null) { return null; }
//		this.goalBlock = (HBlock) this.goalBlock.copy();
//		return this.goalBlock;
//	}
	
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
		if (this.agent == null) { return null; }
		this.agent = (HAgent) agent.copy();
		return this.agent;
	}

	public HouseBaseState copy() {
//		return new HouseBaseState(width, height, touchAgent(), touchBlocks(), touchWall(), touchPoints());
		return new HouseBaseState(width, height,
				touchAgent(),
				touchPoints(),
				touchBlocks(),
				touchWalls(),
				touchRooms(),
				touchGoalHouse()
//				touchGoalRoom(),
//				touchGoalWall(),
//				touchGoalBlock()
		);
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
		out += this.getClass().getSimpleName();
		out += ": {";
		out += agent != null ? "agent at (" + agent.get(HAgent.ATT_X) + ", " + agent.get(HAgent.ATT_Y) + "), " : "";
		out += "points: " + points.size() + ", ";
		out += "blocks: " + blocks.size() + ", ";
		out += "walls: " + walls.size() + ", ";
		out += "rooms: " + rooms.size() + ", ";
		out += "house: " + (goalHouse != null ? 1 : 0);
		out += "}";
		return out;
	}

	public HHouse getGoalHouse() {
		return goalHouse;
	}

//	public HRoom getGoalRoom() {
//		return goalRoom;
//	}
//	
//	public HWall getGoalWall() {
//		return goalWall;
//	}
//	
//	public HBlock getGoalBlock() {
//		return goalBlock;
//	}
	
}

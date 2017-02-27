package amdp.house.level2;

import java.util.ArrayList;
import java.util.List;

import amdp.house.objects.HPoint;
import amdp.house.objects.HWall;
import burlap.mdp.core.oo.state.MutableOOState;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.MutableState;
import burlap.mdp.core.state.State;

public class MakeRoomState implements MutableOOState {
	
	private int width;
	private int height;
	private List<HPoint> points;
	private List<HWall> walls;
	
	public MakeRoomState(int width, int height) {
		this.width = width;
		this.height = height;
		walls = new ArrayList<HWall>();
		points = new ArrayList<HPoint>();
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				String name = HPoint.CLASS_POINT+"_"+i+"_"+j;
				addObject(new HPoint(name, i, j, false));
			}
		}
	}
	
	public MakeRoomState(int width, int height, List<HPoint> points, List<HWall> walls) {
		this.width = width;
		this.height = height;
		this.points = points;
		this.walls = walls;
	}
	
	@Override
	public int numObjects() {
		int numObjects = 0;
		numObjects += points.size();
		numObjects += walls.size();
		return numObjects;
	}

	@Override
	public ObjectInstance object(String oname) {
		for (ObjectInstance object : objects()) {
			if (oname.equals(object.name())) {
				return object;
			}
		}
		throw new RuntimeException("not implemented");
	}

	@Override
	public List<ObjectInstance> objects() {
		List<ObjectInstance> objects = new ArrayList<ObjectInstance>();
		objects.addAll(points);
		objects.addAll(walls);
		return objects;
	}

	@Override
	public List<ObjectInstance> objectsOfClass(String oclass) {
		if(HPoint.CLASS_POINT.equals(oclass)) {
			return new ArrayList<ObjectInstance>(points);
		} else if(HWall.CLASS_WALL.equals(oclass)) {
			return new ArrayList<ObjectInstance>(walls);
		} else {
			throw new RuntimeException("not implemented");
		}
	}

	@Override
	public List<Object> variableKeys() {
		// TODO Auto-generated method stub
		throw new RuntimeException("not implemented");
	}

	@Override
	public Object get(Object variableKey) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not implemented");
	}

	@Override
	public State copy() {
		return new MakeRoomState(width, height, touchPoints(), touchWalls());
	}

	@Override
	public MutableState set(Object variableKey, Object value) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not implemented");
	}

	@Override
	public MutableOOState addObject(ObjectInstance o) {
		if (o instanceof HPoint) {
			List<HPoint> points = this.touchPoints();
			points.add((HPoint)o);
			this.points = points;
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
		// TODO Auto-generated method stub
		throw new RuntimeException("not implemented");
	}

	@Override
	public MutableOOState renameObject(String objectName, String newName) {
		// TODO Auto-generated method stub
		throw new RuntimeException("not implemented");
	}

	public boolean hasWall(HWall wall) {
		HPoint start = (HPoint) wall.get(HWall.ATT_START);
		HPoint end = (HPoint) wall.get(HWall.ATT_END);
		for (HWall other : walls) {
			HPoint oStart = (HPoint) other.get(HWall.ATT_START);
			HPoint oEnd = (HPoint) other.get(HWall.ATT_END);
			if (start.compareTo(oStart) == 0
			 && end.compareTo(oEnd) == 0) {
				return true;
			}
		}
		return false;
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
	
	public List<HPoint> touchPoints() {
    	this.points = new ArrayList<HPoint>(this.points);
    	return this.points;
	}
	
	public HPoint touchPoint(int index) {
    	HPoint copy = (HPoint) points.get(index).copy();
    	touchPoints().remove(index);
    	points.add(index, copy);
    	return copy;
	}
	
	public String toString() {
		String out = "MakeRoomState, has walls: ";
		out += walls.size();
		return out;
	}

}

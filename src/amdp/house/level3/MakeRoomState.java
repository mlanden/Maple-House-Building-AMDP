package amdp.house.level3;

import java.util.List;
import java.util.Map;

import amdp.house.base.HouseBaseState;
import amdp.house.objects.HPoint;
import amdp.house.objects.HRoom;
import amdp.house.objects.HWall;
import burlap.mdp.core.oo.state.ObjectInstance;
import utils.IntPair;

public class MakeRoomState extends HouseBaseState {
	
	private HRoom room;
	
	public MakeRoomState(int width, int height, HRoom room) {
		super(width, height, null, null);
		this.room = room;
	}
	
	public MakeRoomState(int width, int height, Map<IntPair, HPoint> points, List<HWall> walls, HRoom room) {
		super(width, height, null, points, null, walls, null, null);
		this.room = room;
	}
	
	public MakeRoomState(HouseBaseState state) {
		super(state.getWidth(), state.getHeight(), state.getAgent(), state.getPoints(), state.getBlocks(), state.getWalls(), state.getRooms(), state.getGoal());
		this.room = goal;
	}

	@Override
	public int numObjects() {
		int numObjects = super.numObjects();
		numObjects += this.room != null ? 1 : 0;
		return numObjects;
	}

	@Override
	public List<ObjectInstance> objects() {
		List<ObjectInstance> objects = super.objects();
		if (room != null) { objects.add(room); }
		return objects;
	}

	@Override
	public MakeRoomState copy() {
		return new MakeRoomState(width, height, touchPoints(), touchWalls(), touchRoom());
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
	
	public HRoom touchRoom() {
		this.room = (HRoom) this.room.copy();
		return this.room;
	}
	
	public String toString() {
		String out = "MakeRoomState, has walls: ";
		out += walls.size();
		return out;
	}

	public HRoom getRoom() {
		return room;
	}

	public boolean isWallCorner(HPoint corner) {
		for (HWall wall : walls) {
			HPoint start = (HPoint) wall.get(HWall.ATT_START);
			HPoint end = (HPoint) wall.get(HWall.ATT_END);
			if (start.compareTo(corner) == 0 || end.compareTo(corner) == 0) {
				return true;
			}
		}
		return false;
	}

}

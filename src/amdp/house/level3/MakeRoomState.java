package amdp.house.level3;

import java.util.List;
import java.util.Map;

import amdp.house.base.HouseBaseState;
import amdp.house.objects.HAgent;
import amdp.house.objects.HBlock;
import amdp.house.objects.HPoint;
import amdp.house.objects.HRoom;
import amdp.house.objects.HWall;
import utils.DynamicGroundedPropSC;
import utils.IntPair;

public class MakeRoomState extends HouseBaseState {
	
	public MakeRoomState(int width, int height, HRoom goalRoom) {
		super(width, height, null, null);
		this.goalRoom = goalRoom;
	}

	public MakeRoomState(int width, int height, HAgent agent, Map<IntPair, HPoint> points, Map<IntPair, HBlock> blocks,
			List<HWall> walls, List<HRoom> rooms, HRoom goalRoom, HWall goalWall, HBlock goalBlock) {
		super(width, height, agent, points, blocks, walls, rooms, goalRoom, goalWall, goalBlock);
	}
	
	public MakeRoomState(HouseBaseState state) {
		super(state.getWidth(), state.getHeight(), state.getAgent(), state.getPoints(), state.getBlocks(), state.getWalls(),
				state.getRooms(), state.getGoalRoom(), state.getGoalWall(), state.getGoalBlock());
	}

//	@Override
//	public int numObjects() {
//		int numObjects = super.numObjects();
//		numObjects += this.goalRoom != null ? 1 : 0;
//		return numObjects;
//	}
//
//	@Override
//	public List<ObjectInstance> objects() {
//		List<ObjectInstance> objects = super.objects();
//		if (goalRoom != null) { objects.add(goalRoom); }
//		return objects;
//	}


	@Override
	public MakeRoomState copy() {
		return new MakeRoomState(width, height, touchAgent(), touchPoints(), touchBlocks(), touchWalls(), touchRooms(), touchGoalRoom(),
				touchGoalWall(), touchGoalBlock());
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
		this.goalRoom = (HRoom) this.goalRoom.copy();
		return this.goalRoom;
	}
	
	public String toString() {
		String out = "MakeRoomState, has walls: ";
		out += walls.size();
		return out;
	}

	public HRoom getRoom() {
		return goalRoom;
	}

	public boolean isWallCorner(HPoint corner) {
//		return blockAt(corner);
		for (HWall wall : walls) {
			HPoint start = (HPoint) wall.get(HWall.ATT_START);
			HPoint end = (HPoint) wall.get(HWall.ATT_END);
			if (start.compareTo(corner) == 0 || end.compareTo(corner) == 0) {
				return true;
			}
		}
		
		if(blockAt(corner)) {
//			System.err.println("BLOCK PRESENT");
//	        try {
//				Thread.sleep(100);
//			} catch (InterruptedException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
			return true;
		}
		
		return false;
	}

	public boolean areContiguousInLine(HPoint start, HPoint end) {
		
		/// WARNING:
		// using the if/else below is a really bad hack to circumvent issue of state projection for composite objects
//		if (walls.size() > 0) {
		for (HWall wall : walls) {
			HPoint wallStart = (HPoint) wall.get(HWall.ATT_START);
			HPoint wallEnd = (HPoint) wall.get(HWall.ATT_END);
			if ((start.compareTo(wallStart) == 0 && end.compareTo(wallEnd) == 0)
			 || (start.compareTo(wallEnd) == 0 && end.compareTo(wallStart) == 0)) {
				return true;
			}
		}
		return false;
//		}
//		else {
//			HasFinishedWall hasFinishedWall = new HasFinishedWall();
//			int aX = (int) start.get(HPoint.ATT_X);
//			int aY = (int) start.get(HPoint.ATT_Y);
//			int bX = (int) end.get(HPoint.ATT_X);
//			int bY = (int) end.get(HPoint.ATT_Y);
//			boolean wallExists = hasFinishedWall.checkLineMinimal(this, aX, aY, bX, bY);
//			return wallExists;
//		}
	}

	public int wallsRemaining() {
		return HasFinishedRoom.getNumWallsRemaining(this, goalRoom);
	}

}

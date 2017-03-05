package amdp.house.level3;

import java.util.List;
import java.util.Map;

import amdp.amdpframework.GroundedPropSC;
import amdp.house.base.HouseBaseState;
import amdp.house.level2.HasGoalWallPF;
import amdp.house.objects.HAgent;
import amdp.house.objects.HBlock;
import amdp.house.objects.HHouse;
import amdp.house.objects.HPoint;
import amdp.house.objects.HRoom;
import amdp.house.objects.HWall;
import burlap.mdp.core.oo.propositional.GroundedProp;
import utils.IntPair;

public class MakeRoomState extends HouseBaseState {
	
	public MakeRoomState(int width, int height) {
		super(width, height, null, null);
	}

	public MakeRoomState(int width, int height, HAgent agent, Map<IntPair, HPoint> points, Map<IntPair, HBlock> blocks,
			List<HWall> walls, List<HRoom> rooms, HHouse goalHouse) {
			//HRoom goalRoom, HWall goalWall, HBlock goalBlock) {
		super(width, height, agent, points, blocks, walls, rooms, goalHouse);//, goalRoom, goalWall, goalBlock);
	}
	
	public MakeRoomState(HouseBaseState state) {
		super(state.getWidth(), state.getHeight(), state.getAgent(), state.getPoints(), state.getBlocks(), state.getWalls(),
				state.getRooms(), state.getGoalHouse());//, state.getGoalRoom(), state.getGoalWall(), state.getGoalBlock());
	}

	@Override
	public MakeRoomState copy() {
		return new MakeRoomState(width, height, touchAgent(), touchPoints(), touchBlocks(), touchWalls(), touchRooms(),
				touchGoalHouse());
//				touchGoalRoom(), touchGoalWall(), touchGoalBlock());
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
	
//	public HRoom touchRoom() {
//		this.goalRoom = (HRoom) this.goalRoom.copy();
//		return this.goalRoom;
//	}
	
	public boolean isWallCorner(HPoint corner) {
		for (HWall wall : walls) {
			HPoint start = (HPoint) wall.get(HWall.ATT_START);
			HPoint end = (HPoint) wall.get(HWall.ATT_END);
			if (start.compareTo(corner) == 0 || end.compareTo(corner) == 0) {
				return true;
			}
		}
		
		if(blockAt(corner)) {
			return true;
		}
		
		return false;
	}

	public boolean areContiguousInLine(HPoint start, HPoint end) {
		for (HWall wall : walls) {
			HPoint wallStart = (HPoint) wall.get(HWall.ATT_START);
			HPoint wallEnd = (HPoint) wall.get(HWall.ATT_END);
			if ((start.compareTo(wallStart) == 0 && end.compareTo(wallEnd) == 0)
			 || (start.compareTo(wallEnd) == 0 && end.compareTo(wallStart) == 0)) {
				return true;
			}
		}
		return false;
	}

	public int wallsRemaining(MakeRoomTF tf) {
		GroundedPropSC goal = (GroundedPropSC) tf.getGoalCondition();
		GroundedProp gp = goal.gp;
		HasGoalRoomPF pf = (HasGoalRoomPF) gp.pf;
		HRoom goalRoom = pf.goal;
//		String[] params = gp.params;
//		String startName = params[0];
//		String endName = params[1];
//		HPoint start = (HPoint) this.object(startName);
//		HPoint end = (HPoint) this.object(endName);
		return HasGoalRoomPF.getNumWallsRemaining(this, goalRoom);
	}

}

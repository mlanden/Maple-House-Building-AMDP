package amdp.house.level4;

import java.util.List;
import java.util.Map;

import amdp.house.base.HouseBaseState;
import amdp.house.level3.HasFinishedRoom;
import amdp.house.level3.MakeRoomState;
import amdp.house.objects.HAgent;
import amdp.house.objects.HBlock;
import amdp.house.objects.HPoint;
import amdp.house.objects.HRoom;
import amdp.house.objects.HWall;
import utils.IntPair;

public class MakeHouseState  extends HouseBaseState {
	
	public MakeHouseState(int width, int height, HHouse goalHouse) {
		super(width, height, null, null);
		this.goalHouse = goalHouse;
	}

	public MakeHouseState(int width, int height, HAgent agent, Map<IntPair, HPoint> points, Map<IntPair, HBlock> blocks,
			List<HWall> walls, List<HRoom> rooms, HRoom goalRoom, HWall goalWall, HBlock goalBlock) {
		super(width, height, agent, points, blocks, walls, rooms, goalRoom, goalWall, goalBlock);
	}
	
	public MakeHouseState(HouseBaseState state) {
		super(state.getWidth(), state.getHeight(), state.getAgent(), state.getPoints(), state.getBlocks(), state.getWalls(),
				state.getRooms(), state.getGoalRoom(), state.getGoalWall(), state.getGoalBlock());
	}

	@Override
	public MakeHouseState copy() {
		return new MakeHouseState(width, height, touchAgent(), touchPoints(), touchBlocks(), touchWalls(), touchRooms(), touchGoalRoom(),
				touchGoalWall(), touchGoalBlock());
	}

}
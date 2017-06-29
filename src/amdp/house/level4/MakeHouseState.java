package amdp.house.level4;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import amdp.house.base.HouseBaseState;
import amdp.house.objects.HAgent;
import amdp.house.objects.HBlock;
import amdp.house.objects.HHouse;
import amdp.house.objects.HPoint;
import amdp.house.objects.HRoom;
import amdp.house.objects.HWall;
import utils.IntPair;

public class MakeHouseState  extends HouseBaseState {
	
	public HRoom outside = new HRoom("outside", new ArrayList<HPoint>(), true);
	
	public MakeHouseState(int width, int height, HHouse goalHouse) {
		super(width, height, null, null);
		this.goalHouse = goalHouse;
	}

	public MakeHouseState(int width, int height, HAgent agent, Map<IntPair, HPoint> points, Map<IntPair, HBlock> blocks,
			List<HWall> walls, List<HRoom> rooms, HHouse goalHouse) {
		super(width, height, agent, points, blocks, walls, rooms, goalHouse);
	}
	
	public MakeHouseState(HouseBaseState state) {
		super(state.getWidth(), state.getHeight(), state.getAgent(), state.getPoints(), state.getBlocks(), state.getWalls(),
				state.getRooms(), state.getGoalHouse());
	}

	@Override
	public MakeHouseState copy() {
		return new MakeHouseState(width, height, touchAgent(), touchPoints(), touchBlocks(), touchWalls(), touchRooms(), 
				touchGoalHouse());
	}

	public boolean spacesLinked(HRoom room, HRoom outside) {
		
	}

}
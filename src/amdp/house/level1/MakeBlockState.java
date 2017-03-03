package amdp.house.level1;

import java.util.List;
import java.util.Map;

import amdp.house.base.HouseBaseState;
import amdp.house.level2.MakeWallState;
import amdp.house.objects.HAgent;
import amdp.house.objects.HBlock;
import amdp.house.objects.HPoint;
import amdp.house.objects.HRoom;
import amdp.house.objects.HWall;
import utils.IntPair;

public class MakeBlockState extends HouseBaseState {

	
	public MakeBlockState(int width, int height, int agentX, int agentY) {
		super(width, height, agentX, agentY);
	}

	public MakeBlockState(int width, int height, HAgent agent, Map<IntPair, HPoint> points, Map<IntPair, HBlock> blocks,
			List<HWall> walls, List<HRoom> rooms, HRoom goalRoom, HWall goalWall, HBlock goalBlock) {
		super(width, height, agent, points, blocks, walls, rooms, goalRoom, goalWall, goalBlock);
	}
	
	public MakeBlockState(int width, int height, HAgent agent, Map<IntPair,HPoint> points, Map<IntPair, HBlock> blocks, HBlock goalBlock) {
		super(width, height, agent, points, blocks, null, null, null, null, goalBlock);
	}
	
//	@Override
//	public int numObjects() {
//		int numObjects = super.numObjects();
//		numObjects += block != null ? 1 : 0;
//		return numObjects;
//	}
//	
//	@Override
//	public List<ObjectInstance> objects() {
//		List<ObjectInstance> objects = super.objects();
//		objects.add(block);
//		return objects;
//	}
//	
//	public HBlock touchBlock() {
//		this.block = this.block.copy();
//		return this.block;
//	}
	
	public MakeBlockState(HouseBaseState state) {
		super(state.getWidth(), state.getHeight(), state.getAgent(), state.getPoints(), state.getBlocks(), state.getWalls(),
				state.getRooms(), state.getGoalRoom(), state.getGoalWall(), state.getGoalBlock());
	}

	@Override
	public MakeBlockState copy() {
		return new MakeBlockState(width, height, touchAgent(), touchPoints(), touchBlocks(), touchWalls(), touchRooms(), touchGoalRoom(),
				touchGoalWall(), touchGoalBlock());
	}

//	public HBlock getBlock() {
//		return block;
//	}
	
}

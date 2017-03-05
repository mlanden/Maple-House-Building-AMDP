package amdp.house.level2;

import java.util.List;
import java.util.Map;

import amdp.amdpframework.GroundedPropSC;
import amdp.house.base.HouseBaseState;
import amdp.house.objects.HAgent;
import amdp.house.objects.HBlock;
import amdp.house.objects.HPoint;
import amdp.house.objects.HRoom;
import amdp.house.objects.HWall;
import burlap.mdp.core.oo.propositional.GroundedProp;
import utils.IntPair;

public class MakeWallState extends HouseBaseState {

	public MakeWallState(int width, int height, int agentX, int agentY) {
		super(width, height, agentX, agentY);
	}
	
	public MakeWallState(int width, int height, HAgent agent, Map<IntPair, HPoint> points, Map<IntPair, HBlock> blocks,
			List<HWall> walls, List<HRoom> rooms, HRoom goalRoom, HWall goalWall, HBlock goalBlock) {
		super(width, height, agent, points, blocks, walls, rooms, goalRoom, goalWall, goalBlock);
	}

	public MakeWallState(HouseBaseState state) {
		super(state.getWidth(), state.getHeight(), state.getAgent(), state.getPoints(), state.getBlocks(), state.getWalls(),
				state.getRooms(), state.getGoalRoom(), state.getGoalWall(), state.getGoalBlock());
	}

	@Override
	public MakeWallState copy() {
		return new MakeWallState(width, height, touchAgent(), touchPoints(), touchBlocks(), touchWalls(), touchRooms(), touchGoalRoom(),
				touchGoalWall(), touchGoalBlock());
	}

	public int blocksRemaining(MakeWallTF tf) {
		GroundedPropSC goal = (GroundedPropSC) tf.getGoalCondition();
		GroundedProp gp = goal.gp;
		String[] params = gp.params;
		String startName = params[0];
		String endName = params[1];
		HPoint start = (HPoint) this.object(startName);
		HPoint end = (HPoint) this.object(endName);
		return HasGoalWallPF.getNumBlocksRemaining(this, start, end);
	}

	public void setGoalWall(HWall wall) {
		goalWall = wall;
	}

}

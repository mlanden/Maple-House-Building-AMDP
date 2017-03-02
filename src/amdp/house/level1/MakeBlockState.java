package amdp.house.level1;

import java.util.Map;

import amdp.house.base.HouseBaseState;
import amdp.house.objects.HAgent;
import amdp.house.objects.HBlock;
import amdp.house.objects.HPoint;
import utils.IntPair;

public class MakeBlockState extends HouseBaseState {

	
	// empty state with goal block
	public MakeBlockState(int width, int height, int agentX, int agentY, HBlock goalBlock) {
		super(width, height, agentX, agentY);
		this.goalBlock = goalBlock;
	}
	
	// copy constructor
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
	
	@Override
	public MakeBlockState copy() {
		return new MakeBlockState(width, height, touchAgent(), touchPoints(), touchBlocks(), touchGoalBlock());
	}

//	public HBlock getBlock() {
//		return block;
//	}
	
	@Override
	public String toString() {
		String out = "";
		out += getGoalBlock().toString() + ", block finished: " + getGoalBlock().get(HBlock.ATT_FINISHED);
		return out;
	}
	
}

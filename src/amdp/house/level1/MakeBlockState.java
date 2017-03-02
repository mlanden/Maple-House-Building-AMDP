package amdp.house.level1;

import java.util.List;
import java.util.Map;

import amdp.house.base.HouseBaseState;
import amdp.house.objects.HAgent;
import amdp.house.objects.HBlock;
import amdp.house.objects.HPoint;
import burlap.mdp.core.oo.state.ObjectInstance;
import utils.IntPair;

public class MakeBlockState extends HouseBaseState {

	private HBlock block;
	
	// empty state with goal block
	public MakeBlockState(int width, int height, int agentX, int agentY, HBlock block) {
		super(width, height, agentX, agentY);
		this.block = block;
	}
	
	// copy constructor
	public MakeBlockState(int width, int height, HAgent agent, Map<IntPair,HPoint> points, Map<IntPair, HBlock> blocks, HBlock block) {
		super(width, height, agent, points, blocks, null, null, null);
		this.block = block;
	}
	
	@Override
	public int numObjects() {
		int numObjects = super.numObjects();
		numObjects += block != null ? 1 : 0;
		return numObjects;
	}
	
	@Override
	public List<ObjectInstance> objects() {
		List<ObjectInstance> objects = super.objects();
		objects.add(block);
		return objects;
	}
	
	public HBlock touchBlock() {
		this.block = this.block.copy();
		return this.block;
	}
	
	@Override
	public MakeBlockState copy() {
		return new MakeBlockState(width, height, touchAgent(), touchPoints(), touchBlocks(), touchBlock());
	}

	public HBlock getBlock() {
		return block;
	}
	
	@Override
	public String toString() {
		String out = "";
		out += block.toString() + ", block finished: " + block.get(HBlock.ATT_FINISHED);
		return out;
	}
	
}

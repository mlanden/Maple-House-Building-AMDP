package amdp.house.level2;

import java.util.List;
import java.util.Map;

import amdp.house.base.HouseBaseState;
import amdp.house.objects.HAgent;
import amdp.house.objects.HBlock;
import amdp.house.objects.HPoint;
import amdp.house.objects.HWall;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.MutableState;
import utils.IntPair;

public class MakeWallState extends HouseBaseState {

	private HWall wall;
	
	// empty state with goal wall
	public MakeWallState(int width, int height, int agentX, int agentY, HWall wall) {
		super(width, height, agentX, agentY);
		this.wall = wall;
	}
	
//	public MakeWallState(int width, int height, int agentX, int agentY) {
//		this(width, height, agentX, agentY, null);
//	}
	
	// copy constructor
	public MakeWallState(int width, int height, HAgent agent, Map<IntPair,HPoint> points, Map<IntPair, HBlock> blocks, HWall wall) {
		super(width, height, agent, points, blocks, null, null, null);
		this.wall = wall;
	}
	
	public MakeWallState(HouseBaseState state) {
		super(state.getWidth(), state.getHeight(), state.getAgent(), state.getPoints(), state.getBlocks(), state.getWalls(), state.getRooms(), state.getGoal());
	}

	@Override
	public int numObjects() {
		int numObjects = super.numObjects();
		numObjects += wall != null ? 1 : 0;
		return numObjects;
	}
	
	@Override
	public List<ObjectInstance> objects() {
		List<ObjectInstance> objects = super.objects();
		objects.add(wall);
		return objects;
	}
	
	public HWall touchWall() {
		this.wall = this.wall.copy();
		return this.wall;
	}
	
	@Override
	public MakeWallState copy() {
		return new MakeWallState(width, height, touchAgent(), touchPoints(), touchBlocks(), touchWall());
	}

	public HWall getWall() {
		return wall;
	}
	
	@Override
	public String toString() {
		String out = "";
		out += "blocks: " + blocks.size() + ", wall finished: " + wall.get(HWall.ATT_FINISHED);
		return out;
	}
	
}

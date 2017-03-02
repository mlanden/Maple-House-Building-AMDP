package amdp.house.level3;

import amdp.house.base.HouseBaseState;
import amdp.house.level2.MakeWallState;
import amdp.house.objects.HWall;
import burlap.mdp.auxiliary.StateMapping;
import burlap.mdp.core.oo.state.OOStateUtilities;
import burlap.mdp.core.state.State;

public class MakeRoomStateMapping implements StateMapping {
	
	@Override
	public State mapState(State s) {
		HouseBaseState state = (HouseBaseState) s;
		
		
		// here is the question / dilemma
		// the mapper needs to go from low-level state to abstract state by either
		// removing or recomputing something from the state alone
		// so it cannot "know" about the goal at the higher level, unless it is built into the state
		MakeRoomState out = new MakeRoomState(state);
		
		return out;
	}

}

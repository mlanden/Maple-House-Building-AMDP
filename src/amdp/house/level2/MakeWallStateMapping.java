package amdp.house.level2;

import amdp.house.base.HouseBaseState;
import burlap.mdp.auxiliary.StateMapping;
import burlap.mdp.core.state.State;

public class MakeWallStateMapping implements StateMapping {
	
	@Override
	public State mapState(State s) {
		HouseBaseState state = (HouseBaseState) s;
		MakeWallState out = new MakeWallState(state);
		return out;
	}

}

package amdp.house.level1;

import amdp.house.base.HouseBaseState;
import burlap.mdp.auxiliary.StateMapping;
import burlap.mdp.core.state.State;

public class MakeBlockStateMapping implements StateMapping {
	
	@Override
	public State mapState(State s) {
		HouseBaseState state = (HouseBaseState) s;
		MakeBlockState out = new MakeBlockState(state);
		return out;
	}

}

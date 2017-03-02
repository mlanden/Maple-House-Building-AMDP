package amdp.house.level3;

import amdp.house.base.HouseBaseState;
import burlap.mdp.auxiliary.StateMapping;
import burlap.mdp.core.state.State;

public class MakeRoomStateMapping implements StateMapping {
	
	@Override
	public State mapState(State s) {
		HouseBaseState state = (HouseBaseState) s;
		MakeRoomState out = new MakeRoomState(state);
		return out;
	}

}

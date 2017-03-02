package amdp.house.level2;

import amdp.house.base.HouseBaseState;
import burlap.mdp.auxiliary.StateMapping;
import burlap.mdp.core.state.State;

public class MakeWallStateMapping implements StateMapping {
	
	@Override
	public State mapState(State s) {
		HouseBaseState state = (HouseBaseState) s;
		MakeWallState out = new MakeWallState(state);
		
		// here's the dilemma -- the state itself cannot know what the higher AMDP wants to do
		// so the TaskNode's domain/tf/rf need to govern this knowledge instead
		
		return out;
	}

}

package amdp.house.level3;

import burlap.mdp.auxiliary.stateconditiontest.StateConditionTest;
import burlap.mdp.core.state.State;
import utils.GoalFailureTF;

public class MakeRoomTF extends GoalFailureTF {

	public MakeRoomTF(StateConditionTest goalCondition) {
		super(goalCondition);
	}

	@Override
	public boolean satisfiesFailure(State s) {
		return false;
	}
	
}

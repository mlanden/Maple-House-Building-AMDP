package amdp.house.level4;

import burlap.mdp.auxiliary.stateconditiontest.StateConditionTest;
import burlap.mdp.core.state.State;
import utils.GoalFailureTF;

public class MakeHouseTF extends GoalFailureTF {

	public MakeHouseTF(StateConditionTest goalCondition) {
		super(goalCondition);
	}

	@Override
	public boolean satisfiesFailure(State s) {
		return false;
	}
	
}
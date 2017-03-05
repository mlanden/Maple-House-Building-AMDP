package amdp.house.level2;

import burlap.mdp.auxiliary.stateconditiontest.StateConditionTest;
import burlap.mdp.core.state.State;
import utils.GoalFailureTF;

public class MakeWallTF extends GoalFailureTF {

	public MakeWallTF(StateConditionTest goalCondition) {
		super(goalCondition);
	}

	@Override
	public boolean satisfiesFailure(State s) {
		return false;
	}

	public void setGoalCondition(StateConditionTest goalCondition) {
		this.goalCondition = goalCondition;
	}

}
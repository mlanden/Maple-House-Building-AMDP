package utils;

import burlap.mdp.auxiliary.stateconditiontest.StateConditionTest;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.state.State;

public abstract class GoalFailureTF implements TerminalFunction {

	protected StateConditionTest goalCondition;
	
	public GoalFailureTF(StateConditionTest goalCondition) {
		this.goalCondition = goalCondition;
	}
	
	public void setGoalCondition(StateConditionTest goalCondition) {
		this.goalCondition = goalCondition;
	}
	
	public StateConditionTest getGoalCondition() {
		return goalCondition;
	}

	@Override
	public boolean isTerminal(State s) {
		if(satisfiesFailure(s)) {
			return true;
		} else if (satisfiesGoal(s)) {
			return true;
		}
		return false;
	}

	public boolean satisfiesGoal(State s) {
		return goalCondition.satisfies(s);
	}

	public abstract boolean satisfiesFailure(State s);
	
}

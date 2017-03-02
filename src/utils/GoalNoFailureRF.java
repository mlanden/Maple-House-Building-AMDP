package utils;

import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.state.State;

public class GoalNoFailureRF extends GoalFailureRF {
	
	public GoalNoFailureRF(TerminalFunction tf, double rewardGoal, double rewardDefault, double rewardFailure) {
		super(tf, rewardGoal, rewardDefault, rewardFailure);
	}

	@Override
	public boolean satisfiesGoal(State state) {
		return tf.isTerminal(state);
	}

	@Override
	public boolean satisfiesFailure(State state) {
		return false;
	}

}

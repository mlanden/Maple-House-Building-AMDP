package amdp.house.level2;

import burlap.mdp.core.state.State;
import utils.GoalFailureRF;

public class MakeWallRF extends GoalFailureRF {

	public MakeWallRF(MakeWallTF tf, double rewardGoal, double rewardDefault, double rewardFailure) {
		super(tf, rewardGoal, rewardDefault, rewardFailure);
	}

	@Override
	public boolean satisfiesGoal(State s) {
//		return ((MakeWallTF)tf).satisfiesGoal((MakeWallState) state);
		return tf.isTerminal(s);
	}

	@Override
	public boolean satisfiesFailure(State s) {
//		return ((MakeWallTF)tf).goesTooFar((MakeWallState) state);
		return false;
	}

}

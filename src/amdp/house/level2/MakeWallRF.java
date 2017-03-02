package amdp.house.level2;

import burlap.mdp.core.state.State;
import utils.GoalFailureRF;

public class MakeWallRF extends GoalFailureRF {

	public MakeWallRF(MakeWallTF tf, double rewardGoal, double rewardDefault, double rewardFailure) {
		super(tf, rewardGoal, rewardDefault, rewardFailure);
	}

	@Override
	public boolean satisfiesGoal(State state) {
		return ((MakeWallTF)tf).satisfiesGoal((MakeWallState) state);
	}

	@Override
	public boolean satisfiesFailure(State state) {
		return ((MakeWallTF)tf).goesTooFar((MakeWallState) state);
	}

}

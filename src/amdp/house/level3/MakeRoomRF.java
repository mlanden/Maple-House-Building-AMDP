package amdp.house.level3;

import burlap.mdp.core.TerminalFunction;
import utils.GoalNoFailureRF;

public class MakeRoomRF extends GoalNoFailureRF {

	public MakeRoomRF(TerminalFunction tf, double rewardGoal, double rewardDefault, double rewardFailure) {
		super(tf, rewardGoal, rewardDefault, rewardFailure);
	}

}

package amdp.house.level4;

import burlap.mdp.core.TerminalFunction;
import utils.GoalNoFailureRF;

public class MakeHouseRF extends GoalNoFailureRF {

	public MakeHouseRF(TerminalFunction tf, double rewardGoal, double rewardDefault, double rewardFailure) {
		super(tf, rewardGoal, rewardDefault, rewardFailure);
	}

}

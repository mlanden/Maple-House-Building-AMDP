package amdp.house.level1;

import burlap.mdp.core.TerminalFunction;
import utils.GoalNoFailureRF;

public class MakeBlockRF extends GoalNoFailureRF {

	public MakeBlockRF(TerminalFunction tf, double rewardGoal, double rewardDefault, double rewardFailure) {
		super(tf, rewardGoal, rewardDefault, rewardFailure);
	}

}

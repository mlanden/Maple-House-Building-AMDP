package amdp.house.level1;

import amdp.house.objects.HPoint;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.RewardFunction;

public class MakeWallRF implements RewardFunction {

	public MakeWallTF tf;
	public double rewardGoal;
	public double rewardDefault;
	public double rewardFailure;
	
	public MakeWallRF(MakeWallTF tf, double rewardGoal, double rewardDefault, double rewardFailure) {
		this.tf = tf;
		this.rewardGoal = rewardGoal;
		this.rewardDefault = rewardDefault;
		this.rewardFailure = rewardFailure;
	}

	@Override
	public double reward(State s, Action a, State sprime) {
		MakeWallState statePrime = (MakeWallState) sprime;
		if (tf.satisfiesGoal(statePrime)) {
			return rewardGoal;
		} else if (tf.goesTooFar(statePrime)) {
			return rewardFailure;
		}
		return rewardDefault;
	}

}

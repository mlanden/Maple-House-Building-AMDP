package amdp.house.level2;

import amdp.house.level1.MakeWallState;
import amdp.house.level1.MakeWallTF;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.RewardFunction;

public class MakeRoomRF implements RewardFunction {
	
	private MakeRoomTF tf;
	public double rewardGoal;
	public double rewardDefault;
	public double rewardFailure;
	
	public MakeRoomRF(MakeRoomTF tf, double rewardGoal, double rewardDefault, double rewardFailure) {
		this.tf = tf;
		this.rewardGoal = rewardGoal;
		this.rewardDefault = rewardDefault;
		this.rewardFailure = rewardFailure;
	}
	
	public void setTF(MakeRoomTF tf) {
		this.tf = tf;
	}

	@Override
	public double reward(State s, Action a, State sprime) {
		MakeRoomState statePrime = (MakeRoomState) sprime;
		if (tf.isTerminal(statePrime)) {
			return rewardGoal;
		}
		return rewardDefault;
	}

}

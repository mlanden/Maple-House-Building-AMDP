package utils;

import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.RewardFunction;

public abstract class GoalFailureRF implements RewardFunction {
	
	protected TerminalFunction tf;
	protected double rewardGoal;
	protected double rewardDefault;
	protected double rewardFailure;
	
	public GoalFailureRF(TerminalFunction tf, double rewardGoal, double rewardDefault, double rewardFailure) {
		this.tf = tf;
		this.rewardGoal = rewardGoal;
		this.rewardDefault = rewardDefault;
		this.rewardFailure = rewardFailure;
	}
	
	public void setTF(TerminalFunction tf) {
		this.tf = tf;
	}

	@Override
	public double reward(State s, Action a, State sPrime) {
		if (satisfiesGoal(sPrime)) {
			return rewardGoal;
		} else if (satisfiesFailure(sPrime)) {
			return rewardFailure;
		}
		return rewardDefault;
	}
	
	public abstract boolean satisfiesGoal(State state);
	
	public abstract boolean satisfiesFailure(State state);

}

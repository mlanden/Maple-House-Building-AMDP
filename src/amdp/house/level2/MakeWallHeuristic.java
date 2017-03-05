package amdp.house.level2;

import burlap.behavior.valuefunction.ValueFunction;
import burlap.mdp.core.state.State;

public class MakeWallHeuristic implements ValueFunction {

	private double discount;
	private MakeWallTF tf;
	
	public MakeWallHeuristic(double discount, MakeWallTF tf) {
		this.discount = discount;
		this.tf = tf;
	}
	
	@Override
	public double value(State s) {
		MakeWallState state = (MakeWallState) s;
		
		double heuristic = 0.0;
		
		double distanceToGoal = state.blocksRemaining(tf);
		heuristic = Math.pow(discount, distanceToGoal);
		
		return heuristic;
	}

}

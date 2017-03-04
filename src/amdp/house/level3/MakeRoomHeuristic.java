package amdp.house.level3;

import burlap.behavior.valuefunction.ValueFunction;
import burlap.mdp.core.state.State;

public class MakeRoomHeuristic implements ValueFunction {

	private double discount;
	
	public MakeRoomHeuristic(double discount) {
		this.discount = discount;
	}
	
	@Override
	public double value(State s) {
		MakeRoomState state = (MakeRoomState) s;
		
		double heuristic = 0.0;
		
		double distanceToGoal = state.wallsRemaining(); 
		heuristic = Math.pow(discount, distanceToGoal);
		
		return heuristic;
	}

}

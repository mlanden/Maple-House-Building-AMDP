package amdp.house.level3;

import amdp.house.level2.MakeWallTF;
import burlap.behavior.valuefunction.ValueFunction;
import burlap.mdp.core.state.State;

public class MakeRoomHeuristic implements ValueFunction {

	private double discount;
	private MakeRoomTF tf;
	
	public MakeRoomHeuristic(double discount, MakeRoomTF tf) {
		this.discount = discount;
		this.tf = tf;
	}
	
	@Override
	public double value(State s) {
		MakeRoomState state = (MakeRoomState) s;
		
		double heuristic = 0.0;
		
		double distanceToGoal = state.wallsRemaining(tf); 
		heuristic = Math.pow(discount, distanceToGoal);
		
		return heuristic;
	}

}

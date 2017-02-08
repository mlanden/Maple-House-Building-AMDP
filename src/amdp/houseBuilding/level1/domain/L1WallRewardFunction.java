package amdp.houseBuilding.level1.domain;

import java.util.List;

import amdp.houseBuilding.level1.state.L1State;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.RewardFunction;
import compositeObjectDomain.Wall;

public class L1WallRewardFunction implements RewardFunction{

	protected int goalSize;
	
	public L1WallRewardFunction(int goal){
		goalSize = goal;
	}
	
	public double reward(State s, Action a, State sprime) {
		List<Wall> walls = ((L1State) s).walls;
		for(Wall w : walls){
			return 100;
		}
		return -1;
	}

}

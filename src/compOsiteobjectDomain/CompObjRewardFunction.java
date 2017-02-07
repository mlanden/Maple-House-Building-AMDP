package compOsiteobjectDomain;

import java.util.List;

import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.RewardFunction;

public class CompObjRewardFunction implements RewardFunction{
	
	int wallSize;
	
	public CompObjRewardFunction(int wallSize)
	{
		this.wallSize = wallSize;
	}
	
	
	@Override
	public double reward(State s, Action a, State sprime) {
		CompObjAgent agent = (CompObjAgent) s.get(CompObjDomain.CLASS_AGENT);
		List<Wall> walls = (List<Wall>) agent.get("Walls");
		
		for(Wall w: walls)
		{
			if(w.length() >= wallSize)
				return 1000;
		}
		return -1;
	}

}

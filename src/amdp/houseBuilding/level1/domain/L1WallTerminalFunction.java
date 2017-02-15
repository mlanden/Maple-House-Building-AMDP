package amdp.houseBuilding.level1.domain;

import java.util.List;

import amdp.houseBuilding.level1.state.L1State;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.state.State;
import compositeObjectDomain.Wall;

public class L1WallTerminalFunction implements TerminalFunction{

	protected int goalsize;
	
	public L1WallTerminalFunction(int goal){
		goalsize = goal;
	}
	
	public boolean isTerminal(State s) {
		List<Wall> walls = ((L1State) s).walls;
		for(Wall w: walls){
			if(w.length >= goalsize){
				return true;
			}
		}
		return false;
	}

}
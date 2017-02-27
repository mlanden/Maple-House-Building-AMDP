package amdp.house.level2;

import amdp.house.objects.HWall;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.state.State;

public class MakeRoomTF implements TerminalFunction {

	public HWall goal;
	
	public MakeRoomTF(HWall goal) {
		this.goal = goal;
	}
	
	@Override
	public boolean isTerminal(State s) {
		MakeRoomState state = (MakeRoomState) s;
		if (state.hasWall(goal)) {
			return true;
		}
		return false;
	}

}

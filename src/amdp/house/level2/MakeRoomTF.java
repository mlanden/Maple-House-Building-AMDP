package amdp.house.level2;

import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.state.State;

public class MakeRoomTF implements TerminalFunction {

	@Override
	public boolean isTerminal(State s) {
		MakeRoomState state = (MakeRoomState) s;
		if (state.hasWall()) {
			return true;
		}
		return false;
	}

}

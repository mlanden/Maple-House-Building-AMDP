package amdp.house.level3;

import amdp.house.objects.HRoom;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.state.State;

public class MakeRoomTF implements TerminalFunction {

	public MakeRoomTF() {
	}
	
	public boolean satisfiesGoal(MakeRoomState state) {
		HRoom goal = state.getRoom();
		if ((boolean) goal.get(HRoom.ATT_FINISHED)) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean isTerminal(State s) {
		MakeRoomState state = (MakeRoomState) s;
		
		// here is where other termination conditions (for penalizing failure, early termination) go
		
		return satisfiesGoal(state);
	}

}
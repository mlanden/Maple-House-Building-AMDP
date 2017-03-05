package amdp.house.level4;

import amdp.house.level3.HasFinishedRoom;
import amdp.house.level3.MakeRoomState;
import amdp.house.objects.HRoom;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.state.State;

public class MakeHouseTF implements TerminalFunction {
	
	public HasFinishedHouse hasFinishedHouse = new HasFinishedHouse();

	protected HHouse goal;
	
	public MakeHouseTF(HHouse goal) {
		this.goal = goal;
	}
	
	public HHouse getGoal() {
		return goal;
	}
	
	public boolean satisfiesGoal(MakeRoomState state) {
		HRoom goal = state.getRoom();
		if (hasFinishedHouse.satisfies(state, goal)) {
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
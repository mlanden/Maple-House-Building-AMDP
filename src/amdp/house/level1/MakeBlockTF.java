package amdp.house.level1;

import amdp.house.objects.HBlock;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.state.State;

public class MakeBlockTF implements TerminalFunction {
	
	public HasFinishedBlock hasFinishedBlock = new HasFinishedBlock();
	
	protected HBlock goal;
	
	public MakeBlockTF(HBlock goal) {
		this.goal = goal;
	}
	
	public HBlock getGoal() {
		return goal;
	}
	
	public void setGoal(HBlock goal) {
		this.goal = goal;
	}

	@Override
	public boolean isTerminal(State s) {
		MakeBlockState state = (MakeBlockState) s;
		
		// here is where other termination conditions (for penalizing failure, early termination) go
		
		return satisfiesGoal(state);
	}

	public boolean satisfiesGoal(MakeBlockState state) {
		if (hasFinishedBlock.satisfies(state, goal)) {
			return true;
		}
		return false;
	}

}

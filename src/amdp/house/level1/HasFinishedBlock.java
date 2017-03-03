package amdp.house.level1;

import amdp.house.objects.HBlock;
import burlap.mdp.core.state.State;

public class HasFinishedBlock {

	public HasFinishedBlock() {
		
	}
	
	public boolean satisfies(State s, HBlock goal) {
		MakeBlockState state = (MakeBlockState) s;
		return state.blockAt((int) goal.get(HBlock.ATT_X), (int) goal.get(HBlock.ATT_Y));
	}
	
}

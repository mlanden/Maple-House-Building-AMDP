package amdp.house.level1;

import amdp.house.objects.HBlock;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.state.State;

public class MakeBlockTF implements TerminalFunction {

	@Override
	public boolean isTerminal(State s) {
		MakeBlockState state = (MakeBlockState) s;
		boolean finished = (boolean) state.getBlock().get(HBlock.ATT_FINISHED);
		return finished;
	}

}

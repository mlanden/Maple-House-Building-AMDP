package utils;

import burlap.mdp.auxiliary.stateconditiontest.StateConditionTest;
import burlap.mdp.core.oo.propositional.GroundedProp;
import burlap.mdp.core.oo.state.OOState;
import burlap.mdp.core.state.State;

public class DynamicGroundedPropSC implements StateConditionTest {

	protected GroundedProp test;
	
	public DynamicGroundedPropSC(GroundedProp test) {
		this.test = test;
	}
	
	public void setGoalParams(String[] params) {
		test.params = params;
	}

	@Override
	public boolean satisfies(State s) {
		return test.isTrue((OOState) s);
	}

}

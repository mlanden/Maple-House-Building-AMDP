package amdp.house.base;

import amdp.house.objects.HPoint;
import burlap.mdp.core.oo.ObjectParameterizedAction;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.oo.ObjectParameterizedActionType;

public class PointParameterizedActionType extends ObjectParameterizedActionType {

	public PointParameterizedActionType(String name) {
		super(name, new String[]{HPoint.CLASS_POINT});
	}

	@Override
	protected boolean applicableInState(State s, ObjectParameterizedAction a) {
		return true;
	}
	
	
}

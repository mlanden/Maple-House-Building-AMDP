package amdp.house;

import amdp.amdpframework.PrimitiveTaskNode;
import burlap.mdp.core.action.ActionType;

public class TaskLeaf extends PrimitiveTaskNode {

	public TaskLeaf(ActionType action){
		this.setActionType(action);
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
}

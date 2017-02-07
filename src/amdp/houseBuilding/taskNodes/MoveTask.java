package amdp.houseBuilding.taskNodes;

import amdp.amdpframework.PrimitiveTaskNode;
import burlap.mdp.core.action.ActionType;

public class MoveTask extends PrimitiveTaskNode{

	public MoveTask(ActionType action){
		super.setActionType(action);
	}
}

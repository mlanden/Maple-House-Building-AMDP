package amdp.houseBuilding.taskNodes;

import amdp.amdpframework.PrimitiveTaskNode;
import burlap.mdp.core.action.ActionType;

public class PutBlockTask extends PrimitiveTaskNode{

	public PutBlockTask(ActionType action){
		this.setActionType(action);
	}
}

package amdp.houseBuilding.taskNodes;

import amdp.amdpframework.PrimitiveTaskNode;
import burlap.mdp.core.action.ActionType;

public class BuildTask extends PrimitiveTaskNode {

	public BuildTask(ActionType action){
		this.setActionType(action);
	}
}

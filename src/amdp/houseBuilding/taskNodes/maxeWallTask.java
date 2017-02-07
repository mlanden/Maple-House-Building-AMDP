package amdp.houseBuilding.taskNodes;

import java.util.ArrayList;
import java.util.List;

import amdp.amdpframework.GroundedTask;
import amdp.amdpframework.NonPrimitiveTaskNode;
import amdp.amdpframework.TaskNode;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.action.UniversalActionType;
import burlap.mdp.core.oo.OODomain;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.oo.OOSADomain;
import compositeObjectDomain.CompObjDomain;

public class maxeWallTask extends NonPrimitiveTaskNode{

	ActionType maxeWallType;
	
	public maxeWallTask(ActionType makeWallType, OOSADomain houseL0Domain, TaskNode[] subtasks){
		this.maxeWallType = makeWallType;
		this.oosaDomain = houseL0Domain;
		this.oosaDomain.clearActionTypes();
		this.oosaDomain.addActionTypes(
				new UniversalActionType(CompObjDomain.ACTION_NORTH),
				new UniversalActionType(CompObjDomain.ACTION_EAST ),
				new UniversalActionType(CompObjDomain.ACTION_SOUTH),
				new UniversalActionType(CompObjDomain.ACTION_WEST)
		);
		this.childTaskNodes = subtasks;
	}
	
	public List<GroundedTask> getApplicableGroundedTasks(State s) {
		List<GroundedTask> enviromentTasks = new ArrayList<GroundedTask>();
		
		List<Action> possibleAction = maxeWallType.allApplicableActions(s);
		for(Action a: possibleAction){
			enviromentTasks.add(new GroundedTask(this, a));
		}
		
		return enviromentTasks;

		return enviromentTasks;
	}

	public RewardFunction rewardFunction(Action a) {
		
	}

	public boolean terminal(State s, Action a) {
		
	}

	//return a list of string action names
	public Object parametersSet(State s) {
		List<String[]> actionNames = new ArrayList<String[]>();
		
		//gett possible actions from s
		List<Action> possibleActions = maxeWallType.allApplicableActions(s);
		for(Action a : possibleActions){
			actionNames.add(new String[]{a.actionName()});
		}
		
		return actionNames;
	}
}

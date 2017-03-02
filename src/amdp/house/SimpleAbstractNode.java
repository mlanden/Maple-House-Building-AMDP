package amdp.house;

import amdp.amdpframework.GroundedTask;
import amdp.amdpframework.NonPrimitiveTaskNode;
import amdp.amdpframework.TaskNode;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.action.SimpleAction;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.oo.OOSADomain;

import java.util.ArrayList;
import java.util.List;

public class SimpleAbstractNode extends NonPrimitiveTaskNode {
	
	public ActionType actionType;

    public SimpleAbstractNode(ActionType actionType, TaskNode[] subtasks,
    		OOSADomain oosaDomain) {
    	this.actionType = actionType;
    	this.oosaDomain = oosaDomain;
    	this.childTaskNodes = subtasks;
	}

	@Override
	public boolean terminal(State s, Action action) {
		throw new RuntimeException("not implemented");
	}

	@Override
	public List<GroundedTask> getApplicableGroundedTasks(State s) {
		throw new RuntimeException("not implemented");
	}

	@Override
	public RewardFunction rewardFunction(Action action) {
		throw new RuntimeException("not implemented");
	}

	@Override
	public Object parametersSet(State s) {
		throw new RuntimeException("not implemented");
	}

}
package amdp.houseBuilding.taskNodes;

import java.util.ArrayList;
import java.util.List;

import amdp.amdpframework.GroundedTask;
import amdp.amdpframework.NonPrimitiveTaskNode;
import amdp.amdpframework.TaskNode;
import amdp.houseBuilding.level1.state.L1ProjectionFunction;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.action.SimpleAction;
import burlap.mdp.core.action.UniversalActionType;
import burlap.mdp.core.oo.OODomain;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.oo.OOSADomain;
import compositeObjectDomain.CompObjDomain;

public class makeWallTask extends NonPrimitiveTaskNode{

	RewardFunction rf;
	TerminalFunction tf;
	L1ProjectionFunction l1sp = new L1ProjectionFunction();
	ActionType action;
	
	public makeWallTask(ActionType at, OOSADomain houseL0Domain, TaskNode[] subtasks, 
			RewardFunction r, TerminalFunction t){
		this.rf = r; 
		this.tf = t;
		this.childTaskNodes = subtasks;
	
	}

	public boolean terminal(State s, Action action) {
		return tf.isTerminal(l1sp.mapState(s));
	}

	public List<GroundedTask> getApplicableGroundedTasks(State s) {
		List<GroundedTask> groundedTasks = new ArrayList<GroundedTask>();
		List<Action> acts = action.allApplicableActions(s);
		for(Action a : acts){
			groundedTasks.add(new GroundedTask(this, a));
		}
		return groundedTasks;
	}

	public RewardFunction rewardFunction(Action action) {
		return rf;
	}

	//NOT RIGHT FIX
	public Object parametersSet(State s) {
		return new String[]{"1"};
	}
}
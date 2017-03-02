package amdp.house;

import java.util.ArrayList;
import java.util.List;

import amdp.amdpframework.GroundedTask;
import amdp.amdpframework.NonPrimitiveTaskNode;
import amdp.amdpframework.TaskNode;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.FactoredModel;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.oo.OOSADomain;

public class TaskSimpleActions extends NonPrimitiveTaskNode {
	
	public ActionType[] actionTypes;
	
	public TaskSimpleActions(String name, ActionType[] actionTypes, TaskNode[] children, OOSADomain source) {
		this.name = name;
		this.oosaDomain = source;
		this.actionTypes = actionTypes;
		this.childTaskNodes = children;
	}
	
	@Override
	public boolean terminal(State s, Action action) {
		FactoredModel model = (FactoredModel) oosaDomain.getModel();
		return model.getTf().isTerminal(s);
	}

	@Override
	public List<GroundedTask> getApplicableGroundedTasks(State s) {
        List<GroundedTask> gtList = new ArrayList<GroundedTask>();
        for (ActionType actionType : actionTypes) {
            List<Action> gtActions = actionType.allApplicableActions(s);
            for(Action a : gtActions){
                gtList.add(new GroundedTask(this,a));
            }
        }
        return gtList;
	}

	@Override
	public RewardFunction rewardFunction(Action action) {
		FactoredModel model = (FactoredModel) oosaDomain.getModel();
		return model.getRf();
	}

	@Override
	public Object parametersSet(State s) {
		throw new RuntimeException("ERROR: parametersSet not implemented");
	}
	
	@Override
	public String toString() {
		return getName();
	}

}

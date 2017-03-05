package amdp.house;

import java.util.ArrayList;
import java.util.List;

import amdp.amdpframework.GroundedTask;
import amdp.amdpframework.NonPrimitiveTaskNode;
import amdp.amdpframework.TaskNode;
import amdp.house.base.HouseBaseState;
import amdp.house.objects.HAgent;
import amdp.house.objects.HPoint;
import burlap.mdp.auxiliary.stateconditiontest.StateConditionTest;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.oo.ObjectParameterizedAction;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.common.GoalBasedRF;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.oo.OOSADomain;

public class TaskNavigate extends NonPrimitiveTaskNode {
	
	public ActionType[] actionTypes;
	
	public TaskNavigate(String name, ActionType[] actionTypes, TaskNode[] children, OOSADomain source) {
		this.name = name;
		this.oosaDomain = source;
		ActionType[] domainActionTypes = new ActionType[children.length];
		for (int i = 0; i < children.length; i++) {
			TaskNode child = children[i];
			domainActionTypes[i] = this.oosaDomain.getAction(child.getName());
		}
		this.oosaDomain.clearActionTypes();
		for (ActionType domainActionType : domainActionTypes) {
			this.oosaDomain.addActionType(domainActionType);
		}
		this.actionTypes = actionTypes;
		this.childTaskNodes = children;
	}
	
	@Override
	public boolean terminal(State s, Action a) {
//		FactoredModel model = (FactoredModel) oosaDomain.getModel();
//		return model.getTf().isTerminal(s);
//		System.out.println("state " + s);
//		System.out.println("action " + a);
//		throw new RuntimeException("");
//		return true;
		HouseBaseState state = (HouseBaseState) s;
		int aX = (int) state.getAgent().get(HAgent.ATT_X);
		int aY = (int) state.getAgent().get(HAgent.ATT_Y);
		ObjectParameterizedAction action = (ObjectParameterizedAction) a;
		String[] params = action.getObjectParameters();
		String destinationName = params[0];
		HPoint destination = (HPoint) state.object(destinationName);
		int bX = (int) destination.get(HPoint.ATT_X);
		int bY = (int) destination.get(HPoint.ATT_Y);
		if (aX == bX && aY == bY) {
			return true;
		}
		return false;

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
	public RewardFunction rewardFunction(Action a) {
		ObjectParameterizedAction action = (ObjectParameterizedAction) a;
		String[] params = action.getObjectParameters();
		final String destinationName = params[0];
		StateConditionTest sct = new StateConditionTest() {

			@Override
			public boolean satisfies(State s) {
				HouseBaseState state = (HouseBaseState) s;
				int aX = (int) state.getAgent().get(HAgent.ATT_X);
				int aY = (int) state.getAgent().get(HAgent.ATT_Y);
				HPoint destination = (HPoint) state.object(destinationName);
				int bX = (int) destination.get(HPoint.ATT_X);
				int bY = (int) destination.get(HPoint.ATT_Y);
				if (aX == bX && aY == bY) {
					return true;
				}
				return false;
			}
			
			
		};
		return new GoalBasedRF(sct);
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
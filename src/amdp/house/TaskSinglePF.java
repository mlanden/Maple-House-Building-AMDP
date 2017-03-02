package amdp.house;

import java.util.ArrayList;
import java.util.List;

import amdp.amdpframework.GroundedPropSC;
import amdp.amdpframework.GroundedTask;
import amdp.amdpframework.NonPrimitiveTaskNode;
import amdp.amdpframework.TaskNode;
import burlap.mdp.auxiliary.common.GoalConditionTF;
import burlap.mdp.auxiliary.stateconditiontest.StateConditionTest;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.oo.ObjectParameterizedAction;
import burlap.mdp.core.oo.propositional.GroundedProp;
import burlap.mdp.core.oo.propositional.PropositionalFunction;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.common.GoalBasedRF;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.oo.OOSADomain;

public class TaskSinglePF extends NonPrimitiveTaskNode {
	
	public ActionType[] actionTypes;
	public PropositionalFunction pf;
	public String[] constantParameters;
	
	public TaskSinglePF(String name, ActionType[] actionTypes, OOSADomain source, TaskNode[] children, PropositionalFunction pf) {
		this.name = name;
		this.oosaDomain = source;
		this.actionTypes = actionTypes;
		this.childTaskNodes = children;
		this.pf = pf;
		this.constantParameters= new String[pf.getParameterClasses().length];
	}
	
	public void bindConstantParameter(int parameterIndex, String objectName) {
		this.constantParameters[parameterIndex] = objectName;
	}
	
	public StateConditionTest getStateConditionTest(Action action) {
		String[] params = new String[constantParameters.length];
		if (action instanceof ObjectParameterizedAction) {
			ObjectParameterizedAction a = (ObjectParameterizedAction)action;
			String[] actionParams = a.getObjectParameters();
			int j = 0;
			for (int i = 0; i < params.length; i++) {
				String param = constantParameters[i];
				if (param != null) {
					params[i] = param;
				} else {
					params[i] = actionParams[j];
					j++;
				}
			}
		}
		GroundedProp gp = new GroundedProp(pf, params);
        GroundedPropSC gpsc = new GroundedPropSC(gp);
		return gpsc;
	}

	@Override
	public boolean terminal(State s, Action action) {
		StateConditionTest sct = getStateConditionTest(action);
        return new GoalConditionTF(sct).isTerminal(s);
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
		StateConditionTest sct = getStateConditionTest(action);
        return new GoalBasedRF(sct);
	}

	@Override
	public Object parametersSet(State s) {
		throw new RuntimeException("ERROR: parametersSet not implemented in SinglePFTaskNode");
//        List<String[]> params = new ArrayList<String[]>();
//        for (ActionType actionType : actionTypes) {
//	        List<Action> gtActions = actionType.allApplicableActions(s);
//	        for(Action a : gtActions){
//	            params.add(new String[]{a.actionName().split("_")[1]});
//	            System.out.println(a.actionName());
//	        }
//        }
//        return params;
	}
	
	@Override
	public String toString() {
		return getName();
	}

}

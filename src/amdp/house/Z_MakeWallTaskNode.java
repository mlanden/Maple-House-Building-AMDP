package amdp.house;

import java.util.ArrayList;
import java.util.List;

import amdp.amdpframework.GroundedPropSC;
import amdp.amdpframework.GroundedTask;
import amdp.amdpframework.NonPrimitiveTaskNode;
import amdp.amdpframework.TaskNode;
import amdp.house.level1.Z_HasWallSCT;
import amdp.house.level1.MakeWallRF;
import amdp.house.level1.MakeWallTF;
import burlap.mdp.auxiliary.common.GoalConditionTF;
import burlap.mdp.auxiliary.stateconditiontest.StateConditionTest;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.oo.ObjectParameterizedAction;
import burlap.mdp.core.oo.propositional.GroundedProp;
import burlap.mdp.core.oo.propositional.PropositionalFunction;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.common.GoalBasedRF;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.oo.OOSADomain;

public class Z_MakeWallTaskNode extends NonPrimitiveTaskNode {

	public ActionType[] actionTypes;
	public MakeWallRF rf;
	public MakeWallTF tf;
	
	public Z_MakeWallTaskNode(String name, ActionType[] actionTypes, OOSADomain source, TaskNode[] children, MakeWallTF tf, MakeWallRF rf) {
		this.name = name;
		this.oosaDomain = source;
		this.actionTypes = actionTypes;
		this.childTaskNodes = children;
		this.tf = tf;
		this.rf = rf;
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
	public boolean terminal(State s, Action action) {
        return terminalFunction(action).isTerminal(s);
	}
	
	public MakeWallTF terminalFunction(Action action) {
		Z_HasWallSCT goal = getStateConditionTest(action);
		tf.setGoal(goal);
		return tf;
	}

	@Override
	public RewardFunction rewardFunction(Action action) {
		MakeWallTF tf = terminalFunction(action);
		rf.setTF(tf);
        return rf;
	}

	@Override
	public Object parametersSet(State s) {
		throw new RuntimeException("not implemented");
	}

	public Z_HasWallSCT getStateConditionTest(Action action) {
		String[] params = new String[2];
		if (action instanceof ObjectParameterizedAction) {
			ObjectParameterizedAction a = (ObjectParameterizedAction)action;
			String[] actionParams = a.getObjectParameters();
			for (int i = 0; i < actionParams.length; i++) {
				params[i] = actionParams[i];
			}
		}
		Z_HasWallSCT hasWall = new Z_HasWallSCT(params[0], params[1]);
		System.out.println("in getStateConditionTest!");
		System.out.println(hasWall.getStartName() + " " + hasWall.getEndName());
		return hasWall;
	}


}

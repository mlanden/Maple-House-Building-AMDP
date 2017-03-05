package amdp.house;

import java.util.ArrayList;
import java.util.List;

import amdp.amdpframework.GroundedPropSC;
import amdp.amdpframework.GroundedTask;
import amdp.amdpframework.NonPrimitiveTaskNode;
import amdp.amdpframework.TaskNode;
import amdp.house.level2.HasGoalWallPF;
import amdp.house.level2.MakeWallRF;
import amdp.house.level2.MakeWallState;
import amdp.house.level2.MakeWallTF;
import burlap.mdp.auxiliary.stateconditiontest.StateConditionTest;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.oo.ObjectParameterizedAction;
import burlap.mdp.core.oo.propositional.GroundedProp;
import burlap.mdp.core.oo.propositional.PropositionalFunction;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.oo.OOSADomain;

public class TaskMakeWall extends NonPrimitiveTaskNode {

	public ActionType[] actionTypes;
	public MakeWallTF tf;
	public MakeWallRF rf;
	
	public TaskMakeWall(String name, ActionType[] actionTypes, OOSADomain oosaDomain,
			TaskNode[] childTaskNodes, MakeWallTF tf, MakeWallRF rf) {
		this.name = name;
		this.actionTypes = actionTypes;
		this.oosaDomain = oosaDomain;
		this.childTaskNodes = childTaskNodes;
		this.tf = tf;
		this.rf = rf;
	}
	
	public StateConditionTest getGoalOfAction(Action a) {
		ObjectParameterizedAction action = (ObjectParameterizedAction) a;
		String[] params = action.getObjectParameters();
		PropositionalFunction pf = this.oosaDomain.propFunction(HasGoalWallPF.PF_HAS_GOAL_WALL);
		GroundedProp gp = new GroundedProp(pf, params);
		GroundedPropSC test = new GroundedPropSC(gp);
		return test;
	}

	@Override
	public boolean terminal(State s, Action a) {
		MakeWallState state = (MakeWallState) s;
		tf.setGoalCondition(getGoalOfAction(a));
		return tf.isTerminal(s);
	}

	@Override
	public RewardFunction rewardFunction(Action a) {
		MakeWallTF tf = (MakeWallTF) rf.getTf();
		tf.setGoalCondition(getGoalOfAction(a));
		rf.setTf(tf);
		return rf;
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
	public Object parametersSet(State s) {
		throw new RuntimeException("not implemented");
	}

}

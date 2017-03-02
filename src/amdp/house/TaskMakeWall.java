package amdp.house;

import java.util.ArrayList;
import java.util.List;

import amdp.amdpframework.GroundedTask;
import amdp.amdpframework.NonPrimitiveTaskNode;
import amdp.amdpframework.TaskNode;
import amdp.house.level2.MakeWallRF;
import amdp.house.level2.MakeWallTF;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.action.ActionType;
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

	@Override
	public boolean terminal(State s, Action action) {
//		MakeWallState state = (MakeWallState) s;
//		return (boolean) state.getWall().get(HWall.ATT_FINISHED);
		return tf.isTerminal(s);
	}

	@Override
	public RewardFunction rewardFunction(Action action) {
//		SingleGoalPFRF rf = new SingleGoalPFRF(goal);
//		return rf;
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

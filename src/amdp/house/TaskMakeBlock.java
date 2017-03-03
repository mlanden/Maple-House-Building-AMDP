package amdp.house;

import java.util.ArrayList;
import java.util.List;

import amdp.amdpframework.GroundedTask;
import amdp.amdpframework.NonPrimitiveTaskNode;
import amdp.amdpframework.TaskNode;
import amdp.house.level1.MakeBlockRF;
import amdp.house.level1.MakeBlockState;
import amdp.house.level1.MakeBlockTF;
import amdp.house.objects.HBlock;
import amdp.house.objects.HPoint;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.oo.ObjectParameterizedAction;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.oo.OOSADomain;

public class TaskMakeBlock extends NonPrimitiveTaskNode {

	public ActionType[] actionTypes;
	public MakeBlockTF tf;
	public MakeBlockRF rf;
	
	public TaskMakeBlock(String name, ActionType[] actionTypes, OOSADomain oosaDomain,
			TaskNode[] childTaskNodes, MakeBlockTF tf, MakeBlockRF rf) {
		this.name = name;
		this.actionTypes = actionTypes;
		this.oosaDomain = oosaDomain;
		this.childTaskNodes = childTaskNodes;
		this.tf = tf;
		this.rf = rf;
	}

	@Override
	public boolean terminal(State s, Action a) {
		MakeBlockState state = (MakeBlockState) s;
		ObjectParameterizedAction action = (ObjectParameterizedAction) a;
		String[] params = action.getObjectParameters();
		String destinationName = params[0];
		HPoint destination = (HPoint) state.object(destinationName);
		int bX = (int) destination.get(HPoint.ATT_X);
		int bY = (int) destination.get(HPoint.ATT_Y);
		HBlock goal = new HBlock("block_"+bX+"_"+bY, bX, bY, true, false);
		tf.setGoal(goal);
		return tf.isTerminal(s);
	}

	@Override
	public RewardFunction rewardFunction(Action action) {
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
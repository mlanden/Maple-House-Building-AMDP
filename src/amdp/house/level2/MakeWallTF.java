package amdp.house.level2;

import java.util.List;

import amdp.house.objects.HBlock;
import amdp.house.objects.HPoint;
import amdp.house.objects.HWall;
import burlap.mdp.auxiliary.common.GoalConditionTF;
import burlap.mdp.auxiliary.common.SinglePFTF;
import burlap.mdp.auxiliary.stateconditiontest.StateConditionTest;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.oo.propositional.PropositionalFunction;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.State;
import utils.DynamicGroundedPropSC;
import utils.GoalFailureTF;

public class MakeWallTF extends GoalFailureTF {

	public MakeWallTF(StateConditionTest goalCondition) {
		super(goalCondition);
	}

	@Override
	public boolean satisfiesFailure(State s) {
		return false;
	}

	public void setGoalCondition(StateConditionTest goalCondition) {
		this.goalCondition = goalCondition;
	}

		/*
		MakeWallState state = (MakeWallState) s;
		
		if(goesTooFar(state)) {
			// terminate early, in terms of blocks built
			return true;
		}
		
		return satisfiesGoal(state);
		*/
//	}

	
//	protected HasGoalWallSCT hasFinishedWall = new HasGoalWallSCT();
//	protected HWall goal;
	
//	public HWall getGoal() {
//		return goal;
//	}
//
//	public void setGoal(HWall goal) {
//		this.goal = goal;
//	}
//	
//	public double getBudget(MakeWallState state) {
//		HPoint wallStart = (HPoint) getGoal().get(HWall.ATT_START);
//		HPoint wallEnd = (HPoint) getGoal().get(HWall.ATT_END);
//		// blocks are budgeted to be chebyshev distance * budgetScalar
//		double distance = HPoint.distanceChebyshev(wallStart, wallEnd);
//		distance += 1;
//		double budgetScalar = 1.1;//1.1;
//		double budget = distance * budgetScalar;
//		return budget;
//	}
	
//	public boolean goesTooFar(MakeWallState state) {
		// buggy when going for more than one wall
		// so if want to use budget, will need to be w.r.t. total number of blocks
//		List<ObjectInstance> blocks = state.objectsOfClass(HBlock.CLASS_BLOCK);
//		if (blocks.size() > getBudget(state)) {
//			return true;
//		}
//		return false;
//	}
	
//	public boolean satisfiesGoal(MakeWallState state) {
//		HWall goal = getGoal();
//		if (hasFinishedWall.satisfies(state, goal)) {
//			state.addObject(goal);
//			return true;
//		}
//		return false;
//	}
	

}
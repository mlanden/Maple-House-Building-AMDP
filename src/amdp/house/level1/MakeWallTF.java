package amdp.house.level1;

import java.util.List;

import amdp.house.objects.HBlock;
import amdp.house.objects.HPoint;
import amdp.house.objects.HWall;
import amdp.house.pfs.OldHasWall;
import amdp.house.pfs.IsContiguous;
import burlap.mdp.auxiliary.common.GoalConditionTF;
import burlap.mdp.auxiliary.stateconditiontest.StateConditionTest;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.State;

public class MakeWallTF implements TerminalFunction {

	private HasWall goal;
	
	public MakeWallTF(HasWall goal){
		this.goal = goal;
	}
	
	public void setGoal(HasWall goal) {
		this.goal = goal;
	}
	
	public double getBudget(MakeWallState state) {
//		HPoint wallStart = (HPoint) state.object(goal.getStartName());
//		HPoint wallEnd = (HPoint) state.object(goal.getEndName());
//		int aX = (Integer) wallStart.get(HPoint.ATT_X);
//		int aY = (Integer) wallStart.get(HPoint.ATT_Y);
//		int bX = (Integer) wallEnd.get(HPoint.ATT_X);
//		int bY = (Integer) wallEnd.get(HPoint.ATT_Y);
		int aX = goal.aX;
		int aY = goal.aY;
		int bX = goal.bX;
		int bY = goal.bY;
		// blocks are budgeted to be chebyshev distance * budgetScalar
		double budgetScalar = 1.1;
		double distance = Math.max(Math.abs(bX-aX),Math.abs(bY-aY));
		double budget = distance * budgetScalar;
		return budget;
	}
	
	public boolean goesTooFar(MakeWallState state) {
		List<ObjectInstance> blocks = state.objectsOfClass(HBlock.CLASS_BLOCK);
		if (blocks.size() > getBudget(state)) {
			return true;
		}
		return false;
	}
	
	public boolean satisfiesGoal(MakeWallState state) {
		if (goal == null) {
			throw new RuntimeException("not implemented");
		}
		return goal.satisfies(state);
	}
	
	@Override
	public boolean isTerminal(State s) {
		MakeWallState state = (MakeWallState) s;
		
		if(goesTooFar(state)) {
			// terminate early, in terms of blocks built
			return true;
		}
		
		return satisfiesGoal(state);
	}

}
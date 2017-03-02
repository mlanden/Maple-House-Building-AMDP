package amdp.house.level2;

import java.util.List;

import amdp.house.objects.HBlock;
import amdp.house.objects.HPoint;
import amdp.house.objects.HWall;
import amdp.house.pfs.IsContiguous;
import burlap.mdp.auxiliary.common.GoalConditionTF;
import burlap.mdp.auxiliary.stateconditiontest.StateConditionTest;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.State;

public class MakeWallTF implements TerminalFunction {
	
	public MakeWallTF() {
		
	}
	
	public double getBudget(MakeWallState state) {
		HWall goal = state.getWall();
		HPoint wallStart = (HPoint) goal.get(HWall.ATT_START);
		HPoint wallEnd = (HPoint) goal.get(HWall.ATT_END);
		int aX = (Integer) wallStart.get(HPoint.ATT_X);
		int aY = (Integer) wallStart.get(HPoint.ATT_Y);
		int bX = (Integer) wallEnd.get(HPoint.ATT_X);
		int bY = (Integer) wallEnd.get(HPoint.ATT_Y);
		// blocks are budgeted to be chebyshev distance * budgetScalar
		double budgetScalar = 1.1;//1.1;
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
		HWall goal = state.getWall();
		if ((boolean) goal.get(HWall.ATT_FINISHED)) {
			return true;
		}
		return false;
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
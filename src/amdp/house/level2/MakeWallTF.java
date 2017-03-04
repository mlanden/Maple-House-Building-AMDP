package amdp.house.level2;

import java.util.List;

import amdp.house.objects.HBlock;
import amdp.house.objects.HPoint;
import amdp.house.objects.HWall;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.State;

public class MakeWallTF implements TerminalFunction {
	
	public HasFinishedWall hasFinishedWall = new HasFinishedWall();
	
	protected HWall goal;
	
	public MakeWallTF(HWall goal) {
		this.goal = goal;
	}
	
	public HWall getGoal() {
		return goal;
	}

	public void setGoal(HWall goal) {
		this.goal = goal;
	}
	
	public double getBudget(MakeWallState state) {
		HPoint wallStart = (HPoint) getGoal().get(HWall.ATT_START);
		HPoint wallEnd = (HPoint) getGoal().get(HWall.ATT_END);
		// blocks are budgeted to be chebyshev distance * budgetScalar
		double distance = HPoint.distanceChebyshev(wallStart, wallEnd);
		distance += 1;
		double budgetScalar = 1.1;//1.1;
		double budget = distance * budgetScalar;
		return budget;
	}
	
	public boolean goesTooFar(MakeWallState state) {
		// buggy when going for more than one wall
		// so if want to use budget, will need to be w.r.t. total number of blocks
//		List<ObjectInstance> blocks = state.objectsOfClass(HBlock.CLASS_BLOCK);
//		if (blocks.size() > getBudget(state)) {
//			return true;
//		}
		return false;
	}
	
	public boolean satisfiesGoal(MakeWallState state) {
		HWall goal = getGoal();
		if (hasFinishedWall.satisfies(state, goal)) {
			return true;
		}
		return false;
//		HWall goal =  state.getGoalWall();
//		if (goal == null) {
//			System.err.println("Warning: goal wall is null");
//			return false;
//		}
//		if ((boolean) goal.get(HWall.ATT_FINISHED)) {
//			return true;
//		}
//		return false;
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
package amdp.house.level1;

import java.util.List;

import amdp.house.objects.HBlock;
import amdp.house.objects.HPoint;
import amdp.house.objects.HWall;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.State;
import compositeobject.IsContiguous;

public class MakeWallTF implements TerminalFunction {

	public HPoint wallStart;
	public HPoint wallEnd;
	public IsContiguous isContiguous;
	
	public MakeWallTF(HPoint wallStart, HPoint wallEnd){
		this.wallStart = wallStart;
		this.wallEnd = wallEnd;
		this.isContiguous = new IsContiguous();
	}
	
	public boolean satisfiesGoal(MakeWallState state) {
		if (isContiguous.isTrue(state, wallStart, wallEnd)) {
			// honestly do not think that this is the best spot for this
			// but whatever... 
			HWall wall = new HWall("wall", wallStart, wallEnd);
			state.addObject(wall);
			return true;
		}
		return false;
	}
	
	public boolean goesTooFar(MakeWallState state) {
		int aX = (Integer) wallStart.get(HPoint.ATT_X);
		int aY = (Integer) wallStart.get(HPoint.ATT_Y);
		int bX = (Integer) wallEnd.get(HPoint.ATT_X);
		int bY = (Integer) wallEnd.get(HPoint.ATT_Y);
		List<ObjectInstance> blocks = state.objectsOfClass(HBlock.CLASS_BLOCK);
		// blocks are budgeted to be chebyshev distance * budgetScalar
		double budgetScalar = 1.1;
		double distance = Math.max(Math.abs(bX-aX),Math.abs(bY-aY));
		if (blocks.size() > distance * budgetScalar) {
			return true;
		}
		return false;
	}
	
	public boolean isTerminal(State s) {
		MakeWallState state = (MakeWallState) s;
		
		if(goesTooFar(state)) {
			// terminate early, in terms of blocks built
			return true;
		}
		
		return satisfiesGoal(state);
	}

}
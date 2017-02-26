package amdp.house.level1;

import amdp.house.objects.HWall;
import burlap.mdp.auxiliary.stateconditiontest.StateConditionTest;
import burlap.mdp.core.state.State;

public class HasWall implements StateConditionTest {

	public int aX;
	public int aY;
	public int bX;
	public int bY;
	
	public HasWall(int aX, int aY, int bX, int bY) {
		this.aX = aX;
		this.aY = aY;
		this.bX = bX;
		this.bY = bY;
	}
	
	@Override
	public boolean satisfies(State s) {
		MakeWallState state = (MakeWallState) s;
		
		if (!state.blockAt(aX, aY)) {
			return false;
		}
		if (!state.blockAt(bX, bY)) {
			return false;
		}
		
		///
		System.err.println("Warning, adding wall in HasWall -- fix");
		HWall wall = new HWall("newWall", state.getPointAt(aX,aY), state.getPointAt(bX,bY));
		state.addObject(wall);
		
		return true;
	}

}

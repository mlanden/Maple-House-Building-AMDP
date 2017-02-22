package amdp.house.level2;

import amdp.house.level1.MakeWallState;
import amdp.house.objects.HWall;
import burlap.mdp.auxiliary.StateMapping;
import burlap.mdp.core.state.State;

public class MakeRoomStateMapping implements StateMapping {
	
	@Override
	public State mapState(State s) {
		MakeWallState state = (MakeWallState) s;
		
		MakeRoomState out = new MakeRoomState(state.getWidth(), state.getHeight());
		HWall wall = state.getFirstWall();
		if (wall != null) {
			out.addObject(wall);
		}
		
		return out;
	}

}

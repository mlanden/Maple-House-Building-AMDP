package amdp.house.level2;

import amdp.house.level1.MakeWallState;
import amdp.house.objects.HWall;
import burlap.mdp.auxiliary.StateMapping;
import burlap.mdp.core.oo.state.OOStateUtilities;
import burlap.mdp.core.state.State;

public class MakeRoomStateMapping implements StateMapping {
	
	@Override
	public State mapState(State s) {
		MakeWallState state = (MakeWallState) s;
		
		MakeRoomState out = new MakeRoomState(state.getWidth(), state.getHeight());
		HWall wall = state.getWall();
		if (wall != null && (boolean) wall.get(HWall.ATT_FINISHED)) {
			out.addObject(wall);
		}
		
		return out;
	}

}

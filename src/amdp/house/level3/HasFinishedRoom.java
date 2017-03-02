package amdp.house.level3;

import java.util.List;

import amdp.house.objects.HPoint;
import amdp.house.objects.HRoom;
import burlap.mdp.auxiliary.stateconditiontest.StateConditionTest;
import burlap.mdp.core.state.State;

public class HasFinishedRoom {

	public boolean satisfies(State s, HRoom goal) {
		MakeRoomState state = (MakeRoomState) s;
		
		List<HPoint> corners = (List<HPoint>) goal.get(HRoom.ATT_CORNERS);//state.getRoom().get(HRoom.ATT_CORNERS);
		for (HPoint corner : corners) {
			if (!state.isWallCorner(corner)) {
				return false;
			}
		}
		
		return true;
	}

}

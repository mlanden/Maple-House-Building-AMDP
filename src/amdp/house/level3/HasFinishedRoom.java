package amdp.house.level3;

import java.util.List;

import amdp.house.objects.HPoint;
import amdp.house.objects.HRoom;
import burlap.mdp.auxiliary.stateconditiontest.StateConditionTest;
import burlap.mdp.core.state.State;

public class HasFinishedRoom implements StateConditionTest {

	@Override
	public boolean satisfies(State s) {
		MakeRoomState state = (MakeRoomState) s;
		
		List<HPoint> corners = (List<HPoint>) state.getRoom().get(HRoom.ATT_CORNERS);
		for (HPoint corner : corners) {
			if (!state.isWallCorner(corner)) {
				return false;
			}
		}
		
		return true;
	}

}

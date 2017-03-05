package amdp.house.level3;

import java.util.List;

import amdp.house.objects.HPoint;
import amdp.house.objects.HRoom;
import burlap.mdp.core.state.State;

public class HasFinishedRoom {

	public static int getNumWallsRemaining(MakeRoomState state, HRoom goal) {
		@SuppressWarnings("unchecked")
		List<HPoint> corners = (List<HPoint>) goal.get(HRoom.ATT_CORNERS);//state.getRoom().get(HRoom.ATT_CORNERS);
		int wallsRemaining = corners.size();
		if (corners.size() == 2) { wallsRemaining = 1; } // two corners, only one wall
		for (int i = 0; i < corners.size(); i++) {
			int j = (i + 1) % corners.size(); // wrap to zeroth element
			if (state.areContiguousInLine(corners.get(i), corners.get(j))) {
				wallsRemaining -= 1;
			}
		}
		return Math.max(0, wallsRemaining);
	}
	
	public boolean satisfies(State s, HRoom goal) {
		MakeRoomState state = (MakeRoomState) s;

		@SuppressWarnings("unchecked")
		List<HPoint> corners = (List<HPoint>) goal.get(HRoom.ATT_CORNERS);//state.getRoom().get(HRoom.ATT_CORNERS);
//		int wallsRemaining = corners.size();
//		if (corners.size() == 2) { wallsRemaining = 1; } // two corners, only one wall
		for (int i = 0; i < corners.size(); i++) {
			int j = (i + 1) % corners.size(); // wrap to zeroth element
			if (!state.areContiguousInLine(corners.get(i), corners.get(j))) {
				return false;
			}
		}
		return true;
	}

}

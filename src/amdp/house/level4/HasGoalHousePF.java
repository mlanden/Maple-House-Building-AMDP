package amdp.house.level4;

import java.util.List;

import amdp.house.objects.HHouse;
import amdp.house.objects.HRoom;
import burlap.mdp.core.oo.propositional.PropositionalFunction;
import burlap.mdp.core.oo.state.OOState;

public class HasGoalHousePF extends PropositionalFunction {

	public static final String PF_HAS_GOAL_HOUSE = "pfHasGoalHouse";
	
	public HHouse goal;
	
	public HasGoalHousePF(HHouse goal) {
		super(PF_HAS_GOAL_HOUSE, new String[]{});
		this.goal = goal;
	}

	@Override
	public boolean isTrue(OOState s, String... params) {
		MakeHouseState state = (MakeHouseState) s;
		List<HRoom> rooms = state.getRooms();
		if (rooms.size() > 0) {
			HRoom outside = state.outside;
			for (HRoom room : rooms) {
				if (!state.spacesLinked(room, outside)) {
					return false;
				}
			}
			return true;
		}
		return false;
	}

}

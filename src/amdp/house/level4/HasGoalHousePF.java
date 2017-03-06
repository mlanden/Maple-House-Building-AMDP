package amdp.house.level4;

import amdp.house.objects.HHouse;
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
		if (state.getRooms().size() > 0) {
			return true;
		}
		return false;
	}

}

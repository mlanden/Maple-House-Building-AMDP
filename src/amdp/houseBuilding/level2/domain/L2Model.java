package amdp.houseBuilding.level2.domain;

import java.util.ArrayList;
import java.util.List;

import amdp.houseBuilding.level1.state.L1State;
import amdp.houseBuilding.level2.domain.L2DomainGenerator.MakeWallActionType.MakeWallAction;
import burlap.mdp.core.StateTransitionProb;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.statemodel.FullStateModel;
import compositeObjectDomain.Wall;

public class L2Model implements FullStateModel{

	public State sample(State s, Action a) {
		List<StateTransitionProb> stpList = this.stateTransitions(s, a);
        double roll = Math.random();
        double curSum = 0.;
        for (int i = 0; i < stpList.size(); i++) {
            curSum += stpList.get(i).p;
            if (roll < curSum) {
                return stpList.get(i).s;
            }
        }
        throw new RuntimeException("Probabilities don't sum to 1.0: " + curSum);
	}

	public List<StateTransitionProb> stateTransitions(State s, Action a) {
		List<StateTransitionProb> stp = new ArrayList<StateTransitionProb>();
		L1State st = (L1State)s;
		String name = a.actionName().split("_")[0];
		if(name.equals(L2DomainGenerator.ACTION_MAKEWALL)){
			MakeWallAction makAct = (MakeWallAction) a;
			List<Wall> walls = st.touchWalls();
			Wall w = new Wall(makAct.startX, makAct.startY, makAct.endX, makAct.endY,
					makAct.length, "wall" + walls.size());
			walls.add(w);
			stp.add(new StateTransitionProb(st, 1));
		}
		return stp;
	}

}

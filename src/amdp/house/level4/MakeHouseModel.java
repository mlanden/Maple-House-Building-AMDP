package amdp.house.level4;

import java.util.ArrayList;
import java.util.List;

import amdp.house.objects.HPoint;
import amdp.house.objects.HRoom;
import burlap.mdp.core.StateTransitionProb;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.statemodel.FullStateModel;

public class MakeHouseModel implements FullStateModel {
	
	@Override
	public State sample(State s, Action a) {
		s = s.copy();
		String name = a.actionName();
		if (name.equals(MakeHouse.ACTION_MAKE_ROOM)) {
			return makeRoom(s, a);
		}
		throw new RuntimeException("Unknown action " + name);
	}
	
	public State makeRoom(State s, Action a) {
		MakeHouseState state = (MakeHouseState) s;
//		ObjectParameterizedAction action = (ObjectParameterizedAction) a;
//		if (action.actionName() != MakeRoom.ACTION_MAKE_WALL) {
//			throw new RuntimeException("incorrect action passed to makeWall");
//		}
//		HPoint pointA = (HPoint) state.object(action.getObjectParameters()[0]);
//		HPoint pointB = (HPoint) state.object(action.getObjectParameters()[1]);
		int numRooms = state.getRooms().size();
		String name = HRoom.CLASS_ROOM + numRooms;
		
		// obviously temp debug
		List<HPoint> corners = new ArrayList<HPoint>();
		HPoint p0 = new HPoint("p0", 1, 1, false); corners.add(p0);
		HPoint p1 = new HPoint("p1", 1, 3, false); corners.add(p1);
		HPoint p2 = new HPoint("p2", 3, 3, false); corners.add(p2);
		HPoint p3 = new HPoint("p3", 3, 1, false); corners.add(p3);
		
		HRoom room = new HRoom(name, corners, true);
		state.addObject(room);
//		if (hasFinishedRoom.satisfies(state)) {
//			HRoom room = state.touchRoom();
//			room.set(HRoom.ATT_FINISHED, true);
//		}
		return s;
	}
	
	@Override
	public List<StateTransitionProb> stateTransitions(State s, Action a) {
		
		int actionIndex = actionInd(a.actionName());
		double[] transitionProbs = new double[MakeHouse.NUM_ACTIONS];
		for (int i = 0; i < MakeHouse.NUM_ACTIONS; i++) {
			// deterministic transition based on own action
			transitionProbs[i] = actionIndex == i ? 1. : 0.;
		}
		
		List <StateTransitionProb> transitions = new ArrayList<StateTransitionProb>();
		for(int i = 0; i < MakeHouse.NUM_ACTIONS; i++){
			double p = transitionProbs[i];
			if(p == 0.){
				continue; //cannot transition in this direction
			}
			State ns = sample(s, a);

			//make sure this direction doesn't actually stay in the same place and replicate another no-op
			boolean isNew = true;
			for(StateTransitionProb tp : transitions){
				if(tp.s.equals(ns)){
					isNew = false;
					tp.p += p;
					break;
				}
			}
			if(isNew){
				StateTransitionProb tp = new StateTransitionProb(ns, p);
				transitions.add(tp);
			}
		}
		return transitions;
	}


	protected int actionInd(String name){
		if(name.equals(MakeHouse.ACTION_MAKE_ROOM)){
			return 0;
		}
		throw new RuntimeException("Unknown action " + name);
	}

}

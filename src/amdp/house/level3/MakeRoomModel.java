package amdp.house.level3;

import java.util.ArrayList;
import java.util.List;

import amdp.house.objects.HPoint;
import amdp.house.objects.HRoom;
import amdp.house.objects.HWall;
import burlap.mdp.core.StateTransitionProb;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.oo.ObjectParameterizedAction;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.statemodel.FullStateModel;

public class MakeRoomModel implements FullStateModel {

	public HasFinishedRoom hasFinishedRoom = new HasFinishedRoom();
	
	@Override
	public State sample(State s, Action a) {
		s = s.copy();
		String name = a.actionName();
		if (name.equals(MakeRoom.ACTION_MAKE_WALL)) {
			return makeWall(s, a);
		}
		throw new RuntimeException("Unknown action " + name);
	}
	
	public State makeWall(State s, Action a) {
		MakeRoomState state = (MakeRoomState) s;
		ObjectParameterizedAction action = (ObjectParameterizedAction) a;
		if (action.actionName() != MakeRoom.ACTION_MAKE_WALL) {
			throw new RuntimeException("incorrect action passed to makeWall");
		}
		HPoint pointA = (HPoint) state.object(action.getObjectParameters()[0]);
		HPoint pointB = (HPoint) state.object(action.getObjectParameters()[1]);
		int numWalls = state.getNumWalls();
		String name = HWall.CLASS_WALL + numWalls;
		HWall wall = new HWall(name, pointA, pointB, true);
		state.addObject(wall);
		if (hasFinishedRoom.satisfies(state)) {
			HRoom room = state.touchRoom();
			room.set(HRoom.ATT_FINISHED, true);
		}
		
		return s;
	}
	
	@Override
	public List<StateTransitionProb> stateTransitions(State s, Action a) {
		
		int actionIndex = actionInd(a.actionName());
		double[] transitionProbs = new double[MakeRoom.NUM_ACTIONS];
		for (int i = 0; i < MakeRoom.NUM_ACTIONS; i++) {
			// deterministic transition based on own action
			transitionProbs[i] = actionIndex == i ? 1. : 0.;
		}
		
		List <StateTransitionProb> transitions = new ArrayList<StateTransitionProb>();
		for(int i = 0; i < MakeRoom.NUM_ACTIONS; i++){
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
		if(name.equals(MakeRoom.ACTION_MAKE_WALL)){
			return 0;
		}
		throw new RuntimeException("Unknown action " + name);
	}

}

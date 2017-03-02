package amdp.house.level2;

import java.util.ArrayList;
import java.util.List;

import amdp.house.objects.HAgent;
import amdp.house.objects.HBlock;
import amdp.house.objects.HPoint;
import amdp.house.objects.HWall;
import burlap.mdp.core.StateTransitionProb;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.oo.ObjectParameterizedAction;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.statemodel.FullStateModel;
import burlap.mdp.singleagent.oo.ObjectParameterizedActionType;

public class MakeWallModel implements FullStateModel {

	public HasFinishedWall hasFinishedWall = new HasFinishedWall();
	
	@Override
	public State sample(State s, Action a) {
		s = s.copy();
		String name = a.actionName();
		if (name.equals(MakeWall.ACTION_MAKE_BLOCK)) {
			return makeBlock(s, a);
		}
		throw new RuntimeException("Unknown action " + name);
	}
	
	public State makeBlock(State s, Action a) {
		MakeWallState state = (MakeWallState) s;
		ObjectParameterizedAction action = (ObjectParameterizedAction)a;
		String[] params = action.getObjectParameters();
		HPoint destination = (HPoint) state.object(params[0]);
		int newX = (int) destination.get(HPoint.ATT_X);
		int newY = (int) destination.get(HPoint.ATT_Y);
		
		int agentX = (Integer) state.getAgent().get(HAgent.ATT_X);
		int agentY = (Integer) state.getAgent().get(HAgent.ATT_Y);

		// must stay within grid
		if(state.isOutOfBounds(newX, newY)){
			newX = agentX;
			newY = agentY;
		}
		
		HAgent nAgent = state.touchAgent();
		nAgent.set(HAgent.ATT_X, newX);
		nAgent.set(HAgent.ATT_Y, newY);
		
		s = build(s, 0, 0);
		
		return s;
	}
	
	public State build(State s, int dx, int dy) {
		MakeWallState state = (MakeWallState) s;
		
		int agentX = (Integer) state.getAgent().get(HAgent.ATT_X);
		int agentY = (Integer) state.getAgent().get(HAgent.ATT_Y);

		int newX = agentX+dx;
		int newY = agentY+dy;

		// must stay within grid
		if(state.isOutOfBounds(newX, newY)){
			newX = agentX;
			newY = agentY;
		}
		
		if (!state.isOpen(newX, newY)){
			// do nothing
		} else {
//			HBlock newBlock = new HBlock(HBlock.CLASS_BLOCK, newX, newY, true);
			// the name of the new object should be unique to that object
			// that is, no two, newly added objects should ever have same name/ID
			String blockName = HBlock.CLASS_BLOCK + "_" + newX + "_" + newY;
			HBlock newBlock = new HBlock(blockName, newX, newY, true, false);
			s = state.addObject((ObjectInstance)newBlock);
			if(hasFinishedWall.satisfies(state)) {
				HWall wall = state.touchWall();
				wall.set(HWall.ATT_FINISHED, true);
			}
		}
		return s;
	}
	
	@Override
	public List<StateTransitionProb> stateTransitions(State s, Action a) {
		
		int actionIndex = actionInd(a.actionName());
		double[] transitionProbs = new double[MakeWall.NUM_ACTIONS];
		for (int i = 0; i < MakeWall.NUM_ACTIONS; i++) {
			// deterministic transition based on own action
			transitionProbs[i] = actionIndex == i ? 1. : 0.;
		}
		
		List <StateTransitionProb> transitions = new ArrayList<StateTransitionProb>();
		for(int i = 0; i < MakeWall.NUM_ACTIONS; i++){
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
		if(name.equals(MakeWall.ACTION_MAKE_BLOCK)){
			return 0;
		} else {
			throw new RuntimeException("Unknown action " + name);
		}
	}

}
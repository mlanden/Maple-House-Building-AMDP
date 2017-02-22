package amdp.house.level1;

import java.util.ArrayList;
import java.util.List;

import amdp.house.objects.HAgent;
import amdp.house.objects.HBlock;
import burlap.mdp.core.StateTransitionProb;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.statemodel.FullStateModel;

public class MakeWallModel implements FullStateModel {

	@Override
	public State sample(State s, Action a) {
		s = s.copy();
		String name = a.actionName();
		if (name.equals(MakeWall.ACTION_NORTH)
 		 || name.equals(MakeWall.ACTION_SOUTH)
 		 || name.equals(MakeWall.ACTION_EAST)
		 || name.equals(MakeWall.ACTION_WEST)) {
			int direction = actionInd(name);
			int [] delta = MakeWallModel.movementDirectionFromIndex(direction);
			return move(s, delta[0], delta[1]);
		} else if (name.equals(MakeWall.ACTION_BUILD)) {
			return build(s, 0, 0);
		}
		throw new RuntimeException("Unknown action " + name);
	}
	
	public State move(State s, int dx, int dy) {
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
		
		HAgent nAgent = state.touchAgent();
		nAgent.set(HAgent.ATT_X, newX);
		nAgent.set(HAgent.ATT_Y, newY);
		
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
			HBlock newBlock = new HBlock(HBlock.CLASS_BLOCK, newX, newY, true);
			s = state.addObject((ObjectInstance)newBlock);
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
		if(name.equals(MakeWall.ACTION_NORTH)){
			return 0;
		} else if(name.equals(MakeWall.ACTION_SOUTH)){
			return 1;
		} else if(name.equals(MakeWall.ACTION_EAST)){
			return 2;
		} else if(name.equals(MakeWall.ACTION_WEST)){
			return 3;
		} else if(name.equals(MakeWall.ACTION_BUILD)){
			return 4;
		} else {
			throw new RuntimeException("Unknown action " + name);
		}
	}

	protected static int [] movementDirectionFromIndex(int i){

		int [] result = null;
		
		switch (i) {
			case 0:
				result = new int[]{0,1};
				break;

			case 1:
				result = new int[]{0,-1};
				break;

			case 2:
				result = new int[]{1,0};
				break;

			case 3:
				result = new int[]{-1,0};
				break;
				
			case 4:
				result = new int[]{0,0};

			default:
				break;
		}

		return result;
	}
}

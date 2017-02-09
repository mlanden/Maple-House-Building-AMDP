package amdp.houseBuilding.level1.domain;

import java.util.ArrayList;
import java.util.List;

import amdp.houseBuilding.level1.state.L1Agent;
import amdp.houseBuilding.level1.state.L1State;
import burlap.mdp.core.StateTransitionProb;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.statemodel.FullStateModel;
import compositeObjectDomain.Block;

import static amdp.houseBuilding.level1.domain.L1DomainGenerator.ACTION_NORTH;
import static amdp.houseBuilding.level1.domain.L1DomainGenerator.ACTION_EAST;
import static amdp.houseBuilding.level1.domain.L1DomainGenerator.ACTION_SOUTH;
import static amdp.houseBuilding.level1.domain.L1DomainGenerator.ACTION_WEST;
import static amdp.houseBuilding.level1.domain.L1DomainGenerator.ACTION_PUTBLOCK;
import static compositeObjectDomain.CompObjDomain.VAR_X;
import static compositeObjectDomain.CompObjDomain.VAR_Y;

public class L1Model implements FullStateModel{
	
	//action numbers
	public static final int NORTH_ID = 0;
	public static final int EAST_ID = 1;
	public static final int SOUTH_ID = 2;
	public static final int WEST_ID = 3;
	public static final int PUTBLOCK_ID = 4;
	
	protected double[][] transitionProbabilities;
	protected int numActions = 5;
	
	public L1Model(boolean stocastic){
		transitionProbabilities = new double[numActions][numActions];
		if(stocastic){
			stocasticProbabilities();
		}else{
			deterministicProbabilities();
		}
	}
	
	private void stocasticProbabilities(){
		for(int target = 0; target < numActions; target++){
			for(int execute = 0; execute < numActions; execute++){
				transitionProbabilities[target][execute] = target == execute ? 0.8 : 0.2 / numActions;
			}
		}
	}
	
	private void deterministicProbabilities(){
		for(int target = 0; target < numActions; target++){
			for(int execute = 0; execute < numActions; execute++){
				transitionProbabilities[target][execute] = target == execute ? 1 : 0;
			}
		}
	}
	
	public State sample(State s, Action a) {
		List<StateTransitionProb> stp = stateTransitions(s, a);
		
		double roll = Math.random(), sum = 0;
		for(int i = 0; i < stp.size(); i++){
			sum += stp.get(i).p;
			if(sum > roll){
				return stp.get(i).s;
			}
		}
		throw new RuntimeException("Probabilities don't sum to 1.0: " + sum);
	}

	public List<StateTransitionProb> stateTransitions(State s, Action a) {
		List<StateTransitionProb> stp = new ArrayList<StateTransitionProb>();
		int actionInx = actionInx(a.actionName());
		
		for(int i = 0; i < numActions; i++){
			L1State newS = ((L1State)s).copy();
			L1Agent agent = newS.touchAgent();
			int[][] map = newS.getMap();
			int x = agent.x;
			int y = agent.y;
			
			if(i <= WEST_ID){
				int dx = 0, dy = 0;
				if(i == NORTH_ID){
					dx = 0;
					dy = 1;
				}else if(i == EAST_ID){
					dx = 1;
					dy = 0;
				}else if(i == SOUTH_ID){
					dx = 0;
					dy = -1;
				}else if(i == WEST_ID){
					dx = -1;
					dy = 0;
				}
						
				int nx = x + dx;
				int ny = y + dy;
				
				//check boundry
				if(nx < 0 || nx >= map.length || ny < 0 || ny >= map[0].length){
					nx = x;
					ny = y;
				}
				
				agent.x = nx;
				agent.y = ny;
			}else if(i == PUTBLOCK_ID){
				Block bk = new Block(x, y);
				map[x][y] = 1;
				newS.addObject(bk);
			}
			
			stp.add(new StateTransitionProb(newS, transitionProbabilities[actionInx][i]));
		}
		return stp;
	}

	public int actionInx(String name){
		if(name.equals(ACTION_NORTH))
			return NORTH_ID;
		else if(name.equals(ACTION_EAST))
			return EAST_ID;
		else if(name.endsWith(ACTION_SOUTH))
			return SOUTH_ID;
		else if(name.equals(ACTION_WEST))
			return WEST_ID;
		else if(name.equals(ACTION_PUTBLOCK))
			return PUTBLOCK_ID;
		
		return -1;
	}
}

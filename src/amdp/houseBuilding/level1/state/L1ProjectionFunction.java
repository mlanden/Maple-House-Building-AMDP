package amdp.houseBuilding.level1.state;

import java.util.ArrayList;
import java.util.List;

import burlap.mdp.auxiliary.StateMapping;
import burlap.mdp.core.state.State;
import compositeObjectDomain.AtomicObject;
import compositeObjectDomain.CompObjAgent;
import compositeObjectDomain.CompObjState;
import compositeObjectDomain.Wall;

public class L1ProjectionFunction implements StateMapping{

	@Override
	public L1State mapState(State s) {
		CompObjState state = (CompObjState) s;
		CompObjAgent L0Agent = state.agent;
		
		L1Agent agent = new L1Agent(L0Agent.x, L0Agent.y, L0Agent.name());
		L1State ns = new L1State(agent, state.getMap(), new ArrayList<Wall>());
		
		List<AtomicObject> objs = state.objects;
		
		//for each object, let the state add to walls
		for(AtomicObject obj : objs){
			ns.addObject(obj);
		}
		return ns;
	}

}

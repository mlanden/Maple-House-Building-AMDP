package amdp.houseBuilding.experiments;

import amdp.amdpframework.TaskNode; 
import amdp.houseBuilding.taskNodes.RootTaskNode;
import amdp.houseBuilding.level1.domain.L1DomainGenerator;
import amdp.houseBuilding.level1.state.L1ProjectionFunction;
import amdp.houseBuilding.level1.state.L1State;
import amdp.houseBuilding.level2.domain.L2DomainGenerator;
import amdp.houseBuilding.taskNodes.MoveTask;
import amdp.houseBuilding.taskNodes.PutBlockTask;
import amdp.houseBuilding.taskNodes.makeWallTask;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.oo.OOSADomain;
import compositeObjectDomain.CompObjDomain;
import compositeObjectDomain.CompObjState;

public class AMDPAssembler { 

	public static TaskNode assebleAMDP(int goalLength, boolean stoc){
		CompObjDomain l0Dom = new CompObjDomain(5, 5);
		OOSADomain base = l0Dom.generateDomain();
		State initial = new CompObjState(0, 0, new int[5][5]);
		
		L1DomainGenerator l1DomGen = new L1DomainGenerator(goalLength, stoc);
		OOSADomain l1Dom = l1DomGen.generateDomain();
		
		L1ProjectionFunction l1sp = new L1ProjectionFunction();
		L1State l1State = l1sp.mapState(initial);
		
		L2DomainGenerator l2DomGen = new L2DomainGenerator();
		OOSADomain l2Dom = l2DomGen.generateDomain();
		
		ActionType aNorth = base.getAction(CompObjDomain.ACTION_NORTH);
		ActionType aEast = base.getAction(CompObjDomain.ACTION_EAST);
		ActionType aSouth = base.getAction(CompObjDomain.ACTION_SOUTH);
		ActionType aWest = base.getAction(CompObjDomain.ACTION_WEST);
		ActionType aBlock = base.getAction(CompObjDomain.ACTION_PLACEBLOCK);
		ActionType makeWall = l2Dom.getAction(L2DomainGenerator.ACTION_MAKEWALL);
		
		TaskNode tn = new MoveTask(aNorth);
		TaskNode te = new MoveTask(aEast);
		TaskNode ts = new MoveTask(aSouth);
		TaskNode tw = new MoveTask(aWest);
		TaskNode tb = new PutBlockTask(aBlock);
		
		TaskNode[] makeWalllTasks = new TaskNode[]{tn, te, ts, tw, tb};
		
		TaskNode makeWallTask = new makeWallTask(makeWall, l1Dom, makeWalllTasks, l1DomGen.getRF(), l2DomGen.getTF());		
		
		TaskNode root = new RootTaskNode("root", new TaskNode[]{makeWallTask}, l2Dom, l2DomGen.getTF(), l2DomGen.getRF());
		return root;
	}
}
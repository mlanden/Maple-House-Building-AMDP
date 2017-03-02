package amdp.house;

import java.util.ArrayList;
import java.util.List;

import amdp.amdpframework.AMDPAgent;
import amdp.amdpframework.AMDPPolicyGenerator;
import amdp.amdpframework.GroundedTask;
import amdp.amdpframework.NonPrimitiveTaskNode;
import amdp.amdpframework.TaskNode;
import amdp.house.base.HouseBase;
import amdp.house.level1.MakeWall;
import amdp.house.level1.MakeWallRF;
import amdp.house.level1.MakeWallTF;
import amdp.house.level1.TaskLeaf;
import amdp.house.level2.MakeRoom;
import amdp.house.level2.MakeRoomRF;
import amdp.house.level2.MakeRoomTF;
import amdp.house.objects.HPoint;
import amdp.house.objects.HRoom;
import amdp.house.objects.HWall;
import amdp.taxiamdpdomains.testingtools.BoundedRTDPForTests;
import amdp.taxiamdpdomains.testingtools.MutableGlobalInteger;
import burlap.behavior.policy.Policy;
import burlap.behavior.policy.PolicyUtils;
import burlap.behavior.singleagent.Episode;
import burlap.mdp.auxiliary.common.NullTermination;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.oo.state.OOState;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.common.UniformCostRF;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.oo.OOSADomain;
import burlap.statehashing.HashableStateFactory;
import burlap.statehashing.simple.SimpleHashableStateFactory;

public class AMDPAssembler { 

	// bad -- fix this location
	public static List<BoundedRTDPForTests> brtdpList = new ArrayList<BoundedRTDPForTests>();
	public static MutableGlobalInteger bellmanBudget = new MutableGlobalInteger(-1);
	public static MutableGlobalInteger bellmanBudgetL0 = new MutableGlobalInteger(-1);
	public static MutableGlobalInteger bellmanBudgetL1 = new MutableGlobalInteger(-1);
	public static MutableGlobalInteger bellmanBudgetL2 = new MutableGlobalInteger(-1);

	public static TaskNode assembleAMDP(){
		HashableStateFactory hashingFactory = new SimpleHashableStateFactory(true);
		
		// goal is to build this room
		HPoint p0 = new HPoint("p0", 0, 0, false);
		HPoint p1 = new HPoint("p1", 1, 1, false);
//		HPoint p2 = new HPoint("p2", 2, 2, false);
//		HPoint p3 = new HPoint("p3", 2, 0, false);
		List<HPoint> corners = new ArrayList<HPoint>();
		corners.add(p0); corners.add(p1); // corners.add(p2); corners.add(p3);
		
		HRoom goalRoom = new HRoom("goalRoom", corners, false);
		int width = 5;
		int height = 5;
		TerminalFunction tfBase = new NullTermination();
		RewardFunction rfBase = new UniformCostRF();
		HouseBase genBase = new HouseBase(rfBase, tfBase, width, height);
		OOSADomain domainBase = genBase.generateDomain();
		OOSADomain domainEnv = genBase.generateDomain();
		OOState initial = genBase.getInitialState(goalRoom);

		// goal is to build this wall
//		HPoint start = new HPoint("wallStart", 0, 0, false);
//		HPoint end = new HPoint("wallEnd", 3, 2, false);
//		HWall goalWall = new HWall("goalWall", start, end, false);
		
		// make wall AMDP
		TerminalFunction tfWall = new MakeWallTF();
		double rewardGoal = 1000;
		double rewardDefault = -.1;
		double rewardFailure = rewardDefault * 2;
		RewardFunction rfWall = new MakeWallRF((MakeWallTF) tfWall, rewardGoal, rewardDefault, rewardFailure);
		MakeWall genWall = new MakeWall(rfWall, tfWall, width, height);
		OOSADomain domainWall = genWall.generateDomain();
//		OOSADomain domainEnv = genWall.generateDomain();
//		OOState initial = genWall.getInitialState(goalWall);

		// make room AMDP
		TerminalFunction tfRoom = new MakeRoomTF();
		RewardFunction rfRoom = new MakeRoomRF((MakeRoomTF) tfRoom, 1000.0, 0.0, 0.0);
		MakeRoom genRoom = new MakeRoom(rfRoom, tfRoom, width, height);
		OOSADomain domainRoom = genRoom.generateDomain();
		OOState initialStateRoom = genRoom.getInitialState(goalRoom);
		
		List<AMDPPolicyGenerator> pgList = new ArrayList<AMDPPolicyGenerator>();
		pgList.add(0, new MakeWallPolicyGenerator(domainWall));
		pgList.add(1, new MakeRoomPolicyGenerator(domainRoom));

		// base actions
		ActionType aNorth = domainBase.getAction(HouseBase.ACTION_NORTH);
		ActionType aSouth = domainBase.getAction(HouseBase.ACTION_SOUTH);
		ActionType aEast = domainBase.getAction(HouseBase.ACTION_EAST);
		ActionType aWest = domainBase.getAction(HouseBase.ACTION_WEST);
		ActionType aBuild = domainBase.getAction(HouseBase.ACTION_BUILD);
		
		// makeBlock actions
		ActionType aNavigate = domainBase.getAction(MakeBlock.ACTION_NAVIGATE);
		ActionType aPutBlock = domainBase.getAction(MakeBlock.ACTION_PUT_BLOCK);
		
		// makeWall actions
		ActionType aMakeBlock = domainWall.getAction(MakeWall.ACTION_MAKE_BLOCK);
		
		// makeRoom actions
		ActionType aMakeWall = domainRoom.getAction(MakeRoom.ACTION_MAKE_WALL);

		// base tasks
		TaskNode northTask = new TaskLeaf(aNorth);
		TaskNode southTask = new TaskLeaf(aSouth);
		TaskNode eastTask = new TaskLeaf(aEast);
		TaskNode westTask = new TaskLeaf(aWest);
		TaskNode buildTask = new TaskLeaf(aBuild);
		
		TaskNode[] navigateSubtasks = new TaskNode[]{northTask, eastTask, southTask, westTask);
		TaskNode[] putBlockSubtasks = new TaskNode[]{buildTask};
		
		TaskNode navigateTaskNode = new NavigateTaskNode(
				"navigateAMDP",
				new ActionType[]{aNavigate},
				navigateSubtasks,
				genNavigate.generateDomain(),
				(NavigateTF) tfNavigate,
				(NavigateRF) rfNavigate
		);
		
		TaskNode putBlockTaskNode = new PutBlockTaskNode(
				"putBlockAMDP",
				new ActionType[]{aPutBlock},
				putBlockSubtasks,
				genPutBlock.generateDomain();
				(PutBlockTF) tfPutBlock,
				(PutBlockRF) rfPutBlock
		);
		
		TaskNode[] makeBlockSubtasks = new TaskNode[]{navigateTaskNode, putBlockTaskNode};
		
		TaskNode makeBlock = new MakeBlockTaskNode(
				"makeBlockAMDP",
				new ActionType[]{makeBlock},
				genBlock.generateDomain(),
				makeBlockSubtasks,
				(MakeBlockTF) tfWall,
				(MakeBlockRF) rfWall
		);
				
		
		TaskNode[] makeWallSubtasks = new TaskNode[]{makeBlockSubtasks};
		
		
		TaskNode makeWallTask = new MakeWallTaskNode(
				"makeWallAMDP",
				new ActionType[]{aMakeWall},
				genWall.generateDomain(),
				makeWallSubtasks,
				(MakeWallTF) tfWall,
				(MakeWallRF) rfWall
		);
		
		TaskNode[] makeRoomSubtasks = new TaskNode[]{makeWallTask};
		
		TaskNode makeRoomTask = new AbstractTaskNode(
				"makeRoomAMDP",
				makeRoomSubtasks,
				genRoom.generateDomain(),
				(MakeRoomTF) tfRoom,
				(MakeRoomRF) rfRoom
		);
		
		GroundedTask rootGT = makeRoomTask.getApplicableGroundedTasks(initialStateRoom).get(0);
		System.out.println("rootGT: " + rootGT.getAction());
		AMDPAgent agent = new AMDPAgent(rootGT, pgList);

        SimulatedEnvironment envN = new SimulatedEnvironment(domainEnv, initial);
        int maxTrajectoryLength = 101;
        Episode e = agent.actUntilTermination(envN, maxTrajectoryLength);
        int count = 0;
        for(int i=0;i<brtdpList.size();i++){
            int numUpdates = brtdpList.get(i).getNumberOfBellmanUpdates();
            count+= numUpdates;
        }
        Policy bottom = brtdpList.get(1).planFromState(initial);
        AMDPPolicyGenerator pg1 = pgList.get(1);
        State absInitial = pg1.generateAbstractState(initial);
        Policy top = brtdpList.get(0).planFromState(absInitial);
        System.out.println(PolicyUtils.rollout(bottom, initial, domainEnv.getModel()).actionSequence);
        System.out.println(PolicyUtils.rollout(top, absInitial, domainRoom.getModel()).actionSequence);
        System.out.println(e.discountedReturn(1.));
        System.out.println(count);
        System.out.println("Total planners used: " + brtdpList.size());
        System.out.println("House with AMDPs \nBackups by individual planners:");
        for(BoundedRTDPForTests b:brtdpList){
            System.out.println(b.getNumberOfBellmanUpdates());
        }
        return makeRoomTask;
	}

	public static void main(String[] args) {
		AMDPAssembler.assembleAMDP();
    }
	
}
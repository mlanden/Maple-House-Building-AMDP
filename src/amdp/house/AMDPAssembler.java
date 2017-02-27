package amdp.house;

import java.util.ArrayList;
import java.util.List;

import amdp.amdpframework.AMDPAgent;
import amdp.amdpframework.AMDPPolicyGenerator;
import amdp.amdpframework.GroundedTask;
import amdp.amdpframework.TaskNode;
import amdp.house.level1.MakeWall;
import amdp.house.level1.MakeWallRF;
import amdp.house.level1.MakeWallTF;
import amdp.house.level1.TaskLeaf;
import amdp.house.level2.MakeRoom;
import amdp.house.level2.MakeRoomRF;
import amdp.house.level2.MakeRoomTF;
import amdp.house.objects.HPoint;
import amdp.house.objects.HWall;
import amdp.taxiamdpdomains.testingtools.BoundedRTDPForTests;
import amdp.taxiamdpdomains.testingtools.MutableGlobalInteger;
import burlap.behavior.policy.Policy;
import burlap.behavior.policy.PolicyUtils;
import burlap.behavior.singleagent.Episode;
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
		
		// goal is to build this wall
		HPoint start = new HPoint("wallStart", 0, 0, false);
		HPoint end = new HPoint("wallEnd", 3, 2, false);
		HWall goal = new HWall("goalWall", start, end, false);
		
		int width = 5;
		int height = 5;
		HashableStateFactory hashingFactory = new SimpleHashableStateFactory(true);
		TerminalFunction tfWall = new MakeWallTF();
		double rewardGoal = 1000;
		double rewardDefault = -.1;
		double rewardFailure = rewardDefault * 2;
		RewardFunction rfWall = new MakeWallRF((MakeWallTF) tfWall, rewardGoal, rewardDefault, rewardFailure);
		MakeWall genWall = new MakeWall(rfWall, tfWall, width, height);
		OOSADomain domainWall = genWall.generateDomain();
		OOSADomain domainEnv = genWall.generateDomain();
		OOState initial = genWall.getInitialState(goal);

		TerminalFunction tfRoom = new MakeRoomTF(goal);
		RewardFunction rfRoom = new MakeRoomRF((MakeRoomTF) tfRoom, 1000.0, 0.0, 0.0);
		MakeRoom genRoom = new MakeRoom(rfRoom, tfRoom, width, height);
		OOSADomain domainRoom = genRoom.generateDomain();
		OOState initialStateRoom = genRoom.getInitialState();
		
		List<AMDPPolicyGenerator> pgList = new ArrayList<AMDPPolicyGenerator>();
		pgList.add(0, new MakeWallPolicyGenerator(domainWall));
		pgList.add(1, new MakeRoomPolicyGenerator(domainRoom));

		// makeWall actions
		ActionType aNorth = domainWall.getAction(MakeWall.ACTION_NORTH);
		ActionType aSouth = domainWall.getAction(MakeWall.ACTION_SOUTH);
		ActionType aEast = domainWall.getAction(MakeWall.ACTION_EAST);
		ActionType aWest = domainWall.getAction(MakeWall.ACTION_WEST);
		ActionType aBuild = domainWall.getAction(MakeWall.ACTION_BUILD);
		
		// makeRoom actions
		ActionType makeWall = domainRoom.getAction(MakeRoom.ACTION_MAKE_WALL);
		
		TaskNode northTask = new TaskLeaf(aNorth);
		TaskNode southTask = new TaskLeaf(aSouth);
		TaskNode eastTask = new TaskLeaf(aEast);
		TaskNode westTask = new TaskLeaf(aWest);
		TaskNode buildTask = new TaskLeaf(aBuild);
		
		TaskNode[] makeWallSubtasks = new TaskNode[]{northTask, eastTask, southTask, westTask, buildTask};
		
		TaskNode makeWallTask = new MakeWallTaskNode(
				"makeWallAMDP",
				new ActionType[]{makeWall},
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
				tfRoom,
				rfRoom
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
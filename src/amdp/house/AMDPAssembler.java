package amdp.house;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

import amdp.amdpframework.AMDPAgent;
import amdp.amdpframework.AMDPPolicyGenerator;
import amdp.amdpframework.GroundedTask;
import amdp.amdpframework.TaskNode;
import amdp.house.base.HouseBase;
import amdp.house.level1.MakeBlock;
import amdp.house.level1.MakeBlockRF;
import amdp.house.level1.MakeBlockTF;
import amdp.house.level2.MakeWall;
import amdp.house.level2.MakeWallRF;
import amdp.house.level2.MakeWallTF;
import amdp.house.level2.TaskLeaf;
import amdp.house.level3.MakeRoom;
import amdp.house.level3.MakeRoomRF;
import amdp.house.level3.MakeRoomState;
import amdp.house.level3.MakeRoomStateMapping;
import amdp.house.level3.MakeRoomTF;
import amdp.house.objects.HPoint;
import amdp.house.objects.HRoom;
import amdp.taxiamdpdomains.testingtools.BoundedRTDPForTests;
import amdp.taxiamdpdomains.testingtools.MutableGlobalInteger;
import burlap.behavior.policy.Policy;
import burlap.behavior.policy.PolicyUtils;
import burlap.behavior.singleagent.Episode;
import burlap.behavior.singleagent.auxiliary.EpisodeSequenceVisualizer;
import burlap.debugtools.RandomFactory;
import burlap.mdp.auxiliary.common.NullTermination;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.oo.state.OOState;
import burlap.mdp.singleagent.common.UniformCostRF;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.oo.OOSADomain;
import burlap.statehashing.HashableStateFactory;
import burlap.statehashing.simple.SimpleHashableStateFactory;
import burlap.visualizer.Visualizer;

public class AMDPAssembler { 

	// bad -- fix this location
	public static List<BoundedRTDPForTests> brtdpList = new ArrayList<BoundedRTDPForTests>();
	public static MutableGlobalInteger bellmanBudget = new MutableGlobalInteger(-1);
	public static MutableGlobalInteger bellmanBudgetL0 = new MutableGlobalInteger(-1);
	public static MutableGlobalInteger bellmanBudgetL1 = new MutableGlobalInteger(-1);
	public static MutableGlobalInteger bellmanBudgetL2 = new MutableGlobalInteger(-1);
	public static MutableGlobalInteger bellmanBudgetL3 = new MutableGlobalInteger(-1);

	public static double BRTDP_MAX_DIFF = 0.0001;
	
	public static TaskNode assembleAMDP(){
		
//		Random seedGen = new Random();
		Long seed = 1129712866128848395L;//seedGen.nextLong();
		System.out.println(seed);
		RandomFactory.seedMapped(0, seed);
		
		HashableStateFactory hashingFactory = new SimpleHashableStateFactory(true);
		
		// goal is to build this room
		List<HPoint> corners = new ArrayList<HPoint>();
		HPoint p0 = new HPoint("p0", 0, 0, false); corners.add(p0); 
		HPoint p1 = new HPoint("p1", 3, 0, false); corners.add(p1); 
		HPoint p2 = new HPoint("p2", 3, 3, false); corners.add(p2); 
		HPoint p3 = new HPoint("p3", 0, 3, false); corners.add(p3); 
		HRoom goalRoom = new HRoom("goalRoom", corners, false);
		
		
		// make the base MDP domain
		int width = 5;
		int height = 5;
		double rewardGoal = 1.0;
		double goalDefaultRatio = 1000.0;
		double rewardDefault = -rewardGoal / goalDefaultRatio;
		double rewardFailure = rewardDefault * 2;
		TerminalFunction tfBase = new NullTermination();
		RewardFunction rfBase = new UniformCostRF();
		HouseBase genBase = new HouseBase(rfBase, tfBase, width, height);
		HouseBase genNavigate = genBase;
		HouseBase genPutBlock = genBase;
		OOSADomain domainBase = genBase.generateDomain();
		OOSADomain domainEnv = genBase.generateDomain();
		OOState initial = genBase.getInitialState(goalRoom);

		// make block AMDP
		TerminalFunction tfBlock = new MakeBlockTF(null);
		RewardFunction rfBlock = new MakeBlockRF((MakeBlockTF) tfBlock, rewardGoal, rewardDefault, rewardFailure);
		MakeBlock genBlock = new MakeBlock(rfBlock, tfBlock, width, height);
		OOSADomain domainBlock = genBlock.generateDomain();
		
		// make wall AMDP
		TerminalFunction tfWall = new MakeWallTF(null);
		RewardFunction rfWall = new MakeWallRF((MakeWallTF) tfWall, rewardGoal, rewardDefault, rewardFailure);
		MakeWall genWall = new MakeWall(rfWall, tfWall, width, height);
		OOSADomain domainWall = genWall.generateDomain();

		// make room AMDP
		TerminalFunction tfRoom = new MakeRoomTF(goalRoom);
		RewardFunction rfRoom = new MakeRoomRF((MakeRoomTF) tfRoom, rewardGoal, rewardDefault, rewardFailure);
		MakeRoom genRoom = new MakeRoom(rfRoom, tfRoom, width, height);
		OOSADomain domainRoom = genRoom.generateDomain();
		OOState initialStateRoom = (OOState) new MakeRoomStateMapping().mapState(initial);
		
		List<AMDPPolicyGenerator> pgList = new ArrayList<AMDPPolicyGenerator>();
		pgList.add(0, new PolicyGeneratorHouseBase(domainBase));
		pgList.add(1, new PolicyGeneratorMakeBlock(domainBlock));
		pgList.add(2, new PolicyGeneratorMakeWall(domainWall));
		pgList.add(3, new PolicyGeneratorMakeRoom(domainRoom));

		// base actions
		ActionType aNorth = domainBase.getAction(HouseBase.ACTION_NORTH);
		ActionType aSouth = domainBase.getAction(HouseBase.ACTION_SOUTH);
		ActionType aEast = domainBase.getAction(HouseBase.ACTION_EAST);
		ActionType aWest = domainBase.getAction(HouseBase.ACTION_WEST);
		ActionType aBuild = domainBase.getAction(HouseBase.ACTION_BUILD);
		
		// makeBlock actions
		ActionType aNavigate = domainBlock.getAction(MakeBlock.ACTION_NAVIGATE);
		ActionType aPutBlock = domainBlock.getAction(MakeBlock.ACTION_PUT_BLOCK);
		
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
		
		TaskNode[] navigateSubtasks = new TaskNode[]{northTask, eastTask, southTask, westTask};
		TaskNode[] putBlockSubtasks = new TaskNode[]{buildTask};
		
		
		TaskNode navigateTask = new TaskNavigate(
				"navigateAMDP",
				new ActionType[]{aNavigate},
				navigateSubtasks,
				genNavigate.generateDomain()//,
//				tfNavigate,
//				rfNavigate
		);
		
		TaskNode putBlockTask = new TaskPutBlock(
				"putBlockAMDP",
				new ActionType[]{aPutBlock},
				putBlockSubtasks,
				genPutBlock.generateDomain()//,
//				tfPutBlock,
//				rfPutBlock
		);
		
		TaskNode[] makeBlockSubtasks = new TaskNode[]{navigateTask, putBlockTask};
		
		TaskNode makeBlockTask = new TaskMakeBlock(
				"makeBlockAMDP",
				new ActionType[]{aMakeBlock},
				genBlock.generateDomain(),
				makeBlockSubtasks,
				(MakeBlockTF) tfBlock,
				(MakeBlockRF) rfBlock
		);
				
		
		TaskNode[] makeWallSubtasks = new TaskNode[]{makeBlockTask};
		
		
		TaskNode makeWallTask = new TaskMakeWall(
				"makeWallAMDP",
				new ActionType[]{aMakeWall},
				genWall.generateDomain(),
				makeWallSubtasks,
				(MakeWallTF) tfWall,
				(MakeWallRF) rfWall
		);
		
		TaskNode[] makeRoomSubtasks = new TaskNode[]{makeWallTask};
		
		TaskNode makeRoomTask = new TaskUnparameterizedSubtasks(
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
        List<Episode> episodes = new ArrayList<Episode>();
        episodes.add(e);

        System.out.println("done");
//        System.out.println(e.stateSequence);
        System.out.println(e.actionSequence);
        
        Visualizer v = HouseBaseVisualizer.getVisualizer(width, height);
        EpisodeSequenceVisualizer esv = new EpisodeSequenceVisualizer(v, domainBase, episodes);
        esv.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        MakeRoomState absInitial = (MakeRoomState) new MakeRoomStateMapping().mapState(initial);
        Policy top = brtdpList.get(0).planFromState(absInitial);
        System.out.println(PolicyUtils.rollout(top, absInitial, domainRoom.getModel()).actionSequence);

        
//        int count = 0;
//        for(int i=0;i<brtdpList.size();i++){
//            int numUpdates = brtdpList.get(i).getNumberOfBellmanUpdates();
//            count+= numUpdates;
//        }
//        Policy bottom = brtdpList.get(1).planFromState(initial);
//        AMDPPolicyGenerator pg1 = pgList.get(1);
//        State absInitial = pg1.generateAbstractState(initial);
//        Policy top = brtdpList.get(0).planFromState(absInitial);
//        System.out.println(PolicyUtils.rollout(bottom, initial, domainEnv.getModel()).actionSequence);
//        System.out.println(PolicyUtils.rollout(top, absInitial, domainRoom.getModel()).actionSequence);
//        System.out.println(e.discountedReturn(1.));
//        System.out.println(count);
//        System.out.println("Total planners used: " + brtdpList.size());
//        System.out.println("House with AMDPs \nBackups by individual planners:");
//        for(BoundedRTDPForTests b:brtdpList){
//            System.out.println(b.getNumberOfBellmanUpdates());
//        }
        return makeRoomTask;
        /**/
	}

	public static void main(String[] args) {
		AMDPAssembler.assembleAMDP();
    }
	
}
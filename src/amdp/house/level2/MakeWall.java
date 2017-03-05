package amdp.house.level2;

import java.util.Random;

import amdp.amdpframework.GroundedPropSC;
import amdp.house.base.HouseBase;
import amdp.house.base.PointParameterizedActionType;
import amdp.house.objects.HAgent;
import amdp.house.objects.HBlock;
import amdp.house.objects.HPoint;
import amdp.house.objects.HWall;
import burlap.behavior.policy.Policy;
import burlap.behavior.policy.PolicyUtils;
import burlap.behavior.singleagent.Episode;
import burlap.behavior.singleagent.planning.stochastic.rtdp.BoundedRTDP;
import burlap.behavior.valuefunction.ConstantValueFunction;
import burlap.debugtools.RandomFactory;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.oo.OODomain;
import burlap.mdp.core.oo.propositional.GroundedProp;
import burlap.mdp.core.oo.state.OOState;
import burlap.mdp.core.oo.state.OOStateUtilities;
import burlap.mdp.singleagent.model.FactoredModel;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.oo.OOSADomain;
import burlap.statehashing.HashableStateFactory;
import burlap.statehashing.simple.SimpleHashableStateFactory;

public class MakeWall extends HouseBase {
	
	//actions
	public static final String ACTION_MAKE_BLOCK = "makeBlock";
	public static final int NUM_ACTIONS = 1;

    public MakeWall(RewardFunction rf, TerminalFunction tf, int width, int height) {
    	super(rf, tf, width, height);
    }
    
    @Override
	public OOSADomain generateDomain() {
		OOSADomain domain = new OOSADomain();
		
		domain.addStateClass(HAgent.CLASS_AGENT, HAgent.class);
		domain.addStateClass(HPoint.CLASS_POINT, HPoint.class);
		domain.addStateClass(HBlock.CLASS_BLOCK, HBlock.class);
		domain.addStateClass(HWall.CLASS_WALL, HWall.class);
		
		MakeWallModel model = new MakeWallModel();
		FactoredModel fmodel = new FactoredModel(model, rf, tf);
		domain.setModel(fmodel);
		
		//actions
		domain.addActionTypes(new PointParameterizedActionType(ACTION_MAKE_BLOCK));
		
		OODomain.Helper.addPfsToDomain(domain, generatePfs(domain));
		
		return domain;
	}
	
	public MakeWallState getInitialMakeWallState() {
		MakeWallState state = new MakeWallState(width, height, 0, 0);
		return state;
	}
	
	public static void main(String[] args) {
		
		Random random = new Random();
		long seed = random.nextLong();
		System.out.println(seed);
		RandomFactory.seedMapped(0, seed);

		int width = 5;
		int height = 5;
		int sX = 0; int sY = 0;
		int eX = 4; int eY = 4;
		final String startName = HPoint.CLASS_POINT+"_"+sX+"_"+sY;
		final String endName = HPoint.CLASS_POINT+"_"+eX+"_"+eY;
		HPoint wallStart = new HPoint(startName, sX, sY, false);
		HPoint wallEnd = new HPoint(endName, eX, eY, false);
//		HWall wall = new HWall("goalWall", wallStart, wallEnd, false);
		
		HashableStateFactory hashingFactory = new SimpleHashableStateFactory(true);
		GroundedProp gp = new GroundedProp(new HasGoalWallPF(), new String[]{startName, endName});
		GroundedPropSC test = new GroundedPropSC(gp);
		MakeWallTF tf = new MakeWallTF(test);
		double rewardGoal = 1.0;
		double goalDefaultRatio = 1000.0;
		double rewardDefault = -rewardGoal / goalDefaultRatio;
		double rewardFailure = rewardDefault * 2;
		RewardFunction rf = new MakeWallRF(tf, rewardGoal, rewardDefault, rewardFailure);
		MakeWall gen = new MakeWall(rf, tf, width, height);
		OOSADomain domain = gen.generateDomain();
		OOState initial = gen.getInitialMakeWallState();
		
//		System.out.println(OOStateUtilities.ooStateToString(initial));
		
		double lowerVInit = 0.;
//		double upperVInit = 1.;
		double maxDiff = rewardGoal / goalDefaultRatio;
		double gamma = 0.99;
		double distance = HPoint.distanceChebyshev(wallStart, wallEnd);
		distance += 1;
		int maxSteps = (int) distance + 1;
		int maxRollouts = -1;
		int maxRolloutDepth = maxSteps;
		BoundedRTDP brtdp =
				new BoundedRTDP(domain, gamma, hashingFactory, 
				new ConstantValueFunction(lowerVInit),
				new MakeWallHeuristic(gamma, tf),
//				new ConstantValueFunction(upperVInit),
				maxDiff, maxRollouts);
		brtdp.setMaxRolloutDepth(maxRolloutDepth);
		brtdp.toggleDebugPrinting(true);
		long startTime = System.currentTimeMillis();
		Policy P = brtdp.planFromState(initial);
		long endTime = System.currentTimeMillis();
		long duration = (endTime - startTime);
		System.out.println("total planning time: " + duration);
		startTime = System.currentTimeMillis();
		Episode ea = PolicyUtils.rollout(P, initial, domain.getModel(), maxSteps);
		endTime = System.currentTimeMillis();
		duration = (endTime - startTime);
		System.out.println("total rollout time: " + duration);
		System.out.println("total actions: " + ea.actionSequence.size());
		System.out.println("number Bellman: " + brtdp.getNumberOfBellmanUpdates());
		System.out.println(ea.actionSequence);

//		System.out.println(OOStateUtilities.ooStateToString((OOState) ea.stateSequence.get(ea.stateSequence.size()-3)));
//		System.out.println(OOStateUtilities.ooStateToString((OOState) ea.stateSequence.get(ea.stateSequence.size()-2)));
		System.out.println(OOStateUtilities.ooStateToString((OOState) ea.stateSequence.get(ea.stateSequence.size()-1)));
		OOState last = ((OOState) ea.stateSequence.get(ea.stateSequence.size()-1));
		System.out.println(last.objectsOfClass(HBlock.CLASS_BLOCK).size() + " blocks");
		System.out.println(ea.actionSequence);
		
		System.out.println(test.satisfies(last));
		
		System.out.println(seed);
	}

}

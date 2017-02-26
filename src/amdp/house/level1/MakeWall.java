package amdp.house.level1;

import java.util.ArrayList;
import java.util.List;

import amdp.house.objects.HAgent;
import amdp.house.objects.HBlock;
import amdp.house.objects.HPoint;
import amdp.house.objects.HWall;
import burlap.behavior.policy.Policy;
import burlap.behavior.policy.PolicyUtils;
import burlap.behavior.singleagent.Episode;
import burlap.behavior.singleagent.planning.stochastic.rtdp.BoundedRTDP;
import burlap.behavior.valuefunction.ConstantValueFunction;
import burlap.mdp.auxiliary.DomainGenerator;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.action.UniversalActionType;
import burlap.mdp.core.oo.OODomain;
import burlap.mdp.core.oo.propositional.PropositionalFunction;
import burlap.mdp.core.oo.state.OOState;
import burlap.mdp.core.oo.state.OOStateUtilities;
import burlap.mdp.singleagent.common.GoalBasedRF;
import burlap.mdp.singleagent.common.UniformCostRF;
import burlap.mdp.singleagent.model.FactoredModel;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.oo.OOSADomain;
import burlap.statehashing.HashableStateFactory;
import burlap.statehashing.simple.SimpleHashableStateFactory;

public class MakeWall implements DomainGenerator{
	
	//actions
	public static final String ACTION_BUILD = "build";
	public static final String ACTION_NORTH = "north";
	public static final String ACTION_EAST = "east";
	public static final String ACTION_SOUTH = "south";
	public static final String ACTION_WEST = "west";
	public static final int NUM_ACTIONS = 5;

	protected RewardFunction rf;
	protected TerminalFunction tf;
	
	private int width;
	private int height;
	    
    public MakeWall(RewardFunction rf, TerminalFunction tf, int width, int height) {
        this.rf = rf;
        this.tf = tf;
        this.width = width;
        this.height = height;
    }
    
    public RewardFunction getRF(){
    	return rf;
    }
    
    public TerminalFunction getTF(){
    	return tf;
    }
    
	public OOSADomain generateDomain() {
		OOSADomain domain = new OOSADomain();
		
		domain.addStateClass(HBlock.CLASS_BLOCK, HBlock.class);
		domain.addStateClass(HAgent.CLASS_AGENT, HAgent.class);
		
		MakeWallModel model = new MakeWallModel();
		FactoredModel fmodel = new FactoredModel(model, rf, tf);
		domain.setModel(fmodel);
		
		//actions
		domain.addActionTypes(
				new UniversalActionType(ACTION_NORTH),
				new UniversalActionType(ACTION_EAST),
				new UniversalActionType(ACTION_SOUTH),
				new UniversalActionType(ACTION_WEST),
				new UniversalActionType(ACTION_BUILD));
		
		OODomain.Helper.addPfsToDomain(domain, generatePfs(domain));
		
		return domain;
	}
	
	private List<PropositionalFunction> generatePfs(OOSADomain domain) {
		List<PropositionalFunction> pfs = new ArrayList<PropositionalFunction>();
		return pfs;
	}
	
	public MakeWallState getInitialState() {
		MakeWallState state = new MakeWallState(width, height, 0, 0);
		return state;
	}

	public static void main(String[] args) {
		
//		HPoint wallStart = new HPoint("point_0_0", 0, 0, false);
//		HPoint wallEnd = new HPoint("point_4_4", 4, 4, false);
//		HWall wall = new HWall("goalWall", wallStart, wallEnd);
		
		HashableStateFactory hashingFactory = new SimpleHashableStateFactory(true);
		HasWall goal = new HasWall(0,0,0,1);
		MakeWallTF tf = new MakeWallTF(goal);
		double rewardGoal = 1.0;
		double rewardDefault = -.0001;
		double rewardFailure = rewardDefault * 2;
		RewardFunction rf = new MakeWallRF(tf, rewardGoal, rewardDefault, rewardFailure);
		int width = 5;
		int height = 5;
		MakeWall gen = new MakeWall(rf, tf, width, height);
		OOSADomain domain = gen.generateDomain();
		OOState initial = gen.getInitialState();

//		System.out.println(OOStateUtilities.ooStateToString(initial));
		
		double lowerVInit = 0.;
		double upperVInit = 1.;
		double maxDiff = rewardGoal / 10000.0;
		double gamma = 0.99;
		int maxSteps = 30;
		int maxRollouts = 65536;
		int maxRolloutDepth = maxSteps;
		BoundedRTDP brtdp =
				new BoundedRTDP(domain, gamma, hashingFactory, 
				new ConstantValueFunction(lowerVInit),
				new ConstantValueFunction(upperVInit), maxDiff, maxRollouts);
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
		
	}

}

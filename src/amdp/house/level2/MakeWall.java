package amdp.house.level2;

import java.util.ArrayList;
import java.util.List;

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
import burlap.mdp.auxiliary.DomainGenerator;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.oo.OODomain;
import burlap.mdp.core.oo.propositional.PropositionalFunction;
import burlap.mdp.core.oo.state.OOState;
import burlap.mdp.core.oo.state.OOStateUtilities;
import burlap.mdp.singleagent.model.FactoredModel;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.oo.OOSADomain;
import burlap.statehashing.HashableStateFactory;
import burlap.statehashing.simple.SimpleHashableStateFactory;

public class MakeWall implements DomainGenerator{
	
	//actions
	public static final String ACTION_MAKE_BLOCK = "makeBlock";
	public static final int NUM_ACTIONS = 1;

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
	
	private List<PropositionalFunction> generatePfs(OOSADomain domain) {
		List<PropositionalFunction> pfs = new ArrayList<PropositionalFunction>();
		return pfs;
	}
	
	public MakeWallState getInitialState(HWall goalWall) {
		MakeWallState state = new MakeWallState(width, height, 0, 0, goalWall);
		return state;
	}
	
	public static void main(String[] args) {
		
		HPoint wallStart = new HPoint("pointStart", 0, 0, false);
		HPoint wallEnd = new HPoint("pointEnd", 1, 1, false);
		HWall wall = new HWall("goalWall", wallStart, wallEnd, false);
		
		HashableStateFactory hashingFactory = new SimpleHashableStateFactory(true);
		MakeWallTF tf = new MakeWallTF();
		double goalDefaultRatio = 1000.0;
		double rewardGoal = 1.0;
		double rewardDefault = -rewardGoal / goalDefaultRatio;
		double rewardFailure = rewardDefault * 2;
		RewardFunction rf = new MakeWallRF(tf, rewardGoal, rewardDefault, rewardFailure);
		int width = 5;
		int height = 5;
		MakeWall gen = new MakeWall(rf, tf, width, height);
		OOSADomain domain = gen.generateDomain();
		OOState initial = gen.getInitialState(wall);

//		System.out.println(OOStateUtilities.ooStateToString(initial));
		
		double lowerVInit = 0.;
		double upperVInit = 1.;
		double maxDiff = rewardGoal / goalDefaultRatio;
		double gamma = 0.99;
		int maxSteps = width+height;
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

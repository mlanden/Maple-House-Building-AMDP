package amdp.house.level2;

import amdp.house.objects.HPoint;
import amdp.house.objects.HWall;
import burlap.behavior.policy.Policy;
import burlap.behavior.policy.PolicyUtils;
import burlap.behavior.singleagent.Episode;
import burlap.behavior.singleagent.planning.stochastic.rtdp.BoundedRTDP;
import burlap.behavior.valuefunction.ConstantValueFunction;
import burlap.mdp.auxiliary.DomainGenerator;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.oo.ObjectParameterizedAction;
import burlap.mdp.core.oo.state.OOState;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.common.UniformCostRF;
import burlap.mdp.singleagent.model.FactoredModel;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.oo.OOSADomain;
import burlap.mdp.singleagent.oo.ObjectParameterizedActionType;
import burlap.statehashing.HashableStateFactory;
import burlap.statehashing.simple.SimpleHashableStateFactory;

public class MakeRoom implements DomainGenerator {

	public static final String ACTION_MAKE_WALL = "makeWall";
	public static final int NUM_ACTIONS = 1;

	protected RewardFunction rf;
	protected TerminalFunction tf;
	
	private int width;
	private int height;
	
	public MakeRoom(RewardFunction rf, TerminalFunction tf, int width, int height) {
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
	
	@Override
	public OOSADomain generateDomain() {
		OOSADomain domain = new OOSADomain();
		        
//        domain.addStateClass(CLASS_AGENT_L1, MakeWallAgent.class).addStateClass(CLASS_WALL, Wall.class);
        domain.addStateClass(HPoint.CLASS_POINT, HPoint.class);
        domain.addStateClass(HWall.CLASS_WALL, HWall.class);
		
        MakeRoomModel model = new MakeRoomModel();
		FactoredModel fmodel = new FactoredModel(model, rf, tf);
		domain.setModel(fmodel);
		
		//actions
		domain.addActionType(new MakeWallActionType(ACTION_MAKE_WALL));
		
		return domain;
	}
	

	public class MakeWallActionType extends ObjectParameterizedActionType {

		public MakeWallActionType(String name){
			super(name, new String[]{HPoint.CLASS_POINT, HPoint.CLASS_POINT});
		}
		
		public boolean applicableInState(State state, ObjectParameterizedAction groundedAction){
			MakeRoomState s = (MakeRoomState) state;
			String [] params = groundedAction.getObjectParameters();
			HPoint pointA = (HPoint)s.object(params[0]);
			HPoint pointB = (HPoint)s.object(params[1]);
			if (pointA == null || pointB == null) {
				return false;
			}
			
//			int aX = (Integer) pointA.get(HPoint.ATT_X);
//			int aY = (Integer) pointA.get(HPoint.ATT_Y);
//			int bX = (Integer) pointB.get(HPoint.ATT_X);
//			int bY = (Integer) pointB.get(HPoint.ATT_Y);
			
			return true;
		}
	}
	
	public MakeRoomState getInitialState() {
		MakeRoomState state = new MakeRoomState(width, height);
		return state;
	}
	
	public static void main(String[] args) {
		
		// goal is to build this wall
		HPoint start = new HPoint("wallStart", 0, 0, false);
		HPoint end = new HPoint("wallEnd", 4, 2, false);
		HWall goal = new HWall("goalWall", start, end, false);
		
		HashableStateFactory hashingFactory = new SimpleHashableStateFactory();
		MakeRoomTF tf = new MakeRoomTF(goal);
		MakeRoomRF rf = new MakeRoomRF(tf, 1000.0, 0.0, 0.0);
		int width = 5;
		int height = 5;
		MakeRoom gen = new MakeRoom(rf, tf, width, height);
		OOSADomain domain = gen.generateDomain();
		OOState initial = gen.getInitialState();

//		System.out.println(OOStateUtilities.ooStateToString(initial));
		
		double lowerVInit = 0.;
		double upperVInit = 1.;
		double maxDiff = 0.1;
		double gamma = 0.95;
		int maxSteps = 1001;
		int maxRollouts = 16384;//4096;
		int maxRolloutDepth = 128;
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
		
	}

}

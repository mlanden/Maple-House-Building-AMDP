package amdp.house.level3;

import java.util.ArrayList;
import java.util.List;

import amdp.house.base.PointParameterizedActionType;
import amdp.house.objects.HPoint;
import amdp.house.objects.HRoom;
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
			super(name, new String[]{HPoint.CLASS_POINT, HPoint.CLASS_POINT}, new String[]{HPoint.CLASS_POINT, HPoint.CLASS_POINT});
		}
		
		public boolean applicableInState(State state, ObjectParameterizedAction groundedAction){
			MakeRoomState s = (MakeRoomState) state;
			String [] params = groundedAction.getObjectParameters();
			HPoint pointA = (HPoint)s.object(params[0]);
			HPoint pointB = (HPoint)s.object(params[1]);
			if (pointA == null || pointB == null) {
				return false;
			}
			return true;
		}
	}
	
	public MakeRoomState getInitialState(HRoom goal) {
		MakeRoomState state = new MakeRoomState(width, height, goal);
		return state;
	}
	
	public static void main(String[] args) {
		
		int width = 5;
		int height = 5;

		// goal is to build this room
		List<HPoint> corners = new ArrayList<HPoint>();
		HPoint p0 = new HPoint("p0", 0, 0, false); corners.add(p0);
		HPoint p1 = new HPoint("p1", 0, 4, false); corners.add(p1);
		HPoint p2 = new HPoint("p2", 4, 4, false); corners.add(p2);
		HPoint p3 = new HPoint("p3", 4, 0, false); corners.add(p3);
//		HPoint p1 = new HPoint("p1", 0, 1, false); corners.add(p1);
//		HPoint p2 = new HPoint("p2", 0, 2, false); corners.add(p2);
//		HPoint p3 = new HPoint("p3", 1, 2, false); corners.add(p3);
//		HPoint p4 = new HPoint("p4", 2, 2, false); corners.add(p4);
//		HPoint p5 = new HPoint("p5", 2, 1, false); corners.add(p5);
//		HPoint p6 = new HPoint("p6", 2, 0, false); corners.add(p6);
//		HPoint p7 = new HPoint("p7", 1, 0, false); corners.add(p7);
		HRoom goal = new HRoom("goalRoom", corners, false);
		
		HashableStateFactory hashingFactory = new SimpleHashableStateFactory();
		MakeRoomTF tf = new MakeRoomTF(goal);
		double rewardGoal = 1.0;
		double rewardDefault = -rewardGoal / 1000.0;
		double rewardFailure = rewardDefault * 2;
		MakeRoomRF rf = new MakeRoomRF(tf, rewardGoal, rewardDefault, rewardFailure);
		MakeRoom gen = new MakeRoom(rf, tf, width, height);
		OOSADomain domain = gen.generateDomain();
		OOState initial = gen.getInitialState(goal);
		
		
//		MakeRoomState extra = (MakeRoomState)initial;
//		extra.addObject(new HWall("tempdebug",new HPoint("a",0,0,false), new HPoint("b",0,3,false), true));
//		MakeWallActionType action = (MakeWallActionType) domain.getAction(MakeRoom.ACTION_MAKE_WALL);
//		System.out.println(action.allApplicableActions(initial).size());
//		System.exit(-1);

//		System.out.println(OOStateUtilities.ooStateToString(initial));
		
		double lowerVInit = 0.;
//		double upperVInit = 1.;
		double maxDiff = rewardGoal / 1000.0;
		double gamma = 0.99;
		int maxSteps = corners.size() + 2;
		int maxRollouts = -1;//65536;//4096;
		int maxRolloutDepth = maxSteps;
		BoundedRTDP brtdp =
				new BoundedRTDP(domain, gamma, hashingFactory, 
				new ConstantValueFunction(lowerVInit),
				//new ConstantValueFunction(upperVInit),
				new MakeRoomHeuristic(gamma),
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
		
	}

}

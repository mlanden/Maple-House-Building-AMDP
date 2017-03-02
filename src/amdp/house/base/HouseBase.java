package amdp.house.base;

import java.util.ArrayList;
import java.util.List;

import amdp.house.objects.HAgent;
import amdp.house.objects.HBlock;
import amdp.house.objects.HPoint;
import amdp.house.objects.HRoom;
import amdp.house.objects.HWall;
import burlap.mdp.auxiliary.DomainGenerator;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.action.UniversalActionType;
import burlap.mdp.core.oo.OODomain;
import burlap.mdp.core.oo.propositional.PropositionalFunction;
import burlap.mdp.singleagent.model.FactoredModel;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.oo.OOSADomain;

public class HouseBase implements DomainGenerator {
	
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
	    
    public HouseBase(RewardFunction rf, TerminalFunction tf, int width, int height) {
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
		domain.addStateClass(HRoom.CLASS_ROOM, HRoom.class);
		
		HouseBaseModel model = new HouseBaseModel();
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
	
	public HouseBaseState getInitialState(HRoom goal) {
		HouseBaseState state = new HouseBaseState(width, height, new HAgent(0, 0), goal);
		return state;
	}
	
}

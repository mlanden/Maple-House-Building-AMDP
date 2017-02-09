package amdp.houseBuilding.level1.domain;

import java.util.ArrayList;
import java.util.List;

import amdp.houseBuilding.level1.state.L1Agent;
import burlap.mdp.auxiliary.DomainGenerator;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.action.UniversalActionType;
import burlap.mdp.core.oo.OODomain;
import burlap.mdp.core.oo.propositional.PropositionalFunction;
import burlap.mdp.singleagent.model.FactoredModel;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.oo.OOSADomain;
import compositeObjectDomain.Wall;

public class L1DomainGenerator implements DomainGenerator{

	//object classes
	public static final String CLASS_AGENT = 	"L0_Agent";
	public static final String CLASS_WALL = 	"wall";
	
	//wall properties
	public static final String DOORS = 			"doors";
	public static final String START_X = 		"start x";
	public static final String START_Y =		"start y";
	public static final String END_X = 			"end x";
	public static final String END_Y = 			"end y";
	public static final String LENGTH = 		"length";
	
	public static final String MAP = 			"map";
	//actions
	public static final String ACTION_PUTBLOCK = "put block";
	public static final String ACTION_NORTH = 	"north";
	public static final String ACTION_EAST = 	"east";
	public static final String ACTION_SOUTH = 	"south";
	public static final String ACTION_WEST = 	"west";

	protected int goalLength;
	protected RewardFunction rf;
	protected TerminalFunction tf;
	protected boolean stocastic;
	    
    public L1DomainGenerator(int goalLength, boolean stoc){
    	this.goalLength = goalLength;
    	stocastic = stoc;
    }
    
    public RewardFunction getRF(){
    	return rf;
    }
    
    public TerminalFunction getTF(){
    	return tf;
    }
	public OOSADomain generateDomain() {
		OOSADomain domain = new OOSADomain();
		
		//may want to pass into constructer
		if(rf == null){
			rf = new L1WallRewardFunction(goalLength);
		}
		
		if(tf == null){
			tf = new L1WallTerminalFunction(goalLength);
		}
		
		domain.addStateClass(CLASS_AGENT, L1Agent.class).addStateClass(CLASS_WALL, Wall.class);
		
		L1Model model = new L1Model(stocastic);
		FactoredModel fmodel = new FactoredModel(model, rf, tf);
		domain.setModel(fmodel);
		
		//actions
		domain.addActionTypes(
				new UniversalActionType(ACTION_NORTH),
				new UniversalActionType(ACTION_EAST),
				new UniversalActionType(ACTION_SOUTH),
				new UniversalActionType(ACTION_WEST),
				new UniversalActionType(ACTION_PUTBLOCK));
		
		OODomain.Helper.addPfsToDomain(domain, generatePfs(domain));
		
		return domain;
	}
	
	private List<PropositionalFunction> generatePfs(OOSADomain domain) {
		List<PropositionalFunction> pfs = new ArrayList<PropositionalFunction>();
		
		return pfs;
	}
}

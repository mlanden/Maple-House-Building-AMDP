package amdp.houseBuilding.level2.domain;

import java.util.ArrayList;
import java.util.List;

import amdp.houseBuilding.level1.domain.L1DomainGenerator;
import amdp.houseBuilding.level1.domain.L1Model;
import amdp.houseBuilding.level1.state.L1Agent;
import amdp.houseBuilding.level1.state.L1State;
import burlap.mdp.auxiliary.DomainGenerator;
import burlap.mdp.auxiliary.common.NullTermination;
import burlap.mdp.core.Domain;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.action.ActionType;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.common.UniformCostRF;
import burlap.mdp.singleagent.model.FactoredModel;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.oo.OOSADomain;
import compositeObjectDomain.Wall;

public class L2DomainGenerator implements DomainGenerator{

	public static final String CLASS_AGENT = 	"L1_Agent";
	public static final String CLASS_WALL = 	"wall";
	
	public static final String ACTION_MAKEWALL = "Make wall";
	
	protected RewardFunction rf;
	protected TerminalFunction tf;
	
	public L2DomainGenerator(){
		
	}
	public RewardFunction getRF(){
    	return rf;
    }
    
    public TerminalFunction getTF(){
    	return tf;
    }
	public L2DomainGenerator(RewardFunction rf, TerminalFunction tf) {
        this.rf = rf;
        this.tf = tf;
	}
	
	public OOSADomain generateDomain() {
		OOSADomain domain = new OOSADomain();
		
		if(rf == null){
            rf = new UniformCostRF();
        }
        if(tf == null){
            tf = new NullTermination();
        }
        
        domain.addStateClass(CLASS_AGENT, L1Agent.class).addStateClass(CLASS_WALL, Wall.class);
		
        L2Model model = new L2Model();
		FactoredModel fmodel = new FactoredModel(model, rf, tf);
		domain.setModel(fmodel);
		
		//actions
		domain.addActionType(new MakeWallActionType());
		
		return domain;
	}

	
	public class MakeWallActionType implements ActionType{

		public List<Action> allApplicableActions(State s) {
			List<Action> actions = new ArrayList<Action>();
			L1State st = (L1State)s;
			int[][] map = (int[][]) st.get(L1DomainGenerator.MAP);
			int wid = map.length;
			int hei = map[0].length;
			for(int stX = 0; stX < wid; stX++){
				for(int stY = 0; stY < hei; stY++){
					for(int edX = 0; edX < wid; edX++){
						for(int edY = 0; edY < hei; edY++){
							actions.add(new MakeWallAction("wall(" + stX + ',' + stY + ',' + edX + ',' + edY));
						}
					}
				}
			}
			return actions;
		}

		public Action associatedAction(String strRep) {
			return new MakeWallAction(strRep);
		}

		public String typeName() {
			return ACTION_MAKEWALL;
		}
		
		public class MakeWallAction implements Action{
			
			protected String wallName;
			public MakeWallAction(String wName){
				wallName = wName;
			}
			
			public String actionName() {
				return ACTION_MAKEWALL + "_" + wallName;
			}


			public Action copy() {
				return new MakeWallAction(wallName);
			}
			
			public String toString() {
                return this.actionName();
            }
		}
	}
}

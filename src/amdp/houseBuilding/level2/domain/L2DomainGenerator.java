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
					for(int edX = stX; edX < wid; edX++){
						for(int edY = stY; edY < hei; edY++){
							String name = "wall(" + stX + ',' + stY + ',' + edX + ',' + edY;
							actions.add(new MakeWallAction(name, stX, stY, edX, edY));
						}
					}
				}
			}
			return actions;
		}
		//FIX ME!!!!!!!!!!!!!!!!!
		public Action associatedAction(String strRep) {
			String[] nam = strRep.split("_");
			int sx = Integer.parseInt(nam[1]);
			int sy = Integer.parseInt(nam[2]);
			int ex = Integer.parseInt(nam[3]);
			int ey = Integer.parseInt(nam[4]);
			return new MakeWallAction(strRep, sx, sy, ex, ey);
		}

		public String typeName() {
			return ACTION_MAKEWALL;
		}
		
		public class MakeWallAction implements Action{
			
			protected String wallName;
			public int startX, startY, endX, endY, length;
			public MakeWallAction(String wName, int sx, int sy, int ex, int ey){
				wallName = wName;
				startX = sx;
				startY = sy;
				endX = ex;
				endY = ey;
				int dx = endX - startX, dy = endY - startY;
				if(dx == 0)
					length = dy;
				else 
					length = dx;
			}
			
			public String actionName() {
				return ACTION_MAKEWALL + "_" + startX + "_" + startY + "_" + endX + "_" + endY;
			}


			public Action copy() {
				return new MakeWallAction(wallName, startX, startY, endX, endY);
			}
			
			public String toString() {
                return this.actionName();
            }
		}
	}
}

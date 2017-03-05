package amdp.house.level3;

import java.util.List;

import amdp.house.base.HouseBaseState;
import amdp.house.level2.HasGoalWallPF;
import amdp.house.objects.HBlock;
import amdp.house.objects.HWall;
import burlap.mdp.auxiliary.StateMapping;
import burlap.mdp.core.state.State;

public class MakeRoomStateMapping implements StateMapping {
	
	@Override
	public State mapState(State s) {
		HouseBaseState state = (HouseBaseState) s;
		MakeRoomState out = new MakeRoomState(state);

		List<HBlock> blocks = state.getBlocksList();
		for (int i = 0; i < blocks.size(); i++) {
			HBlock start = blocks.get(i);
			int aX = (int) start.get(HBlock.ATT_X);
			int aY = (int) start.get(HBlock.ATT_Y);
			for (int j = i; j < blocks.size(); j++) {
				HBlock end = blocks.get(j);
				int bX = (int) end.get(HBlock.ATT_X);
				int bY = (int) end.get(HBlock.ATT_Y);
				boolean wallCouldExist = HasGoalWallPF.checkLineMinimal(state, aX, aY, bX, bY);
				if (wallCouldExist) {
					String wallName = "wall_"+aX+"_"+aY+"_"+bX+"_"+bY;
					if (state.object(wallName) == null) {
						HWall wall = new HWall(wallName,start,end,true);
						out.addObject(wall);
//						System.out.println(out);
//						System.out.println(wall);
//						System.out.println("--");
					}
				}
			}
		}
		
		return out;
	}

}

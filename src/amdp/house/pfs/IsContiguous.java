package amdp.house.pfs;

import burlap.mdp.core.oo.propositional.PropositionalFunction;
import burlap.mdp.core.oo.state.OOState;
import burlap.mdp.core.oo.state.ObjectInstance;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import amdp.house.level2.MakeWallState;
import amdp.house.objects.HBlock;
import amdp.house.objects.HPoint;
import amdp.house.objects.HWall;

/**
 * Created by khalil8500 on 2/6/2017.
 */
public class IsContiguous extends PropositionalFunction
{
	
	public static final String PF_IS_CONTIGUOUS = "isContiguous";

    public IsContiguous() {
        super(PF_IS_CONTIGUOUS, new String[]{HPoint.CLASS_POINT, HPoint.CLASS_POINT}, new String[]{HPoint.CLASS_POINT});
    }
    
    public boolean isTrue(OOState s, HPoint wallStart, HPoint wallEnd) {
    	MakeWallState state = (MakeWallState) s;

        if (wallStart.compareTo(wallEnd) > 0) {
        	HPoint temp = wallStart; wallStart = wallEnd; wallEnd = temp;
        }

		int aX = (Integer) wallStart.get(HPoint.ATT_X);
		int aY = (Integer) wallStart.get(HPoint.ATT_Y);
		int bX = (Integer) wallEnd.get(HPoint.ATT_X);
		int bY = (Integer) wallEnd.get(HPoint.ATT_Y);
		
		if (!state.blockAt(aX, aY) || !state.blockAt(bX, bY)) {
        	return false;
		}
		
		List<ObjectInstance> objects = state.objectsOfClass(HBlock.CLASS_BLOCK);
		List<HBlock> blocks = new ArrayList<HBlock>();
		for (ObjectInstance obj : objects) { blocks.add((HBlock)obj); }
		
//		Collections.sort(blocks);
//		int startIndex = blocks.indexOf(start);
//		int endIndex = blocks.indexOf(end);
//		int index = startIndex;
		HBlock start = state.getBlockAt(aX, aY);
		HBlock end = state.getBlockAt(bX, bY);
		List<HBlock> toVisit = new ArrayList<HBlock>();
		toVisit.add(start);
		while (toVisit.size() > 0) {
			HBlock block = toVisit.remove(toVisit.size()-1); // pop
			if (block.equals(end)) { return true; }
			int x = (Integer) block.get(HPoint.ATT_X);
			int y = (Integer) block.get(HPoint.ATT_Y);
			HBlock east = state.getBlockAt(x+1, y);
			HBlock north = state.getBlockAt(x, y+1);
			HBlock northEast = state.getBlockAt(x+1, y+1);
			if (east != null) { toVisit.add(east); }
			if (north != null) { toVisit.add(north); }
			if (northEast != null) { toVisit.add(northEast); }
			
//			System.out.println(x + " " + y);
//			if (state.blockAt(x+1, y)) { toVisit.add(blocks.indexOf()); }
//			if (state.blockAt(x, y+1)) { toVisit.add(blocks.indexOf(state.getBlockAt(x, y+1))); }
//			if (state.blockAt(x+1, y+1)) { toVisit.add(blocks.indexOf(state.getBlockAt(x+1, y+1))); }
		}
		
        return false;
    	
    }

    @Override
    public boolean isTrue(OOState s, String... params) {
    	HWall wall = (HWall) s.object(params[0]);
    	return isTrue(s, wall);
    }
    
    public boolean isTrue(OOState s, HWall wall) {
        HPoint wallStart = (HPoint) wall.get(HWall.ATT_START);
        HPoint wallEnd = (HPoint) wall.get(HWall.ATT_END);
        return isTrue(s, wallStart, wallEnd);
    }
    
}
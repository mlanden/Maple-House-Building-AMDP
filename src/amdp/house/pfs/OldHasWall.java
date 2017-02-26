package amdp.house.pfs;

import java.util.List;

import amdp.house.level1.MakeWallState;
import amdp.house.objects.HPoint;
import amdp.house.objects.HWall;
import burlap.mdp.auxiliary.stateconditiontest.StateConditionTest;
import burlap.mdp.core.state.State;
import utils.IntPair;

public class OldHasWall implements StateConditionTest {

	private HPoint start;
	private HPoint end;
	private List<IntPair> others;
	
	public OldHasWall(HPoint start, HPoint end) {
		// sort
		HPoint temp;
        if (start.compareTo(end) > 0) {
        	temp = start; start = end; end = temp;
        }
		this.start = start;
		this.end = end;
		int aX = (Integer) start.get(HPoint.ATT_X);
		int aY = (Integer) start.get(HPoint.ATT_Y);
		int bX = (Integer) end.get(HPoint.ATT_X);
		int bY = (Integer) end.get(HPoint.ATT_Y);
		double dX = bX - aX;
		double dY = bY - aY;
		double nX = Math.abs(dX);
		double nY = Math.abs(dY);
		double signX = Math.signum(dX);
		double signY = Math.signum(dY);
		
		IntPair p = new IntPair(aX, aY);
		for (double ix = 0, iy = 0; ix < nX || iy < nY; ) {
			double halfX = (0.5+ix) / nX;
			double halfY = (0.5+iy) / nY;
			if (halfX == halfY) {
				p.x += signX;
				p.y += signY;
				ix++;
				iy++;
			} else if (halfX < halfY) {
				p.x += signX;
				ix++;
			} else {
				p.y += signY;
				iy++;
			}
			if (p.x == bX && p.y == bY) {
				// dont add the last point
				break;
			}
			others.add(new IntPair(p.x, p.y));
		}

	}
	
	public HPoint getStart() {
		return start;
	}
	
	public HPoint getEnd() {
		return end;
	}

	@Override
	public boolean satisfies(State s) {
		MakeWallState state = (MakeWallState) s;
		int aX = (Integer) start.get(HPoint.ATT_X);
		int aY = (Integer) start.get(HPoint.ATT_Y);
		int bX = (Integer) end.get(HPoint.ATT_X);
		int bY = (Integer) end.get(HPoint.ATT_Y);
		if (!state.blockAt(aX, aY)) {
			return false;
		}
		if (!state.blockAt(bX, bY)) {
			return false;
		}
		for (IntPair point : others) {
			if (!state.blockAt(point.x, point.y)) {
				return false;
			}
		}
		return true;
	}

}

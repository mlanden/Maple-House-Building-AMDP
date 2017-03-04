package amdp.house.level2;

import java.util.ArrayList;
import java.util.List;

import amdp.house.base.HouseBaseState;
import amdp.house.objects.HPoint;
import amdp.house.objects.HWall;
import burlap.mdp.core.state.State;
import utils.IntPair;

public class HasFinishedWall {

	private List<IntPair> others;
	
	public HasFinishedWall() {
		this.others = new ArrayList<IntPair>();
	}
	
	public boolean satisfies(State s, HWall goal) {
		MakeWallState state = (MakeWallState) s;
		HPoint start = (HPoint) goal.get(HWall.ATT_START);//state.getGoalWall().get(HWall.ATT_START);
		HPoint end = (HPoint) goal.get(HWall.ATT_END);//state.getGoalWall().get(HWall.ATT_END);
		// sort
		HPoint temp;
        if (start.compareTo(end) > 0) {
        	temp = start; start = end; end = temp;
        }
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
		return checkLineMinimal(state, aX, aY, bX, bY);
//		return checkLineSuperCovered(state, aX, aY, bX, bY);
	}
	
	public static double interpolate(double a, double b, double degree) {
		return a + (degree * (b - a));
	}
	
	public boolean checkLineMinimal(HouseBaseState state, int aX, int aY, int bX, int bY) {
		others.clear(); // uncomment to force it to recalculate every time
		if (others.size() < 1) {
			double dX = bX - aX;
			double dY = bY - aY;
			double distance = Math.max(Math.abs(dX), Math.abs(dY));
			for (double step = 0; step <= distance; step++) {
				double degree = distance == 0 ? 0.0 : step / distance;
				double interpX = interpolate(aX, bX, degree);
				double interpY = interpolate(aY, bY, degree);
				IntPair rounded = new IntPair((int) Math.round(interpX), (int) Math.round(interpY));
				others.add(rounded);
			}
		}
		for (IntPair point : others) {
			if (!state.blockAt(point.x, point.y)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean checkLineSuperCovered(MakeWallState state, int aX, int aY, int bX, int bY) {
		others.clear(); // comment this out to have it not recalculate every time
		if (others.size() < 1) {
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
		for (IntPair point : others) {
			if (!state.blockAt(point.x, point.y)) {
				return false;
			}
		}
		return true;
	}

}

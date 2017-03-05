package amdp.house.level2;

import java.util.ArrayList;
import java.util.List;

import amdp.house.base.HouseBaseState;
import amdp.house.objects.HPoint;
import amdp.house.objects.HWall;
import burlap.mdp.core.oo.propositional.PropositionalFunction;
import burlap.mdp.core.oo.state.OOState;
import utils.IntPair;

public class HasGoalWallPF extends PropositionalFunction {

	public static final String PF_HAS_GOAL_WALL = "pfHasGoalWall";
	
	public HasGoalWallPF() {
		super(PF_HAS_GOAL_WALL, new String[]{HPoint.CLASS_POINT, HPoint.CLASS_POINT});
	}

	@Override
	public boolean isTrue(OOState s, String... params) {
		HouseBaseState state = (HouseBaseState) s;
		String startName = params[0];
		String endName = params[1];
		HPoint start = (HPoint) state.object(startName);
		HPoint end = (HPoint) state.object(endName);
//		String wallName = "wall_"+startName+"_"+endName;
//		HWall goal = new HWall(wallName, start, end, false);
		boolean result = satisfies(state, start, end);

		return result;
	}

//	private boolean satisfies(State s, HWall goal) {
	private boolean satisfies(HouseBaseState state, HPoint start, HPoint end) {

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
	
	public static List<IntPair> getPointsInLine(int aX, int aY, int bX, int bY) {
		double dX = bX - aX;
		double dY = bY - aY;
		double distance = Math.max(Math.abs(dX), Math.abs(dY));
		List<IntPair> points = new ArrayList<IntPair>();
		for (double step = 0; step <= distance; step++) {
			double degree = distance == 0 ? 0.0 : step / distance;
			double interpX = interpolate(aX, bX, degree);
			double interpY = interpolate(aY, bY, degree);
			int nX = (int) Math.round(interpX);
			int nY = (int) Math.round(interpY);
			IntPair rounded = new IntPair(nX, nY);
			points.add(rounded);
		}
		return points;
	}
	
	public static int getNumBlocksRemaining(MakeWallState state, HPoint start, HPoint end) {
		int aX = (Integer) start.get(HPoint.ATT_X);
		int aY = (Integer) start.get(HPoint.ATT_Y);
		int bX = (Integer) end.get(HPoint.ATT_X);
		int bY = (Integer) end.get(HPoint.ATT_Y);
		List<IntPair> points = getPointsInLine(aX, aY, bX, bY);
		int goalNumber = points.size();
		int numRemaining = goalNumber;
		for (IntPair point : points) {
			if (state.blockAt(point.x, point.y)) {
				numRemaining -= 1;
			}
		}
		return numRemaining;
	}
	
	public static boolean checkLineMinimal(HouseBaseState state, int aX, int aY, int bX, int bY) {
		List<IntPair> points = new ArrayList<IntPair>();
		points = getPointsInLine(aX, aY, bX, bY);
		for (IntPair point : points) {
			if (!state.blockAt(point.x, point.y)) {
				return false;
			}
		}
		return true;
	}
	
	
	public static boolean checkLineSuperCovered(MakeWallState state, int aX, int aY, int bX, int bY) {
		List<IntPair> others = new ArrayList<IntPair>();
//		if (others.size() < 1) {
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
//		}
		for (IntPair point : others) {
			if (!state.blockAt(point.x, point.y)) {
				return false;
			}
		}
		return true;
	}

//	public static int getNumBlocksRemaining(MakeWallState state, HPoint start, HPoint end) {
//		int aX = (Integer) start.get(HPoint.ATT_X);
//		int aY = (Integer) start.get(HPoint.ATT_Y);
//		int bX = (Integer) end.get(HPoint.ATT_X);
//		int bY = (Integer) end.get(HPoint.ATT_Y);
//		double dX = bX - aX;
//		double dY = bY - aY;
//		double distance = Math.max(Math.abs(dX), Math.abs(dY));
//		return (int) distance;
//	}
}

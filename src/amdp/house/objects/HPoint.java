package amdp.house.objects;

import java.util.Arrays;
import java.util.List;

import utils.MutableObject;
import utils.MutableObjectInstance;

public class HPoint extends MutableObject implements MutableObjectInstance, Comparable<HPoint> {

	public static long GLOBAL_ID_HPOINT = 0;
	public static final String CLASS_POINT = "point";
	public static final String ATT_X = "x";
	public static final String ATT_Y = "y";
	
	public String name;
	
	private final static List<Object> keys = Arrays.<Object>asList(
			HPoint.ATT_X,
			HPoint.ATT_Y
	);
	
	public HPoint(String name, Integer x, Integer y, boolean giveNewID) {
		this.name = name;
		this.set(HPoint.ATT_X, x);
		this.set(HPoint.ATT_Y, y);
		if (giveNewID) {
			this.name += GLOBAL_ID_HPOINT++;
		}
	}
	
	@Override
	public String className() {
		return CLASS_POINT;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public List<Object> variableKeys() {
		return keys;
	}

	@Override
	public HPoint copyWithName(String objectName) {
		return new HPoint(
				objectName,
				(Integer) get(HPoint.ATT_X),
				(Integer) get(HPoint.ATT_Y),
				false
		);
	}

	@Override
	public HPoint copy() {
		return copyWithName(name);
	}

	@Override
	public int compareTo(HPoint other) {
		Integer thisX = (Integer) this.get(ATT_X);
		Integer otherX = (Integer) other.get(ATT_X);
		Integer thisY = (Integer) this.get(ATT_Y);
		Integer otherY = (Integer) other.get(ATT_Y);
		if (thisX != otherX) {
			return thisX.compareTo(otherX);
		} else {
			return thisY.compareTo(otherY);
		}
	}
	
	public static double distanceChebyshev(HPoint src, HPoint dst) {
		int aX = (Integer) src.get(HPoint.ATT_X);
		int aY = (Integer) src.get(HPoint.ATT_Y);
		int bX = (Integer) dst.get(HPoint.ATT_X);
		int bY = (Integer) dst.get(HPoint.ATT_Y);
		double distance = Math.max(Math.abs(bX-aX),Math.abs(bY-aY));
		return distance;
	}
	
	public String toString() {
		return name + " (" + get(ATT_X) + ", " + get(ATT_Y) + ")";
	}
	
}

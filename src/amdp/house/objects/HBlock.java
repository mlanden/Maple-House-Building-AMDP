package amdp.house.objects;

import java.util.Arrays;
import java.util.List;

import burlap.mdp.core.oo.state.ObjectInstance;

public class HBlock extends HPoint {

	public static final String CLASS_BLOCK = "block";
	public static final String ATT_FINISHED = "finished";
	
	private final static List<Object> keys = Arrays.<Object>asList(
			HPoint.ATT_X,
			HPoint.ATT_Y,
			HBlock.ATT_FINISHED
	);
	
	public HBlock(String name, int x, int y, boolean finished, boolean giveNewID) {
		super(name, x, y, giveNewID);
		this.set(ATT_FINISHED, finished);
	}

	@Override
	public String className() {
		return CLASS_BLOCK;
	}

	@Override
	public HBlock copyWithName(String objectName) {
		return new HBlock(
				objectName,
				(Integer) get(HPoint.ATT_X),
				(Integer) get(HPoint.ATT_Y),
				(Boolean) get(ATT_FINISHED),
				false
		);
	}

	@Override
	public HBlock copy() {
		return copyWithName(name);
	}
	
	@Override
	public String toString() {
		return super.toString() + ", finished: " + get(ATT_FINISHED);
	}
	
}

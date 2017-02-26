package amdp.house.objects;

import burlap.mdp.core.oo.state.ObjectInstance;

public class HBlock extends HPoint {

	public static final String CLASS_BLOCK = "block";
	
	public HBlock(String name, int x, int y, boolean giveNewID) {
		super(name, x, y, giveNewID);
		
		// redo name/ID system to just use the block_x_y as name
		
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
				false
		);
	}

	@Override
	public HBlock copy() {
		return copyWithName(name);
	}
}

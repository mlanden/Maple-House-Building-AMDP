package amdp.house.objects;

import java.util.Arrays;
import java.util.List;

import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.State;
import utils.MutableObject;
import utils.MutableObjectInstance;

public class HWall extends MutableObject implements MutableObjectInstance {

	public static final String CLASS_WALL = "wall";
	public static final String ATT_START = "start";
	public static final String ATT_END = "end";
	
	public String name;
	
	private final static List<Object> keys = Arrays.<Object>asList(
			ATT_START,
			ATT_END
	);
	
	public HWall(String name, HPoint start, HPoint end) {
		this.name = name;
		this.set(ATT_START, start);
		this.set(ATT_END, end);
	}
	
	@Override
	public String className() {
		return CLASS_WALL;
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
	public HWall copyWithName(String objectName) {
		return new HWall(
				objectName,
				(HPoint) get(ATT_START),
				(HPoint) get(ATT_END)
		);
	}

	@Override
	public HWall copy() {
		return new HWall(
				name,
				(HPoint) get(ATT_START),
				(HPoint) get(ATT_END)
		);
	}

}

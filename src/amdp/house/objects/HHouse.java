package amdp.house.objects;

import java.util.Arrays;
import java.util.List;

import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.State;
import utils.MutableObject;
import utils.MutableObjectInstance;

public class HHouse extends MutableObject implements MutableObjectInstance {

	public static final String CLASS_HOUSE = "house";
	
	public String name;
	
	private static final List<Object> keys = Arrays.<Object>asList(

	);
	
	public HHouse(String name) {
		this.name = name;
	}
	
	@Override
	public String className() {
		return CLASS_HOUSE;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public ObjectInstance copyWithName(String objectName) {
		return new HHouse(objectName);
	}

	@Override
	public List<Object> variableKeys() {
		return keys;
	}

	@Override
	public State copy() {
		return new HHouse(this.name);
	}
	
}
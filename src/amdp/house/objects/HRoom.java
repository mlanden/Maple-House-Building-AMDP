package amdp.house.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.State;
import utils.MutableObject;
import utils.MutableObjectInstance;

public class HRoom extends MutableObject implements MutableObjectInstance {

	public static final String CLASS_ROOM = "room";
	
	public static final String ATT_CORNERS = "corners";
	public static final String ATT_FINISHED = "finished";
	
	public String name;
//	public List<HPoint> corners;
	
	private static final List<Object> keys = Arrays.<Object>asList(
		ATT_CORNERS,
		ATT_FINISHED
	);
	
	public HRoom(String name) {
//		corners = new ArrayList<HPoint>();
		this.set(ATT_CORNERS, new ArrayList<HPoint>());
		this.set(ATT_FINISHED, (Boolean) false);
	}
	
	public HRoom(String name, List<HPoint> corners, Boolean finished) {
		this.name = name;
		this.set(ATT_CORNERS, corners);
		this.set(ATT_FINISHED, finished);
	}
	
	@Override
	public String className() {
		return CLASS_ROOM;
	}

	@Override
	public String name() {
		return name;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ObjectInstance copyWithName(String objectName) {
		return new HRoom(objectName, (List<HPoint>) this.get(ATT_CORNERS), (Boolean) this.get(ATT_FINISHED));
	}

	@Override
	public List<Object> variableKeys() {
		return keys;
	}

	@SuppressWarnings("unchecked")
	@Override
	public State copy() {
		return new HRoom(this.name, (List<HPoint>) this.get(ATT_CORNERS), (Boolean) this.get(ATT_FINISHED));
	}
	
	@SuppressWarnings("unchecked")
	public List<HPoint> getCorners() {
		return (List<HPoint>) get(ATT_CORNERS);
	}
	
}

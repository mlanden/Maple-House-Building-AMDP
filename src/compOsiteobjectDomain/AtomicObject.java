package compOsiteobjectDomain;

import static burlap.domain.singleagent.gridworld.GridWorldDomain.VAR_TYPE;

import java.util.Arrays;
import java.util.List;

import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.State;

public class AtomicObject implements ObjectInstance, Comparable<AtomicObject>{
	
	protected String className;
	protected String name;
	public int type;
	public int x, y;
	
	protected final static List<Object> keys = Arrays.<Object>asList(CompObjDomain.VAR_X, CompObjDomain.VAR_Y);
	
	public AtomicObject()
	{
		className = CompObjDomain.CLASS_ATOMICOBJECT;
	}
	
	public AtomicObject(int x, int y)
	{
		this();
		this.x = x;
		this.y = y;
	}
	
	public AtomicObject(int x, int y, String name)
	{
		this(x, y);
		this.name = name;
	}
	
	public AtomicObject(int x, int y, String name, int type)
	{
		this(x, y, name);
		this.type = type;
	}
	public void setX(int val)
	{
		x = val;
	}
	
	public void setY(int val)
	{
		y = val;
	}

	@Override
	public List<Object> variableKeys() {
		return keys;
	}

	@Override
	public Object get(Object variableKey) {
		if(!(variableKey instanceof String)){
			throw new RuntimeException("GridAgent variable key must be a string");
		}

		String key = (String)variableKey;
		if(key.equals(CompObjDomain.VAR_X)){
			return x;
		}
		else if(key.equals(CompObjDomain.VAR_Y)){
			return y;
		}
		else if(key.equals(VAR_TYPE)){
			return type;
		}

		throw new RuntimeException("Unknown key " + key);
	}

	@Override
	public AtomicObject copy() {
		return new AtomicObject(x, y, null, type);
	}

	@Override
	public String className() {
		return className;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public AtomicObject copyWithName(String objectName) {
		return new AtomicObject(x, y, objectName, type);
	}

	@Override
	public int compareTo(AtomicObject a) {
		if((Integer)a.get(CompObjDomain.VAR_X) != this.x)
			return ((Integer)x).compareTo((Integer)a.get(CompObjDomain.VAR_X));
		else
			return ((Integer)y).compareTo((Integer)a.get(CompObjDomain.VAR_Y));
	}

}

package amdp.houseBuilding.level1.state;

import java.util.Arrays; 
import java.util.List;

import amdp.houseBuilding.level1.domain.L1DomainGenerator;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.UnknownKeyException;
import compositeObjectDomain.CompObjDomain;

public class L1Agent implements ObjectInstance{

	public int x, y;
	protected String name;
	private static final List<Object> keys = Arrays.<Object>asList
			(CompObjDomain.VAR_X, CompObjDomain.VAR_Y);
	
	public L1Agent(){
		name = "L1Agent";
	}
	
	public L1Agent(int x, int y){
		this();
		this.x = x;
		this.y = y;
	}
	
	public L1Agent(int x, int y, String name){
		this(x, y);
		this.name = name;
	}
	
	public L1Agent copy() {
		return new L1Agent(x, y, name);
	}

	public Object get(Object variableKey) {
		if(variableKey.equals(CompObjDomain.VAR_X)){
			return x;
		}else if(variableKey.equals(CompObjDomain.VAR_Y)){
			return y;
		}
		
		throw new UnknownKeyException(variableKey);
	}

	public List<Object> variableKeys() {
		return keys;
	}

	public String className() {
		return L1DomainGenerator.CLASS_AGENT;
	}

	public L1Agent copyWithName(String newName) {
		return new L1Agent(x, y, newName);
	}

	public String name() {
		return name;
	}
}

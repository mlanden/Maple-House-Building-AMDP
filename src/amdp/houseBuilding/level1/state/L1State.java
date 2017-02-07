package amdp.houseBuilding.level1.state;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import burlap.mdp.core.oo.state.MutableOOState;
import burlap.mdp.core.oo.state.OOStateUtilities;
import burlap.mdp.core.oo.state.OOVariableKey;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.oo.state.exceptions.UnknownClassException;
import burlap.mdp.core.state.MutableState;
import burlap.mdp.core.state.State;
import burlap.mdp.core.state.UnknownKeyException;
import compositeObjectDomain.Wall;
import static amdp.houseBuilding.level1.domain.L1DomainGenerator.CLASS_AGENT;
import static amdp.houseBuilding.level1.domain.L1DomainGenerator.CLASS_WALL;
import static compositeObjectDomain.CompObjDomain.VAR_X;
import static compositeObjectDomain.CompObjDomain.VAR_Y;

public class L1State implements MutableOOState{

	protected L1Agent agent;
	public List<Wall> walls;
	
	public L1State() {
	
	}
	
	public L1State(int x, int y, List<Wall> walls){
		this(new L1Agent(x,y), walls);
	}
	
	public L1State(L1Agent agent, List<Wall> walls){
		this.agent = agent;
		this.walls = walls;
	}
	
	public int numObjects() {
		return walls.size() + 1;
	}
	
	public ObjectInstance object(String name) {
		if(name.equals(agent.name())){
			return agent;
		}
		int wallIndex = wallIndex(name);
		if(wallIndex != -1){
			return walls.get(wallIndex); 
		}
		return null;
	}

	protected int wallIndex(String name){
		for(int i = 0; i < walls.size(); i++){
			if(walls.get(i).name().equals(name)){
				return i;
			}
		}
		return -1;
	}
	public List<ObjectInstance> objects() {
		List<ObjectInstance> objects = new ArrayList<ObjectInstance>();
		objects.add(agent);
		objects.addAll(walls);
		return objects();
	}
	
	public List<ObjectInstance> objectsOfClass(String oclass) {
		if(oclass.equals(CLASS_AGENT)){
			return Arrays.<ObjectInstance>asList(agent); 
		}else if(oclass.equals(CLASS_WALL)){
			return new ArrayList<ObjectInstance>(walls);
		}
		throw new UnknownClassException(oclass);
	}

	
	public L1State copy() {
		return new L1State(agent, walls);
	}

	
	public Object get(Object variableKey) {
		OOVariableKey key = OOStateUtilities.generateKey(variableKey);
		if(key.obName.equals(agent.name())){
			return agent.get(key.obVarKey);
		}
		int wallIndex = wallIndex(key.obName);
		if(wallIndex != -1){
			return walls.get(wallIndex).get(key.obVarKey);
		}
		return new UnknownKeyException(variableKey);
	}

	
	public List<Object> variableKeys() {
		return OOStateUtilities.flatStateKeys(this);
	}

	
	public MutableState set(Object variableKey, Object value) {
		
	}

	
	public MutableOOState addObject(ObjectInstance arg0) {
		return null;
	}

	
	public MutableOOState removeObject(String arg0) {
		return null;
	}

	
	public MutableOOState renameObject(String arg0, String arg1) {
		return null;
	}
	
	public L1Agent touchAgent(){
		this.agent = agent.copy();
		return agent;
	}
	
	public List<Wall> touchWalls(){
		this.walls = new ArrayList<Wall>();
		return walls;
	}
}

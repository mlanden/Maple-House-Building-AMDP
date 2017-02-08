package amdp.houseBuilding.level1.state;

import java.util.ArrayList; 
import java.util.Arrays;
import java.util.List;

import javax.management.RuntimeErrorException;

import burlap.mdp.core.oo.state.MutableOOState;
import burlap.mdp.core.oo.state.OOStateUtilities;
import burlap.mdp.core.oo.state.OOVariableKey;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.oo.state.exceptions.UnknownClassException;
import burlap.mdp.core.state.MutableState;
import burlap.mdp.core.state.State;
import burlap.mdp.core.state.StateUtilities;
import burlap.mdp.core.state.UnknownKeyException;
import compositeObjectDomain.AtomicObject;
import compositeObjectDomain.Wall;

import static amdp.houseBuilding.level1.domain.L1DomainGenerator.CLASS_AGENT;
import static amdp.houseBuilding.level1.domain.L1DomainGenerator.CLASS_WALL;

import static compositeObjectDomain.CompObjDomain.VAR_X;
import static compositeObjectDomain.CompObjDomain.VAR_Y;
import static amdp.houseBuilding.level1.domain.L1DomainGenerator.DOORS;
import static amdp.houseBuilding.level1.domain.L1DomainGenerator.START_X;
import static amdp.houseBuilding.level1.domain.L1DomainGenerator.START_Y;
import static amdp.houseBuilding.level1.domain.L1DomainGenerator.END_X;
import static amdp.houseBuilding.level1.domain.L1DomainGenerator.END_Y;
import static amdp.houseBuilding.level1.domain.L1DomainGenerator.LENGTH;

public class L1State implements MutableOOState{

	protected L1Agent agent;
	public List<Wall> walls;
	public int[][] map;
	
	public L1State() {
	
	}
	
	public L1State(int x, int y, int[][] map, List<Wall> walls){
		this(new L1Agent(x,y), map, walls);
	}
	
	public L1State(L1Agent agent, int[][] map, List<Wall> walls){
		this.agent = agent;
		this.walls = walls;
		this.map = map;
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
		return new L1State(agent, map, walls);
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

	public int[][] getMap(){
		return map;
	}
	
	public List<Object> variableKeys() {
		return OOStateUtilities.flatStateKeys(this);
	}

	
	public MutableState set(Object variableKey, Object value) {
		OOVariableKey key = OOStateUtilities.generateKey(variableKey);
		
		if(key.obName.equals(agent.name)){
			if(key.obVarKey.equals(VAR_X)){
				int x = (int) StateUtilities.stringOrNumber(value);
				touchAgent().x = x;
			}else if(key.obVarKey.equals(VAR_Y)){
				int y = (int) StateUtilities.stringOrNumber(value);
				touchAgent().y = y;
			}
		}
		int wallUndex = wallIndex(key.obName);
		if(wallUndex != -1){
			if(key.obVarKey.equals(DOORS)){
				if(value instanceof List<?>){
					touchWalls().get(wallUndex).doors = (List<AtomicObject>) value;
				}
				throw new RuntimeException("The value must be of type List<AtomicObject");
			}else if(key.obVarKey.equals(START_X)){
				int startx = (int) StateUtilities.stringOrNumber(value);
				touchWalls().get(wallUndex).startX = startx;
			}else if(key.obVarKey.equals(START_Y)){
				int starty = (int) StateUtilities.stringOrNumber(value);
				touchWalls().get(wallUndex).startY = starty;
			}else if(key.obVarKey.equals(END_X)){
				int endx = (int) StateUtilities.stringOrNumber(value);
				touchWalls().get(wallUndex).endX = endx;
			}else if(key.obVarKey.equals(END_Y)){
				int endy = (int) StateUtilities.stringOrNumber(value);
				touchWalls().get(wallUndex).endY = endy;
			}else if(key.obVarKey.equals(LENGTH)){
				int length = (int) StateUtilities.stringOrNumber(value);
				touchWalls().get(wallUndex).length = length;
			}
		}
		return this;
	}

	
	public MutableOOState addObject(ObjectInstance o) {
		if(!(o instanceof Wall)){
			throw new RuntimeException("Can only add walls");
		}
		Wall w = (Wall)o;
		touchWalls().add(w);
		
		return this;
	}

	
	public MutableOOState removeObject(String oname) {
		if(agent.name().equals(oname)){
			throw new RuntimeException("Cannot remove agent");
		}
		
		int wallIndex = wallIndex(oname);
		touchWalls().remove(wallIndex);
		
		return this;
	}

	
	public MutableOOState renameObject(String objName, String newName) {
		if(agent.name.equals(objName)){
			L1Agent nagent = agent.copyWithName(newName);
			this.agent = nagent;
		}else{
			int wallIndex = wallIndex(objName);
			if(wallIndex == -1){
				throw new RuntimeException("Cant find object " + objName);
			}
			
			Wall nw = this.walls.get(wallIndex).copyWithName(newName);
			walls.remove(wallIndex);
			
			//maintain the same order
			walls.add(wallIndex, nw);
		}
		
		return this;
	}
	
	public L1Agent touchAgent(){
		this.agent = agent.copy();
		return agent;
	}
	
	public List<Wall> touchWalls(){
		this.walls = new ArrayList<Wall>();
		return walls;
	}
	public 
}

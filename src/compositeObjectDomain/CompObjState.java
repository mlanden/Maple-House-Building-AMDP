package compositeObjectDomain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import burlap.mdp.core.oo.propositional.PropositionalFunction;
import burlap.mdp.core.oo.state.MutableOOState;
import burlap.mdp.core.oo.state.OOStateUtilities;
import burlap.mdp.core.oo.state.OOVariableKey;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.MutableState;
import burlap.mdp.core.state.State;
import burlap.mdp.core.state.StateUtilities;

public class CompObjState implements MutableOOState {
	
	public CompObjAgent agent;
	public ArrayList<AtomicObject> objects;
	public AtomicObject [][] objectsMap;
	int [][] map;
	
	public CompObjState()
	{
		
	}
	
	public CompObjState(int x, int y, int [][] map, AtomicObject...objects)
	{
		this(new CompObjAgent(x, y), map, objects);
	}
	
	public CompObjState(CompObjAgent agent, int [][] map, AtomicObject...objects)
	{
		this.agent = agent;
		if(objects.length == 0){
			this.objects = new ArrayList<AtomicObject>();
		}
		else {
			this.objects = (ArrayList<AtomicObject>) Arrays.asList(objects);
		}
		objectsMap = new AtomicObject[map.length][map[0].length];
		for(AtomicObject o:objects)
		{
			objectsMap[(Integer)o.get(CompObjDomain.VAR_X)][(Integer)o.get(CompObjDomain.VAR_Y)] = o;
		}
		this.map = map.clone();
	}
	
	public CompObjState(CompObjAgent agent, int [][] map, ArrayList<AtomicObject> objects)
	{
		this.agent = agent;
		this.objects = objects;
		this.map = map.clone();
		objectsMap = new AtomicObject[map.length][map[0].length];
		for(AtomicObject o:objects)
		{
			objectsMap[(Integer)o.get(CompObjDomain.VAR_X)][(Integer)o.get(CompObjDomain.VAR_Y)] = o;
		}
	}

	@Override
	public int numObjects() {
		return objects.size() + 1;
	}

	@Override
	public ObjectInstance object(String oname) {
		if(oname.equals(agent.name())){
			return agent;
		}
		int ind = this.objectInd(oname);
		if(ind != -1){
			return objects.get(ind);
		}
		return null;
	}
	
	protected int objectInd(String oname){
		int ind = -1;
		for(int i = 0; i < objects.size(); i++){
			if(objects.get(i).name().equals(oname)){
				ind = i;
				break;
			}
		}
		return ind;
	}

	@Override
	public List<ObjectInstance> objects() {
		List<ObjectInstance> obs = new ArrayList<ObjectInstance>(1+objects.size());
		obs.add(agent);
		obs.addAll(objects);
		return obs;
	}

	@Override
	public List<ObjectInstance> objectsOfClass(String oclass) {
		if(oclass.equals(CompObjDomain.CLASS_AGENT)){
			return Arrays.<ObjectInstance>asList(agent);
		}
		else if(oclass.equals(CompObjDomain.CLASS_ATOMICOBJECT) ){
			return new ArrayList<ObjectInstance>(objects);
		}
		throw new RuntimeException("Unknown class type " + oclass);
	}

	@Override
	public List<Object> variableKeys() {
		return OOStateUtilities.flatStateKeys(this);
	}
	
	public int[][] getMap()
	{
		return map;
	}

	@Override
	public Object get(Object variableKey) {
		OOVariableKey key = OOStateUtilities.generateKey(variableKey);
		if(key.obName.equals(agent.name())){
			return agent.get(key.obVarKey);
		}
		int ind = this.objectInd(key.obName);
		if(ind == -1){
			throw new RuntimeException("Cannot find object " + key.obName);
		}
		return objects.get(ind).get(key.obVarKey);
	}

	@Override
	public State copy() {
		ArrayList<AtomicObject> objectsCopy = new ArrayList<AtomicObject>();
		for(AtomicObject a: objects)
		{
			objectsCopy.add(a.copyWithName(a.name()));
		}
		int [][] newMap = new int[map.length][map[0].length];
		for(int i = 0; i < map.length; i++){
			for(int j = 0; j < map[0].length; j++){
				newMap[i][j] = map[i][j];
			}
		}
		return new CompObjState(agent.copyWithName(agent.name()), newMap, objectsCopy);
	}

	@Override
	public MutableState set(Object variableKey, Object value) {
		OOVariableKey key = OOStateUtilities.generateKey(variableKey);
		int iv = StateUtilities.stringOrNumber(value).intValue();
		if(key.obName.equals(agent.name())){
			if(key.obVarKey.equals(CompObjDomain.VAR_X)){
				touchAgent().x = iv;
			}
			else if(key.obVarKey.equals(CompObjDomain.VAR_Y)){
				touchAgent().y = iv;
			}
			else{
				throw new RuntimeException("Unknown variable key " + variableKey);
			}
			return this;
		}
		int ind = objectInd(key.obName);
		if(ind != -1){
			if(key.obVarKey.equals(CompObjDomain.VAR_X)){
				touchObject(ind).x = iv;
			}
			else if(key.obVarKey.equals(CompObjDomain.VAR_Y)){
				touchObject(ind).y = iv;
			}
			else if(key.obVarKey.equals(CompObjDomain.VAR_TYPE)){
				touchObject(ind).type = iv;
			}
			else{
				throw new RuntimeException("Unknown variable key " + variableKey);
			}

			return this;
		}

		throw new RuntimeException("Unknown variable key " + variableKey);
	}

	public CompObjAgent touchAgent(){
		this.agent = agent.copyWithName(agent.name());
		return agent;
	}

	public ArrayList<AtomicObject> touchObjects(){
		this.objects = new ArrayList<AtomicObject>(objects);
		for(AtomicObject o:objects)
		{
			objectsMap[(Integer)o.get(CompObjDomain.VAR_X)][(Integer)o.get(CompObjDomain.VAR_Y)] = o;
		}
		return objects;
	}
	
	public ArrayList<AtomicObject> deepTouchObjects(){
		ArrayList<AtomicObject> nlocs = new ArrayList<AtomicObject>(objects.size());
		for(AtomicObject loc : objects){
			nlocs.add(loc.copyWithName(loc.name()));
		}
		objects = nlocs;
		for(AtomicObject o:objects)
		{
			objectsMap[(Integer)o.get(CompObjDomain.VAR_X)][(Integer)o.get(CompObjDomain.VAR_Y)] = o;
		}
		return objects;
	}

	public AtomicObject touchObject(int ind){
		AtomicObject n = objects.get(ind).copyWithName(objects.get(ind).name());
		touchObjects().remove(ind);
		objects.add(ind, n);
		objectsMap[(Integer)n.get(CompObjDomain.VAR_X)][(Integer)n.get(CompObjDomain.VAR_Y)] = objects.get(ind);
		return n;
	}
	@Override
	public MutableOOState addObject(ObjectInstance o) {
		if(!(o instanceof AtomicObject)){
			throw new RuntimeException("Can only add AtomicObject objects to a GridWorldState.");
		}
		AtomicObject obj = (AtomicObject)o;

		//copy on write
		touchObjects().add(obj);
		objectsMap[(Integer)obj.get(CompObjDomain.VAR_X)][(Integer)obj.get(CompObjDomain.VAR_Y)] = obj;

		return this;
	}

	@Override
	public MutableOOState removeObject(String oname) {
		if(oname.equals(agent.name())){
			throw new RuntimeException("Cannot remove agent object from state");
		}
		int ind = this.objectInd(oname);
		if(ind == -1){
			throw new RuntimeException("Cannot find object " + oname);
		}

		objectsMap[(Integer)objects.get(ind).get(CompObjDomain.VAR_X)][(Integer)objects.get(ind).get(CompObjDomain.VAR_Y)] = null;

		//copy on write
		touchObjects().remove(ind);

		return this;
	}

	@Override
	public MutableOOState renameObject(String objectName, String newName) {
		if(objectName.equals(agent.name())){
			CompObjAgent nagent = agent.copyWithName(newName);
			this.agent = nagent;
		}
		else{
			int ind = this.objectInd(objectName);
			if(ind == -1){
				throw new RuntimeException("Cannot find object " + objectName);
			}

			//copy on write
			AtomicObject obj = this.objects.get(ind).copyWithName(newName);
			touchObjects().remove(ind);
			objects.add(ind, obj);

		}

		return this;
	}
	
	public String toString()
	{
		return OOStateUtilities.ooStateToString(this);
	}
	
	public void checkForWalls(CompObjState s, int start, int end, ArrayList<AtomicObject> selection)
	{
		CompObjDomain temp = new CompObjDomain(1, 1);
		List<PropositionalFunction> pfs = temp.generatePfs();
		//List<ObjectInstance> objects = s.objectsOfClass(CompObjDomain.CLASS_ATOMICOBJECT);
		for(int i = start;i < end; i++)
		{
			selection.add(objects.get(i));
			agent.setSelection(selection);
			checkForWalls(s, i + 1, end, selection);
			//CompObjDomain.AreBarriers ar = new CompObjDomain.AreBarriers(CompObjDomain.PF_AreBarriers, new String[]{CompObjDomain.CLASS_AGENT});
			if(pfs.get(0).isTrue(s, "agent"))
			{
				if(pfs.get(1).isTrue(s, "agent"))
				{
					if(pfs.get(2).isTrue(s, "agent"))
					{
						agent.map(selection);
					}
				}
			}
			selection.remove(objects.get(i));
		}
	}

	public void removeObject(int x, int y)
	{
		AtomicObject temp = objectsMap[x][y];
		if(temp == null)
			return;
		removeObject(temp.name());
	}

}

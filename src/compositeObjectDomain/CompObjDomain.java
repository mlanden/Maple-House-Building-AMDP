package compositeObjectDomain;

import java.awt.Component;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import burlap.behavior.policy.Policy;
import burlap.behavior.policy.PolicyUtils;
import burlap.behavior.singleagent.Episode;
import burlap.behavior.singleagent.MDPSolverInterface;
import burlap.behavior.singleagent.auxiliary.EpisodeSequenceVisualizer;
import burlap.behavior.singleagent.learning.LearningAgent;
import burlap.behavior.singleagent.learning.tdmethods.QLearning;
import burlap.behavior.singleagent.planning.Planner;
import burlap.behavior.singleagent.planning.deterministic.DeterministicPlanner;
import burlap.behavior.singleagent.planning.deterministic.uninformed.bfs.BFS;
import burlap.behavior.singleagent.planning.stochastic.valueiteration.ValueIteration;
import burlap.debugtools.RandomFactory;
import burlap.mdp.auxiliary.DomainGenerator;
import burlap.mdp.auxiliary.common.NullTermination;
import burlap.mdp.auxiliary.common.SinglePFTF;
import burlap.mdp.auxiliary.stateconditiontest.StateConditionTest;
import burlap.mdp.auxiliary.stateconditiontest.TFGoalCondition;
import burlap.mdp.core.Domain;
import burlap.mdp.core.StateTransitionProb;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.action.Action;
import burlap.mdp.core.action.UniversalActionType;
import burlap.mdp.core.oo.OODomain;
import burlap.mdp.core.oo.propositional.PropositionalFunction;
import burlap.mdp.core.oo.state.OOState;
import burlap.mdp.core.oo.state.ObjectInstance;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.SADomain;
import burlap.mdp.singleagent.common.SingleGoalPFRF;
import burlap.mdp.singleagent.common.UniformCostRF;
import burlap.mdp.singleagent.environment.SimulatedEnvironment;
import burlap.mdp.singleagent.environment.extensions.EnvironmentObserver;
import burlap.mdp.singleagent.model.FactoredModel;
import burlap.mdp.singleagent.model.RewardFunction;
import burlap.mdp.singleagent.model.statemodel.FullStateModel;
import burlap.mdp.singleagent.oo.OOSADomain;
import burlap.shell.EnvironmentShell;
import burlap.shell.visual.VisualExplorer;
import burlap.statehashing.HashableStateFactory;
import burlap.statehashing.simple.SimpleHashableStateFactory;
import burlap.visualizer.Visualizer;
import compositeObjectDomain.CompObjDomain.GridWorldModel;

public class CompObjDomain implements DomainGenerator {

	public static final String VAR_X = "x";
	
	public static final String VAR_Y = "y";
	
	public static final String VAR_TYPE = "type";
	
	public static final String CLASS_AGENT = "Comp Obj Agent";
	
	public static final String CLASS_ATOMICOBJECT = "Atomic Object";
	
	/**
	 * Constant for the name of the north action
	 */
	public static final String ACTION_NORTH = "north";
	
	/**
	 * Constant for the name of the south action
	 */
	public static final String ACTION_SOUTH = "south";
	
	/**
	 * Constant for the name of the east action
	 */
	public static final String ACTION_EAST = "east";
	
	/**
	 * Constant for the name of the west action
	 */
	public static final String ACTION_WEST = "west";
	
	public static final String ACTION_PLACEBLOCK = "place block";
	
	public static final String ACTION_PLACEDOOR = "place door";

	public static final String ACTION_REMOVEOJECT = "remove object";
	
	public static final List<String> ACTIONS = Arrays.asList(ACTION_PLACEBLOCK, ACTION_PLACEDOOR, ACTION_REMOVEOJECT);
	
	public static final String PF_AreBarriers = "Are Barriers";
	
	public static final String PF_IsStraight = "Is Straight";
	
	public static final String PF_IsContiguous = "Is Contiguous";
	
	public static final String PF_hasSizeWall = "Has Size Wall";
	
	protected int height;
	
	protected int width;
	
	protected int[][] map; 
	
	protected double[][] transitionDynamics;
	
	protected int desiredWallSize;

	protected RewardFunction rf;
	protected static TerminalFunction tf;
	
	public CompObjDomain(int height, int width)
	{
		this.height = height;
		this.width = width;
		this.setDeterministicTransitionDynamics();
		makeEmptyMap();
	}
	
	public CompObjDomain(int [][] map)
	{
		height = map.length;
		width = map[0].length;
		this.map = map.clone();
		this.setDeterministicTransitionDynamics();
	}
	
	public void makeEmptyMap()
	{
		this.map = new int[height][width];
		for(int i = 0;i < height;i++)
		{
			for(int j = 0;j < width;j++)
			{
				map[i][j] = 0;
			}
		}
	}
	
	public int [][] getMap(){
		int [][] cmap = new int[this.map.length][this.map[0].length];
		for(int i = 0; i < this.map.length; i++){
			for(int j = 0; j < this.map[0].length; j++){
				cmap[i][j] = this.map[i][j];
			}
		}
		return cmap;
	}
	
	public void setMap(int [][] map){
		this.width = map.length;
		this.height = map[0].length;
		this.map = map.clone();
	}
	
	public void setDeterministicTransitionDynamics(){
		int na = 7;
		transitionDynamics = new double[na][na];
		for(int i = 0; i < na; i++){
			for(int j = 0; j < na; j++){
				if(i != j){
					transitionDynamics[i][j] = 0.;
				}
				else{
					transitionDynamics[i][j] = 1.;
				}
			}
		}
	}
	
	/**
	 * Sets the domain to use probabilistic transitions. Agent will move in the intended direction with probability probSucceed. Agent
	 * will move in a random direction with probability 1 - probSucceed
	 * @param probSucceed probability to move the in intended direction
	 */
	public void setProbSucceedTransitionDynamics(double probSucceed){
		int na = 7;
		double pAlt = (1.-probSucceed)/3.;
		transitionDynamics = new double[na][na];
		for(int i = 0; i < na; i++){
			for(int j = 0; j < na; j++){
				if(i != j){
					transitionDynamics[i][j] = pAlt;
				}
				else{
					transitionDynamics[i][j] = probSucceed;
				}
			}
		}
	}
	
	public void setTransitionDynamics(double [][] transitionDynamics){
		this.transitionDynamics = transitionDynamics.clone();
	}

	public double [][] getTransitionDynamics(){
		double [][] copy = new double[transitionDynamics.length][transitionDynamics[0].length];
		for(int i = 0; i < transitionDynamics.length; i++){
			for(int j = 0; j < transitionDynamics[0].length; j++){
				copy[i][j] = transitionDynamics[i][j];
			}
		}
		return copy;
	}
	

	/**
	 * Returns this grid world's width
	 * @return this grid world's width
	 */
	public int getWidth() {
		return this.width;
	}

	/**
	 * Returns this grid world's height
	 * @return this grid world's height
	 */
	public int getHeight() {
		return this.height;
	}
	
	public RewardFunction getRf() {
		return rf;
	}

	public void setRf(RewardFunction rf) {
		this.rf = rf;
	}

	public TerminalFunction getTf() {
		return tf;
	}

	public void setTf(TerminalFunction tf) {
		this.tf = tf;
	}
	
	public List<PropositionalFunction> generatePfs()
	{
		List<PropositionalFunction> pfs = Arrays.asList(
				new AreBarriers(PF_AreBarriers, new String[]{CLASS_AGENT}),
				new isStraight(PF_IsStraight, new String[]{CLASS_AGENT}),
				new isContiguous(PF_IsContiguous, new String[]{CLASS_AGENT}),
				new hasSizeWall(PF_hasSizeWall, new String[]{CLASS_AGENT}, desiredWallSize)
				);
		return pfs;
	}
	
	@Override
	public OOSADomain generateDomain() {
		OOSADomain domain = new OOSADomain();
		
		desiredWallSize = 4;
		
		OODomain.Helper.addPfsToDomain(domain, this.generatePfs());

		int [][] cmap = this.getMap();
		
		domain.addStateClass(CLASS_AGENT, CompObjAgent.class).addStateClass(CLASS_ATOMICOBJECT, AtomicObject.class);

		GridWorldModel smodel = new GridWorldModel(cmap, getTransitionDynamics());
		
		RewardFunction rf = new SingleGoalPFRF(domain.propFunction(PF_hasSizeWall), 1000, -1);
		TerminalFunction tf = new SinglePFTF(domain.propFunction(PF_hasSizeWall));

		/*if(rf == null){
			rf = new UniformCostRF();
		}
		if(tf == null){
			tf = new NullTermination();
		}*/


		FactoredModel model = new FactoredModel(smodel, rf, tf);
		domain.setModel(model);
		
		domain.addActionTypes(
				new UniversalActionType(ACTION_NORTH),
				new UniversalActionType(ACTION_SOUTH),
				new UniversalActionType(ACTION_EAST),
				new UniversalActionType(ACTION_WEST),
				new UniversalActionType(ACTION_PLACEBLOCK),
				new UniversalActionType(ACTION_PLACEDOOR),
				new UniversalActionType(ACTION_REMOVEOJECT));
		
		return domain;
	}
	
	protected static int [] movementDirectionFromIndex(int i){

		int [] result = null;

		switch (i) {
			case 0:
				result = new int[]{0,1};
				break;

			case 1:
				result = new int[]{0,-1};
				break;

			case 2:
				result = new int[]{1,0};
				break;

			case 3:
				result = new int[]{-1,0};
				break;

			default:
				break;
		}

		return result;
	}
	
	public static class GridWorldModel implements FullStateModel{


		/**
		 * The map of the world
		 */
		int [][] map;


		/**
		 * Matrix specifying the transition dynamics in terms of movement directions. The first index
		 * indicates the action direction attempted (ordered north, south, east, west) the second index
		 * indicates the actual resulting direction the agent will go (assuming there is no wall in the way).
		 * The value is the probability of that outcome. The existence of walls does not affect the probability
		 * of the direction the agent will actually go, but if a wall is in the way, it will affect the outcome.
		 * For instance, if the agent selects north, but there is a 0.2 probability of actually going east and
		 * there is a wall to the east, then with 0.2 probability, the agent will stay in place.
		 */
		protected double[][] transitionDynamics;

		protected Random rand = RandomFactory.getMapped(0);


		public GridWorldModel(int[][] map, double[][] transitionDynamics) {
			this.map = map;
			this.transitionDynamics = transitionDynamics;
		}

		@Override
		public List<StateTransitionProb> stateTransitions(State s, Action a) {
			
			s = s.copy();

			double [] directionProbs = transitionDynamics[actionInd(a.actionName())];

			List <StateTransitionProb> transitions = new ArrayList<StateTransitionProb>();
			for(int i = 0; i < directionProbs.length; i++){
				double p = directionProbs[i];
				if(p == 0.){
					continue; //cannot transition in this direction
				}
				State ns = s.copy();
				if(i < 4)
				{
					int [] dcomps = movementDirectionFromIndex(i);
					ns = move(ns, dcomps[0], dcomps[1]);
				}
				else
				{
					move(s, ACTIONS.get(i-4));
					CompObjState cos = (CompObjState)s;

					int ax = cos.agent.x;
					int ay = cos.agent.y;
					//map[ax][ay] = 0;
				}

				//make sure this direction doesn't actually stay in the same place and replicate another no-op
				boolean isNew = true;
				for(StateTransitionProb tp : transitions){
					if(tp.s.equals(ns)){
						isNew = false;
						tp.p += p;
						break;
					}
				}

				if(isNew){
					StateTransitionProb tp = new StateTransitionProb(ns, p);
					transitions.add(tp);
				}


			}


			return transitions;
		}

		@Override
		public State sample(State s, Action a) {

			s = s.copy();

			double [] actionProbs = transitionDynamics[actionInd(a.actionName())];
			double roll = rand.nextDouble();
			double curSum = 0.;
			int dir = 0;
			for(int i = 0; i < actionProbs.length; i++){
				curSum += actionProbs[i];
				if(roll < curSum){
					dir = i;
					break;
				}
			}
			
			if(actionInd(a.actionName()) < 4){
				int [] dcomps = movementDirectionFromIndex(dir);
				return move(s, dcomps[0], dcomps[1]);
			}
			else
			{
				return move(s, a);
			}

		}

		/**
		 * Attempts to move the agent into the given position, taking into account walls and blocks
		 * @param s the current state
		 * @param xd the attempted new X position of the agent
		 * @param yd the attempted new Y position of the agent
		 * @return input state s, after modification
		 */
		protected State move(State s, int xd, int yd){

			CompObjState cos = (CompObjState)s;
			
			int [][] map = (int[][]) cos.getMap();

			int ax = cos.agent.x;
			int ay = cos.agent.y;

			int nx = ax+xd;
			int ny = ay+yd;

			//hit wall, so do not change position
			if(nx < 0 || nx >= map.length || ny < 0 || ny >= map[0].length  ||
					(xd > 0 && (map[ax][ay] == 3 || map[ax][ay] == 4)) || (xd < 0 && (map[nx][ny] == 3 || map[nx][ny] == 4)) ||
					(yd > 0 && (map[ax][ay] == 2 || map[ax][ay] == 4)) || (yd < 0 && (map[nx][ny] == 2 || map[nx][ny] == 4)) ){
				nx = ax;
				ny = ay;
			}

			CompObjAgent nagent = cos.touchAgent();
			nagent.x = nx;
			nagent.y = ny;
			
			return s;
		}
		
		protected State move(State s, String a)
		{
			CompObjState cos = (CompObjState)s;
			int [][] map = (int[][]) cos.getMap();

			int ax = cos.agent.x;
			int ay = cos.agent.y;
			
			if(a.equals(ACTION_PLACEBLOCK))
			{
				if(map[ax][ay] == 1)
					return cos;
				Block newBlock = new Block(ax, ay, "Block " + ax + ", " +  ay);
				cos.addObject(newBlock);
				map[ax][ay] = 1;
				
				cos.checkForWalls(cos, 0, (cos.objectsOfClass(CLASS_ATOMICOBJECT)).size(), new ArrayList<AtomicObject> ());
			}
			else if(a.equals(ACTION_PLACEDOOR))
			{
				if(map[ax][ay] == 1)
					return cos;
				Door newDoor = new Door(ax, ay, "Door " + ax + ", " +  ay);
				cos.addObject(newDoor);
				map[ax][ay] = 1;
				
				cos.checkForWalls(cos, 0, ((List<ObjectInstance>) cos.objectsOfClass(CLASS_ATOMICOBJECT)).size(), new ArrayList<AtomicObject> ());
			}
			return cos;
		}
		
		protected State move(State s, Action a)
		{
			CompObjState cos = (CompObjState)s;
			
			int [][] map = (int[][]) cos.getMap();

			cos.agent.clearWalls();

			int ax = cos.agent.x;
			int ay = cos.agent.y;
			
			if(a.actionName().equals(ACTION_PLACEBLOCK))
			{
				if(map[ax][ay] == 1)
					return cos;
				Block newBlock = new Block(ax, ay, "Block " + ax + ", " +  ay);
				cos.addObject(newBlock);
				map[ax][ay] = 1;
				
				cos.checkForWalls(cos, 0, (cos.objectsOfClass(CLASS_ATOMICOBJECT)).size(), new ArrayList<AtomicObject> ());
			}
			else if(a.actionName().equals(ACTION_PLACEDOOR))
			{
				if(map[ax][ay] == 1)
					return cos;
				Door newDoor = new Door(ax, ay, "Door " + ax + ", " +  ay);
				cos.addObject(newDoor);
				map[ax][ay] = 1;
				
				cos.checkForWalls(cos, 0, ((List<ObjectInstance>) cos.objectsOfClass(CLASS_ATOMICOBJECT)).size(), new ArrayList<AtomicObject> ());
			}
			else if(a.actionName().equals(ACTION_REMOVEOJECT))
			{
				cos.removeObject(ax, ay);
				map[ax][ay] = 0;
				cos.checkForWalls(cos, 0, ((List<ObjectInstance>) cos.objectsOfClass(CLASS_ATOMICOBJECT)).size(), new ArrayList<AtomicObject> ());
			}
			return cos;
		}

		protected int actionInd(String name){
			if(name.equals(ACTION_NORTH)){
				return 0;
			}
			else if(name.equals(ACTION_SOUTH)){
				return 1;
			}
			else if(name.equals(ACTION_EAST)){
				return 2;
			}
			else if(name.equals(ACTION_WEST)){
				return 3;
			}
			else if(name.equals(ACTION_PLACEBLOCK))
			{
				return 4;
			}
			else if(name.equals(ACTION_PLACEDOOR))
			{
				return 5;
			}
			else if(name.equals(ACTION_REMOVEOJECT))
			{
				return 6;
			}
			throw new RuntimeException("Unknown action " + name);
		}
		
		public void makeEmptyMap(){
			this.map = new int[map.length][map[0].length];
			for(int i = 0; i < map.length; i++){
				for(int j = 0; j < map[0].length; j++){
					this.map[i][j] = 0;
				}
			}
		}

	}
	
	public class AreBarriers extends PropositionalFunction
	{		
		public AreBarriers(String name, String[] parameterClasses) {
			super(name, parameterClasses);
		}

		@Override
		public boolean isTrue(OOState s, String... params) {
			ObjectInstance agent = s.object(params[0]);
			ArrayList<AtomicObject> selection = (ArrayList<AtomicObject>) ((CompObjAgent) agent).getSelection();
			for(AtomicObject a:selection)
			{
				if(a.className() != "Atomic Object")
					return false;
			}
			return true;
		}
		
	}
	
	public class isStraight extends PropositionalFunction
	{

		public isStraight(String name, String[] parameterClasses) {
			super(name, parameterClasses);
		}

		@Override
		public boolean isTrue(OOState s, String... params) {
			ObjectInstance agent = s.object(params[0]);
			ArrayList<AtomicObject> selection = (ArrayList<AtomicObject>) agent.get("selection");
			if(selection.size() <= 1)
				return true;
			//double slope = Math.abs(((Double)selection.get(1).get(VAR_X) - (Double)selection.get(0).get(VAR_X))/((Double)selection.get(1).get(VAR_Y) - (Double)selection.get(0).get(VAR_Y)));
			double initialX = (Integer)selection.get(0).get(VAR_X);
			double initialY = (Integer)selection.get(0).get(VAR_Y);
			double dx = (Integer)selection.get(1).get(VAR_X) - initialX;
			double dy = (Integer)selection.get(1).get(VAR_Y) - initialY;
			for(AtomicObject a:selection)
			{
				if((Integer)a.get(VAR_X) != initialX && (Integer)a.get(VAR_Y) != initialY)
				{
					double tempDx = (Integer)a.get(VAR_X)-initialX;
					double tempDy = (Integer)a.get(VAR_Y)-initialY;
					if(dx != 0)
					{
						double slope = Math.abs(dy/dx);
						if(tempDx != 0)
						{
							double compSlope = Math.abs(tempDy/tempDx);
							if(compSlope != slope)
								return false;
						}
						else
							return false;
					}
					else
					{
						if(tempDx != 0)
							return false;
					}
				}
			}
			return true;
		}
		
	}
	
	public class isContiguous extends PropositionalFunction
	{

		public isContiguous(String name, String[] parameterClasses) {
			super(name, parameterClasses);
		}

		@Override
		public boolean isTrue(OOState s, String... params) {
			ObjectInstance agent = s.object(params[0]);
			ArrayList<AtomicObject> selection = (ArrayList<AtomicObject>) agent.get("selection");
			if(selection.size() <= 1)
				return true;
			Collections.sort(selection);
			boolean checkX = (((Integer)selection.get(0).get(VAR_X) + 1) == (Integer)selection.get(1).get(VAR_X));
			for(int i = 0; i < selection.size() - 1; i++)
			{
				if(checkX && ((Integer)selection.get(i).get(VAR_X) + 1) != (Integer)selection.get(i+1).get(VAR_X))
				{
					return false;
				}
				if(!checkX && ((Integer)selection.get(i).get(VAR_Y) + 1) != (Integer)selection.get(i+1).get(VAR_Y))
				{
					return false;
				}
			}
			return true;
		}
	}
	
	public class hasSizeWall extends PropositionalFunction
	{
		int wallSize;

		public hasSizeWall(String name, String[] parameterClasses, int size) {
			super(name, parameterClasses);
			wallSize = size;
		}

		@Override
		public boolean isTrue(OOState s, String... params) {
			ObjectInstance agent = s.object(params[0]);
			List<Wall> walls = (List<Wall>) agent.get("Walls");
			
			for(Wall w: walls)
			{
				if((int) w.get("length") >= wallSize)
					return true;
			}
			
			return false;
		}
		
		
		
	}
	
	public class CompObjSimEnvironment extends SimulatedEnvironment {

		public CompObjSimEnvironment(SADomain domain, State initialState) {
			super(domain, initialState);
		}
		
		public void resetEnvironment() {
			super.resetEnvironment();
			/*for(int i = 0;i < height;i++)
			{
				for(int j = 0;j < width;j++)
				{
					map[i][j] = 0;
				}
			}*/
		}
		
		public FactoredModel getModel()
		{
			return (FactoredModel) model;
		}

	}
	
	
	
	public static void main(String[] args) {
		
		CompObjDomain cod = new CompObjDomain(10, 10);

		String outputPath = "output/";

		SADomain d = cod.generateDomain();


		CompObjState s = new CompObjState(new CompObjAgent(0, 0), cod.map);
		
		int expMode = 0;
		if(args.length > 0){
			if(args[0].equals("v")){
				expMode = 1;
			}
			else if(args[0].equals("t")){
				expMode = 0;
			}
		}

		
				
		if(expMode == 0){
			EnvironmentShell shell = new EnvironmentShell(d, s);
			shell.start();
			
			CompObjSimEnvironment env = cod.new CompObjSimEnvironment(d, s);
			
			StateConditionTest goalCondition = new TFGoalCondition(env.getModel().getTf());
			
			HashableStateFactory hashingFactory = new SimpleHashableStateFactory();

			cod.CompObjBFS(d, goalCondition, hashingFactory, s, outputPath);

			env.resetEnvironment();

			cod.CompObjQLearning(d, hashingFactory, env, outputPath, 300, 0.99, 1, 0.1, 100);

			Visualizer v = CompObjVisualizer.getVisualizer(cod.getMap());
			new EpisodeSequenceVisualizer(v, d, outputPath);
			
		}
		else if(expMode == 1){
			Visualizer v = CompObjVisualizer.getVisualizer(cod.getMap());
			VisualExplorer exp = new VisualExplorer(d, v, s);
			
			exp.addKeyAction("w", ACTION_NORTH, "");
			exp.addKeyAction("s", ACTION_SOUTH, "");
			exp.addKeyAction("a", ACTION_WEST, "");
			exp.addKeyAction("d", ACTION_EAST, "");
			exp.addKeyAction("q", ACTION_PLACEBLOCK, "");
			exp.addKeyAction("e", ACTION_PLACEDOOR, "");
			exp.addKeyAction("r", ACTION_REMOVEOJECT, "");
			
			exp.initGUI();
		}
		
	}

	public void CompObjBFS(SADomain d, StateConditionTest goalCondition, HashableStateFactory hashingFactory, CompObjState s, String outputPath)
	{
		DeterministicPlanner planner = new BFS(d, goalCondition, hashingFactory);
		Policy p = planner.planFromState(s);
		PolicyUtils.rollout(p, s, d.getModel()).write(outputPath + "bfs");
	}

	public void CompObjQLearning(SADomain d, HashableStateFactory hashingFactory, SimulatedEnvironment env, String outputPath, int iterations, double gamma, double qinit, double learningRate, int maxSteps)
	{
		LearningAgent agent = new QLearning(d, gamma, hashingFactory, qinit, learningRate);

		//run learning for 200 episodes
		for(int i = 0; i < iterations; i++){
			Episode e = agent.runLearningEpisode(env, maxSteps);

			e.write(outputPath + "ql_" + i);
			System.out.println(i + ": " + e.maxTimeStep());

			//reset environment for next learning episode
			env.resetEnvironment();
		}
	}

}

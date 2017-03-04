package amdp.house;

import amdp.amdpframework.AMDPPolicyGenerator;
import amdp.amdpframework.GroundedTask;
import amdp.amdpframework.NonPrimitiveTaskNode;
import amdp.amdpframework.TaskNode;
import amdp.house.level2.MakeWallStateMapping;
import amdp.house.level2.MakeWallTF;
import amdp.house.objects.HWall;
import amdp.taxiamdpdomains.testingtools.BoundedRTDPForTests;
import amdp.taxiamdpdomains.testingtools.GreedyReplan;
import burlap.behavior.policy.Policy;
import burlap.behavior.valuefunction.ConstantValueFunction;
import burlap.behavior.valuefunction.QProvider;
import burlap.mdp.core.TerminalFunction;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.FactoredModel;
import burlap.mdp.singleagent.oo.OOSADomain;
import burlap.statehashing.simple.SimpleHashableStateFactory;

public class PolicyGeneratorMakeWall implements AMDPPolicyGenerator {
	
    private OOSADomain domain;
    private MakeWallStateMapping mapping;
    private double discount = 0.99;
    private int depth = 65536;

    public PolicyGeneratorMakeWall(OOSADomain domain){
        this.domain = domain;
        this.mapping = new MakeWallStateMapping();
    }

    @Override
    public Policy generatePolicy(State s, GroundedTask gt) {
    	
        domain = ((NonPrimitiveTaskNode)gt.getT()).domain();
        domain.setModel(new FactoredModel(((FactoredModel)domain.getModel()).getStateModel(),gt.rewardFunction(), gt.terminalFunction()));
//        TaskMakeWall t = (TaskMakeWall) gt.getT();
//        TerminalFunction tf = gt.terminalFunction();
//        MakeWallTF makeWallTF = (MakeWallTF) t.tf;
//        HWall goal = makeWallTF.getGoal();
//        double length = goal.getLength();
        double length = 5;
        int maxSteps = (int) length+2; // needs one for the origin point, needs one since PolicyUtils maxSteps terminates at maxSteps-1
        depth = maxSteps;
        
        BoundedRTDPForTests brtd = new BoundedRTDPForTests(domain, this.discount,
                new SimpleHashableStateFactory(true), // perhaps should be false
                new ConstantValueFunction(0.),
                new ConstantValueFunction(1.0),
                AMDPAssembler.BRTDP_MAX_DIFF,
                -1);
        brtd.setRemainingNumberOfBellmanUpdates(AMDPAssembler.bellmanBudgetL2);
        brtd.setMaxRolloutDepth(depth);
        brtd.toggleDebugPrinting(true);
        AMDPAssembler.brtdpList.add(brtd);
        brtd.planFromState(s);
        return new GreedyReplan(brtd);
    }

    @Override
    public State generateAbstractState(State s) {
    	return mapping.mapState(s);
    }

    @Override
    public QProvider getQProvider(State s, GroundedTask gt) {
        domain = ((NonPrimitiveTaskNode)gt.getT()).domain();
        domain.setModel(new FactoredModel(((FactoredModel)domain.getModel()).getStateModel(),gt.rewardFunction(), gt.terminalFunction()));

        BoundedRTDPForTests brtd = new BoundedRTDPForTests(domain, this.discount,
                new SimpleHashableStateFactory(true), // false?
                new ConstantValueFunction(0.),
                new ConstantValueFunction(1.),
                AMDPAssembler.BRTDP_MAX_DIFF,
                -1);
        brtd.setRemainingNumberOfBellmanUpdates(AMDPAssembler.bellmanBudgetL2);
        brtd.setMaxRolloutDepth(depth);
        brtd.toggleDebugPrinting(true);
        AMDPAssembler.brtdpList.add(brtd);
        brtd.planFromState(s);
        return brtd;
    }

}



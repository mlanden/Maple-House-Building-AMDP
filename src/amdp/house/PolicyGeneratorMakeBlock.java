package amdp.house;

import amdp.amdpframework.AMDPPolicyGenerator;
import amdp.amdpframework.GroundedTask;
import amdp.amdpframework.NonPrimitiveTaskNode;
import amdp.taxiamdpdomains.testingtools.BoundedRTDPForTests;
import amdp.taxiamdpdomains.testingtools.GreedyReplan;
import burlap.behavior.policy.Policy;
import burlap.behavior.valuefunction.ConstantValueFunction;
import burlap.behavior.valuefunction.QProvider;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.FactoredModel;
import burlap.mdp.singleagent.oo.OOSADomain;
import burlap.statehashing.simple.SimpleHashableStateFactory;

public class PolicyGeneratorMakeBlock implements AMDPPolicyGenerator {
	
    private OOSADomain domain;
    private double discount = 0.99;

    public PolicyGeneratorMakeBlock(OOSADomain domain){
        this.domain = domain;
    }

    @Override
    public Policy generatePolicy(State s, GroundedTask gt) {
    	
        domain = ((NonPrimitiveTaskNode)gt.getT()).domain();
        domain.setModel(new FactoredModel(((FactoredModel)domain.getModel()).getStateModel(),gt.rewardFunction(), gt.terminalFunction()));

        BoundedRTDPForTests brtd = new BoundedRTDPForTests(domain, this.discount,
                new SimpleHashableStateFactory(true), // perhaps should be false
                new ConstantValueFunction(0.),
                new ConstantValueFunction(1.0),
                0.01,
                -1);
        brtd.setRemainingNumberOfBellmanUpdates(AMDPAssembler.bellmanBudgetL0);
        brtd.setMaxRolloutDepth(100);
        brtd.toggleDebugPrinting(false);
        AMDPAssembler.brtdpList.add(brtd);
        brtd.planFromState(s);
        return new GreedyReplan(brtd);
    }

    @Override
    public State generateAbstractState(State s) {
        return null;
    }

    @Override
    public QProvider getQProvider(State s, GroundedTask gt) {
        domain = ((NonPrimitiveTaskNode)gt.getT()).domain();
        domain.setModel(new FactoredModel(((FactoredModel)domain.getModel()).getStateModel(),gt.rewardFunction(), gt.terminalFunction()));

        BoundedRTDPForTests brtd = new BoundedRTDPForTests(domain, this.discount,
                new SimpleHashableStateFactory(false),
                new ConstantValueFunction(0.),
                new ConstantValueFunction(1.),
                0.01,
                -1);
        brtd.setRemainingNumberOfBellmanUpdates(AMDPAssembler.bellmanBudgetL0);
        brtd.setMaxRolloutDepth(100);
        brtd.toggleDebugPrinting(false);
        AMDPAssembler.brtdpList.add(brtd);
        brtd.planFromState(s);
        return brtd;
    }

}
package amdp.house;

import amdp.amdpframework.AMDPPolicyGenerator;
import amdp.amdpframework.GroundedTask;
import amdp.amdpframework.NonPrimitiveTaskNode;
import amdp.house.level1.MakeBlockStateMapping;
import amdp.taxiamdpdomains.testingtools.BoundedRTDPForTests;
import amdp.taxiamdpdomains.testingtools.GreedyReplan;
import burlap.behavior.policy.Policy;
import burlap.behavior.valuefunction.ConstantValueFunction;
import burlap.behavior.valuefunction.QProvider;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.FactoredModel;
import burlap.mdp.singleagent.oo.OOSADomain;
import burlap.statehashing.simple.SimpleHashableStateFactory;

public class PolicyGeneratorHouseBase implements AMDPPolicyGenerator {

    private OOSADomain domain;
    private double discount = 0.99;
    
    public PolicyGeneratorHouseBase(OOSADomain domain) {
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
                AMDPAssembler.BRTDP_MAX_DIFF,
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
		// no abstraction at base level
		return s;
//		return null;
//		throw new RuntimeException("Error: should not call abstractions at base level");
	}

	@Override
	public QProvider getQProvider(State s, GroundedTask gt) {
        domain = ((NonPrimitiveTaskNode)gt.getT()).domain();
        domain.setModel(new FactoredModel(((FactoredModel)domain.getModel()).getStateModel(),gt.rewardFunction(), gt.terminalFunction()));

        BoundedRTDPForTests brtd = new BoundedRTDPForTests(domain, this.discount,
                new SimpleHashableStateFactory(true),
                new ConstantValueFunction(0.),
                new ConstantValueFunction(1.),
                AMDPAssembler.BRTDP_MAX_DIFF,
                -1);
        brtd.setRemainingNumberOfBellmanUpdates(AMDPAssembler.bellmanBudgetL0);
        brtd.setMaxRolloutDepth(100);
        brtd.toggleDebugPrinting(false);
        AMDPAssembler.brtdpList.add(brtd);
        brtd.planFromState(s);
        return brtd;
	}

}

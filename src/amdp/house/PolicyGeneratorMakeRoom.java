package amdp.house;

import amdp.amdpframework.AMDPPolicyGenerator;
import amdp.amdpframework.GroundedTask;
import amdp.amdpframework.NonPrimitiveTaskNode;
import amdp.house.level3.MakeRoomStateMapping;
import amdp.taxiamdpdomains.testingtools.BoundedRTDPForTests;
import amdp.taxiamdpdomains.testingtools.GreedyReplan;
import burlap.behavior.policy.Policy;
import burlap.behavior.valuefunction.ConstantValueFunction;
import burlap.behavior.valuefunction.QProvider;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.model.FactoredModel;
import burlap.mdp.singleagent.oo.OOSADomain;
import burlap.statehashing.simple.SimpleHashableStateFactory;

public class PolicyGeneratorMakeRoom implements AMDPPolicyGenerator {
	
    private OOSADomain domain;
    private MakeRoomStateMapping mapping;
    private double discount = 0.99;
    private int depth = 65536;

    public PolicyGeneratorMakeRoom(OOSADomain domain){
        this.domain = domain;
        this.mapping = new MakeRoomStateMapping();
    }
    
    @Override
    public Policy generatePolicy(State s, GroundedTask gt) {

        domain = ((NonPrimitiveTaskNode)gt.getT()).domain();
        FactoredModel model = new FactoredModel(
    		((FactoredModel)domain.getModel()) .getStateModel(),
    		gt.rewardFunction(),
    		gt.terminalFunction());
        domain.setModel(model);

        SimpleHashableStateFactory shf = new SimpleHashableStateFactory(true);
        BoundedRTDPForTests brtdp = new BoundedRTDPForTests(domain, discount, shf,
            new ConstantValueFunction(0.),
            new ConstantValueFunction(1.),
            AMDPAssembler.BRTDP_MAX_DIFF,
            -1);

        brtdp.setRemainingNumberOfBellmanUpdates(AMDPAssembler.bellmanBudgetL3);
        brtdp.setMaxRolloutDepth(depth);
        brtdp.toggleDebugPrinting(true);

        Policy p = brtdp.planFromState(s);
        AMDPAssembler.brtdpList.add(brtdp);
        return new GreedyReplan(brtdp);
    }

    @Override
    public State generateAbstractState(State s) {
        return mapping.mapState(s);
    }

    @Override
    public QProvider getQProvider(State s, GroundedTask gt) {

        domain = ((NonPrimitiveTaskNode)gt.getT()).domain();
        domain.setModel(new FactoredModel(((FactoredModel)domain.getModel()).getStateModel(),gt.rewardFunction(), gt.terminalFunction()));

        SimpleHashableStateFactory shf = new SimpleHashableStateFactory(true); // perhaps should be false
        BoundedRTDPForTests brtdp = new BoundedRTDPForTests(domain, discount, shf,
                new ConstantValueFunction(0.),
                new ConstantValueFunction(1.),
                AMDPAssembler.BRTDP_MAX_DIFF,
                -1);

        brtdp.setRemainingNumberOfBellmanUpdates(AMDPAssembler.bellmanBudgetL3);
        brtdp.setMaxRolloutDepth(depth);
        brtdp.toggleDebugPrinting(true);

        Policy p = brtdp.planFromState(s);
        AMDPAssembler.brtdpList.add(brtdp);
        return brtdp;
    }


}

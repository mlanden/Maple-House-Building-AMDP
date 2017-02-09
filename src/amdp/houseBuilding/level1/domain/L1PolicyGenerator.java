package amdp.houseBuilding.level1.domain;

import amdp.amdpframework.AMDPPolicyGenerator;
import amdp.amdpframework.GroundedTask;
import amdp.amdpframework.NonPrimitiveTaskNode;
import burlap.behavior.policy.Policy;
import burlap.behavior.valuefunction.QProvider;
import burlap.mdp.core.state.State;
import burlap.mdp.singleagent.oo.OOSADomain;

public class L1PolicyGenerator implements AMDPPolicyGenerator{

	private OOSADomain l1;
	private double discount;
	L1PropositionalFunctions spf = new L1PropositionalFunctions();
	
	public L1PolicyGenerator(OOSADomain dom, double disc){
		this.discount = disc;
	}
	public Policy generatePolicy(State s, GroundedTask groundedTask) {
		l1 = ((NonPrimitiveTaskNode)groundedTask.getT()).domain();
		
	}

	public State generateAbstractState(State s) {
	
	}

	public QProvider getQProvider(State s, GroundedTask groundedTask) {
	
	}

}

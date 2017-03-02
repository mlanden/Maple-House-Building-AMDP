package amdp.house.objects;

import burlap.mdp.core.oo.state.ObjectInstance;

public class HAgent extends HPoint {

	public static final String CLASS_AGENT = "agent";
	
	public HAgent(Integer x, Integer y) {
		this(CLASS_AGENT+"0", x, y);
	}
	
	public HAgent(String name, Integer x, Integer y) {
		super(name, x, y, false);
	}
	
	@Override
	public String className() {
		return CLASS_AGENT;
	}

	@Override
	public HAgent copyWithName(String objectName) {
		return new HAgent(
				objectName,
				(Integer)get(HAgent.ATT_X),
				(Integer) get(HAgent.ATT_Y)
		);
	}
	
	@Override
	public HAgent copy() {
		return new HAgent(
				name,
				(Integer)get(HAgent.ATT_X),
				(Integer) get(HAgent.ATT_Y)
		);
	}
	
}

package amdp.houseBuilding.level1.domain;

import burlap.mdp.auxiliary.DomainGenerator;
import burlap.mdp.core.oo.OODomain;

public class L1DomainGenerator implements DomainGenerator{

	//object classes
	public static final String CLASS_AGENT = 	"L0_Agent";
	public static final String CLASS_WALL = 	"wall";
	
	//wall properties
	public static final String DOORS = 			"doors";
	public static final String START_X = 		"start x";
	public static final String START_Y =		"start y";
	public static final String END_X = 			"end x";
	public static final String END_Y = 			"end y";
	public static final String LENGTH = 		"length";
	
	public OODomain generateDomain() {
		
	}

}

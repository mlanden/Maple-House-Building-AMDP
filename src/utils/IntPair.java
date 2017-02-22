package utils;

/**
 * A pair class for two ints.
 * @author James MacGlashan
 *
 */
public class IntPair{
	public int x;
	public int y;
	public IntPair(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	@Override
	public int hashCode(){
		return this.x + 31*this.y;
	}
	
	@Override
	public boolean equals(Object other){
		if(!(other instanceof IntPair)){
			return false;
		}
		
		IntPair o = (IntPair)other;
		return this.x == o.x && this.y == o.y;
	}
}
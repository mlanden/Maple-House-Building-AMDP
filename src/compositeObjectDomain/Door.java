package compositeObjectDomain;

public class Door extends AtomicObject{
	
	public Door()
	{
		super();
		//className = "Door";
		type = 1;
	}
	
	public Door(int x, int y)
	{
		super(x, y);
		//className = "Door";
		type = 1;
	}
	
	public Door(int x, int y, String name)
	{
		super(x, y, name);
		//className = "Door";
		type = 1;
	}

}

package org.jempe.counter;


public class TapCounter 
{

	// variable that store the count
	public int tap_count = 0;
	
	public String getCount()
	{
		String current_count = String.valueOf(tap_count);
		
		return current_count;
	}
	
	public void changeCount(int newValue)
	{
		tap_count = newValue;
	}
	
	public void increaseCount()
	{
		int newValue = tap_count + 1;
		
		changeCount(newValue);
	}
	
	public void decreaseCount()
	{
		int newValue = tap_count;
		
		if(tap_count > 0)
		{
			newValue = tap_count - 1;
		}
		
		changeCount(newValue);
	}
	
	public void resetCount()
	{
		changeCount(0);
	}
}

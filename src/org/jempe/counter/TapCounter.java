package org.jempe.counter;


public class TapCounter 
{

	// variable that store the count
	public int tap_count = 0;
	
	public String getCount()
	{
		String current_count = String.valueOf(tap_count);
		
		if(current_count.length() < 6)
		{
			String zeroes = "";
			for(int i = 0; i < (6 - current_count.length()); i++)
			{
				zeroes += "0";
			}
			
			current_count = zeroes + current_count;
		}
		
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

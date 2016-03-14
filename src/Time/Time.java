package Time;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Time
{
	private static double delta;
	
	public static final long SECOND = 1000000000L;
	
	public static long getTime()
	{
		return System.nanoTime();
	}
	
	public static double getDelta()
	{
		return delta;
	}
	
	public static void setDelta(double delta)
	{
		Time.delta = delta;
	}
	
	public static String getTimeString()
	{
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss,SSS");
		Date date = new Date();
		
		return dateFormat.format(date);
	}
}

package Debug;
import Time.Time;

public class Debug
{
	public static void log(Object message)
	{
		System.out.println("[" + Time.getTimeString() + "] " + message);
	}
	
	public static void error(Object message)
	{
		System.err.println("[" + Time.getTimeString() + "] " + message);
	}
}

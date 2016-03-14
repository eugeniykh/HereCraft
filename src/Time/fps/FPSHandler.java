package Time.fps;
import Debug.Debug;
import Time.Time;
import Window.Window;

public class FPSHandler
{
	private static int maxFps = 60;
	private static int fps = 0;
	private static int frames = 0;
	private static long framesTimer = 0;
	private static long lastTime = Time.getTime();
	private static float unprocessedTime = 0;
	private static float frameTime = (float)(1.0f / 60);
	private static boolean render = false;
	
	public static void update()
	{
		render = false;
		
		long startTime = System.nanoTime();
		long passedTime = startTime - lastTime;
		lastTime = startTime;
				
		unprocessedTime += (float)(passedTime / (float)Time.SECOND);
		framesTimer += passedTime;
		
		if(framesTimer >= Time.SECOND)
		{
			fps = (int)Math.floor(frames / ((float)framesTimer / Time.SECOND) + 0.5);
			frames = 0;
			framesTimer = 0;
		}	
	}
	
	public static boolean process()
	{
		if(unprocessedTime > frameTime)
		{
			render = true;
			
			unprocessedTime -= frameTime;
			Time.setDelta(frameTime);

			return true;
		}
		return false;
	}
	
	public static boolean render()
	{
		if(render)
		{
			frames++;
			return true;
		}
		return false;
	}
	
	public static void setFPS(int fps)
	{
		FPSHandler.maxFps = fps;
		frameTime = (float)(1.0f / maxFps);
	}
	
	public static int getFPS()
	{
		return fps;
	}
	
	public static int getMaxFPS()
	{
		return maxFps;
	}
}

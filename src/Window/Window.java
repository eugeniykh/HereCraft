package Window;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;

import Debug.Debug;
import Time.fps.FPSHandler;
import Vector.Vector2i;

public class Window
{
	private static boolean isCreated = false;
	
	public static void create()
	{
		try
		{
			Display.create();
			Keyboard.create();
			Mouse.create();
			
			isCreated = true;
		}
		catch (LWJGLException e)
		{
			e.printStackTrace();
		}
	}

	public static void destroy()
	{
		Display.destroy();
		Keyboard.destroy();
		Mouse.destroy();
		
		isCreated = false;
	}

	public static void render()
	{
		Display.update();
	}

	public static void setVSync(boolean enabled)
	{
		Display.setVSyncEnabled(enabled);
	}
	
	public static void setTitle(String title)
	{
		Display.setTitle(title);
	}

	public static void setFPS(int fps)
	{
		FPSHandler.setFPS(fps);
	}
	
	public static void setResolution(int width, int height, boolean fullscreen)
	{
		if (fullscreen)
		{
			try
			{
				DisplayMode mode = null;
				DisplayMode[] modes = Display.getAvailableDisplayModes();
				for(int i = 0; i < modes.length; i++)
				{
					if(modes[i].isFullscreenCapable() && modes[i].getWidth() == width && modes[i].getHeight() == height)
					{
						mode = modes[i];
						break;
					}
				}

				if(mode != null)
					Display.setDisplayModeAndFullscreen(mode);
				else
				{
					Debug.error("Resolution: " + width + " x " + height + ", is not supported in fullscreen!");
					Debug.error("Following resolutions are supported:");
					
					Vector2i[] resoluctions = getAvailableResolutions();
					for(int i = 0; i < resoluctions.length; i++)
					{
						Debug.error(" - " + resoluctions[i].getX() + " x " + resoluctions[i].getY());
					}
					
					System.exit(1);
				}
			}
			catch (LWJGLException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			try
			{
				Display.setDisplayMode(new DisplayMode(width, height));
			}
			catch (LWJGLException e)
			{
				e.printStackTrace();
			}
		}
	}

	public static boolean isCloseRequested()
	{
		return Display.isCloseRequested();
	}

	public static int getWidth()
	{
		return Display.getDisplayMode().getWidth();
	}

	public static int getHeight()
	{
		return Display.getDisplayMode().getHeight();
	}
	
	public static int getFPS()
	{
		return FPSHandler.getFPS();
	}
	
	public static int getMaxFPS()
	{
		return FPSHandler.getMaxFPS();
	}

	public static String getTitle()
	{
		return Display.getTitle();
	}
	
	public static boolean isCreated()
	{
		return isCreated;
	}
	
	public static Vector2i[] getAvailableResolutions()
	{
		try
		{
			List<Vector2i> resolutions = new ArrayList<Vector2i>();
			DisplayMode[] modes = Display.getAvailableDisplayModes();
			
			for(int i = 0; i < modes.length; i++)
			{
				if(modes[i].isFullscreenCapable())
				{
					Vector2i resolution = new Vector2i(modes[i].getWidth(), modes[i].getHeight());
					
					boolean add = true;
					for(int j = 0; j < resolutions.size(); j++)
					{
						if(resolutions.get(j).getX() == resolution.getX() && resolutions.get(j).getY() == resolution.getY())
							add = false;
					}
					
					if(add)
						resolutions.add(resolution);
				}
			}
			
			Collections.sort(resolutions, new Comparator<Vector2i>() {
			    public int compare(Vector2i v1, Vector2i v2)  {
			        return (int) (v1.getX() - v2.getX());
			    }
			});
			return (Vector2i[]) resolutions.toArray(new Vector2i[resolutions.size()]);
		}
		catch (LWJGLException e)
		{
			e.printStackTrace();
		}
		
		return null;
	}
}

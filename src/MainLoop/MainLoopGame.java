package MainLoop;

import java.awt.Font;
import java.awt.Point;
import java.nio.FloatBuffer;
import java.util.ArrayList;

import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import MainLoop.World.LogicShapes.Bullet;
import MainLoop.World.LogicShapes.Monster;
import MainLoop.World.LogicShapes.User;
import MainLoop.World.OBJLoader.BufferTools;
import Debug.Debug;
import MainLoop.KeyEventsClick.KeyEventsHandler;
import MainLoop.World.World;
import MainLoop.World.Font.FontMineGame;
import MainLoop.World.Shapes.SideBrick;
import MineGame.MineGame;
import Shader.ShaderProgram;
import Time.fps.FPSHandler;
import Utils.Utils;
import Vector.Point3D;
import Vector.Point3DVectored;
import Window.Window;

import static org.lwjgl.opengl.GL11.*;

public class MainLoopGame {

	/** Desired frame time */
	private final int FRAMERATE = 30;

	/** Exit the game */
	public boolean finished;

	public World world;

	/** A rotating square! */
	public float angle;

	public FirstPersonCameraController camera;

	public float dx = 0.0f;
	public float dy = 0.0f;
	public float dt = 0.0f; // length of frame
	public float lastTime = 0.0f; // when the last frame was
	public float time = 0.0f;

	public float mouseSensitivity = 0.1f;
	public float movementSpeed = 15.0f; // move 10 units per second
	
	public KeyEventsHandler keyEventsHandler;
	
	public ShaderProgram shader;

	/**
	 * Runs the game (the "main loop")
	 */
	public void run() {
		while (!finished) {
			
			if (User.alive()) {
				keyEventsHandler.MouseHandler(this);
			}
			
			FPSHandler.process();
			FPSHandler.render();
			FPSHandler.update();
			
			// Always call Window.update(), all the time
			Display.update();

			if (Display.isCloseRequested()) {
				// Check for O/S close requests
				finished = true;
			} else if (Display.isActive()) {
				// The window is in the foreground, so we should play the game
				logic();
				render();
				Display.sync(FRAMERATE);
			} else {
				// The window is not in the foreground, so we can allow other
				// stuff to run and
				// infrequently update
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
				}
				logic();
				if (Display.isVisible() || Display.isDirty()) {
					// Only bother rendering if the window is visible or dirty
					render();
				}
			}
		}
	}

	/**
	 * Do all calculations, handle input, etc.
	 */
	private void logic() {

		// Example input handler: we'll check for the ESC key and finish the
		// game instantly when it's pressed
		if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
			finished = true;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_R)) {
			MineGame.createGame(this);
		}
		
		if (User.alive()) {
			keyEventsHandler.LoopEventClick(this);
		}
		
		glColor3f((float)User.crateFound/(float)User.crateFoundMax, (float)User.crateFound/(float)User.crateFoundMax, (float)User.crateFound/(float)User.crateFoundMax);
		
		world.pointsController(this);

   	 	world.needToGenerateNewWorld(this);
		
		angle += 5.0f;
		angle %= 360;
	}

	/**
	 * Render the current frame
	 */
	private void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		
		// set the modelview matrix back to the identity
		GL11.glLoadIdentity();
		
		glPushMatrix();
		
		camera.lookThrough(world);
		
		//world.drawStaff(this);
		
		world.drawWorld(this);
		
		glPopMatrix();
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, Window.getWidth(), Window.getHeight(), 0, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);

        glColor3f(0, 0, 0);
        
        glPushMatrix();

        //int monsterSize = world.monsters.size();
        
        //FontMineGame.drawString("Monsters: "+monsterSize, (float) (Window.getWidth() * 0.85f), 10);

        //String health = String.valueOf(Math.round(User.health));
        
        //FontMineGame.drawString("Health: "+health, 10, 10);
        
        String crateFound = String.valueOf(User.crateFound);
        
        String crateFoundMax = String.valueOf(User.crateFoundMax);
        
        FontMineGame.drawString(crateFound+" boxes from "+crateFoundMax, 10, 10);
        
        String ammo = String.valueOf(User.ammo);
        
        FontMineGame.drawString("Portal " + ammo + " found", 10, 20);
        
        /*if (!User.alive()) {
        	FontMineGame.drawString("You lose", (float) (Window.getWidth() * 0.45f), (float) (Window.getHeight() * 0.25f));
        	FontMineGame.drawString("Press R to restart", (float) (Window.getWidth() * 0.40f), (float) (Window.getHeight() * 0.27f));
        }*/
        
        String timePlaying = ((world.timer > 60) ? String.valueOf(world.timer / 60)+" minutes " + String.valueOf(world.timer % 60) + " seconds" : String.valueOf(world.timer) + " seconds");
        
        if (User.crateFound == User.crateFoundMax && world.horizontalGenerateTemp.contains(new Point(0, world.NOT_SEE_WORLD))) {
        	FontMineGame.drawString("Youre score is "+timePlaying, (float) (Window.getWidth() * 0.35f), (float) (Window.getHeight() * 0.25f));
        	FontMineGame.drawString("Press R to restart", (float) (Window.getWidth() * 0.43f), (float) (Window.getHeight() * 0.27f));
        	world.time.cancel();
        	User.makeDead(this);
        } else {
        	FontMineGame.drawString(timePlaying, 10, 292);
        }
        
        //String fps = String.valueOf(FPSHandler.getFPS());
        
        //FontMineGame.drawString("FPS: "+fps, Window.getWidth() * 0.85f, 10);
        
        glPopMatrix();
        
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GLU.gluPerspective(45.0f, (float) Window.getWidth() / (float) Window.getHeight(), 0.1f, 50000.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
}

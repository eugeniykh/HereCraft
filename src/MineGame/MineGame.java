package MineGame;

/*
 * Copyright (c) 2002-2010 LWJGL Project
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 *
 * * Neither the name of 'LWJGL' nor the names of
 *   its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED
 * TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import org.lwjgl.BufferUtils;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import Texture.TextureCRUD;
import MainLoop.FirstPersonCameraController;
import MainLoop.MainLoopGame;
import MainLoop.KeyEventsClick.KeyEventsHandler;
import MainLoop.World.World;
import MainLoop.World.LogicShapes.User;
import MainLoop.World.OBJLoader.BufferTools;
import MainLoop.World.OBJLoader.Model;
import MainLoop.World.OBJLoader.ModelOBJ;
import MainLoop.World.OBJLoader.OBJLoader;
import Shader.ShaderProgram;
import Window.Window;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL32.GL_DEPTH_CLAMP;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 *
 * This is a <em>very basic</em> skeleton to init a game and run it.
 *
 * @author $Author: spasi $
 * @version $Revision: 3418 $ $Id: Game.java 3418 2010-09-28 21:11:35Z spasi $
 */
public class MineGame {

	/** Game title */
	public static final String GAME_TITLE = "HereCraft";

	/**
	 * No constructor needed - this class is static
	 */
	private MineGame() {
	}

	/**
	 * Application init
	 * 
	 * @param args
	 *            Commandline args
	 */
	public static void main(String[] args) {
		// hide the mouse
		try {
			
			// create Main Loop Game
			MainLoopGame mainLoop = new MainLoopGame();
			
			init(mainLoop);
			
			mainLoop.run();
			
		} catch (Exception e) {
			e.printStackTrace(System.err);
			Sys.alert(GAME_TITLE, "An error occured and the game will exit.");
		} finally {
			cleanup();
		}

		System.exit(0);
	}

	/**
	 * Initialise the game
	 * 
	 * @throws Exception
	 *             if init fails
	 */
	private static void init(MainLoopGame mainLoop) throws Exception {

		Window.setTitle(GAME_TITLE);
		Window.setResolution(1920, 1080, true);
		Window.setVSync(false);
		Window.setFPS(5000);
		Window.create();

		// Start up the sound system
		AL.create();

		// TODO: Load in your textures etc here
		createGame(mainLoop);
		
		openGL();
		
		Mouse.setGrabbed(true);
	}
	
	public static void createGame(MainLoopGame mainLoop) {
		loadWorld(mainLoop);
		loadTextures(mainLoop);
		loadModels(mainLoop);
		setUpCameraPosition(mainLoop);
		setUpShaders(mainLoop);
		User.init(mainLoop);
		setUpKeyClick(mainLoop);
	}
	
	private static void setUpShaders(MainLoopGame mainLoop) {
		mainLoop.shader = new ShaderProgram();
		 
		// do the heavy lifting of loading, compiling and linking
		// the two shaders into a usable shader program
		mainLoop.shader.init("res/shaders/simple.vertex", "res/shaders/simple.fragment");		
	}

	private static void loadTextures(MainLoopGame mainLoop) {
		mainLoop.world.grass = TextureCRUD.loadTexture("upper_grass.jpg", "JPG");
		mainLoop.world.sideGrass = TextureCRUD.loadTexture("grass.jpg", "JPG");
		mainLoop.world.grassFonNewTexture = TextureCRUD.loadTexture("upper_grass_fon_new.jpg", "JPG");
		mainLoop.world.ground = TextureCRUD.loadTexture("ground.jpg", "JPG");
		mainLoop.world.modelTexture = TextureCRUD.loadTexture("wraith.jpg", "JPG");
		mainLoop.world.modelTexture_2 = TextureCRUD.loadTexture("wraith_d.jpg", "JPG");
		mainLoop.world.staffTexture = TextureCRUD.loadTexture("staff.jpg", "JPG");
		mainLoop.world.palmTexture = TextureCRUD.loadTexture("palm.jpg", "JPG");
		mainLoop.world.frostIceTexture = TextureCRUD.loadTexture("frost_ice.png", "PNG");
		mainLoop.world.redIceTexture = TextureCRUD.loadTexture("red_ice.png", "PNG");
		mainLoop.world.skyTexture = TextureCRUD.loadTexture("sky.jpg", "JPG");
		mainLoop.world.bushTexture = TextureCRUD.loadTexture("corn.jpg", "JPG");
		mainLoop.world.rockTexture = TextureCRUD.loadTexture("rock.jpg", "JPG");
		mainLoop.world.flaresTexture = TextureCRUD.loadTexture("flares.jpg", "JPG");
		mainLoop.world.goblinTexture = TextureCRUD.loadTexture("goblin.jpg", "JPG");
		mainLoop.world.crateTexture = TextureCRUD.loadTexture("crate.png", "PNG");
		mainLoop.world.bombTexture = TextureCRUD.loadTexture("bomb.jpg", "JPG"); 
	}

	private static void loadWorld(MainLoopGame mainLoop) {
		mainLoop.world = new World();
		
		mainLoop.world.generateWorld();
	}
	
	private static void setUpCameraPosition(MainLoopGame mainLoop) {
		mainLoop.camera = new FirstPersonCameraController(0, 0, 0);
	}
	
	private static void setUpKeyClick(MainLoopGame mainLoop) {
		mainLoop.keyEventsHandler = new KeyEventsHandler();
	}
	
	private static void openGL() {
		
		// Put the window into orthographic projection mode with 1:1 pixel
		// ratio.
		// We haven't used GLU here to do this to avoid an unnecessary
		// dependency.
		//GL11.glDisable(GL11.GL_DITHER);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glEnable(GL11.GL_TEXTURE_2D); // Enable Texture Mapping
		GL11.glClearColor(0f, 0f, 0f, 1.0f); // Black Background
		GL11.glClearDepth(1.0f); // Depth Buffer Setup
		// Really Nice Perspective Calculations
		GL11.glDepthFunc(GL11.GL_LEQUAL); 
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST);

		GL11.glMatrixMode(GL11.GL_PROJECTION); // Select The Projection Matrix
		GL11.glLoadIdentity(); // Reset The Projection Matrix

		GL11.glEnable(GL11.GL_DEPTH_TEST);
        
		// Calculate The Aspect Ratio Of The Window
		GLU.gluPerspective(60.0f, (float) Window.getWidth() / (float) Window.getHeight(), 0.2f, 50000.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW); // Select The Modelview Matrix
		
		/*glEnable(GL_FOG); {
			FloatBuffer fogColor = BufferUtils.createFloatBuffer(4);
			fogColor.put(0.2f).put(0.0f).put(0.0f).put(1.0f).flip();

			glFogi(GL_FOG_MODE, GL_EXP);
			glFog(GL_FOG_COLOR, fogColor);
			glFogf(GL_FOG_DENSITY, 0.002f);
			glHint(GL_FOG_HINT, GL_DONT_CARE);
			glFogf(GL_FOG_START, 135000.0f);
			glFogf(GL_FOG_END, 137000.0f);
		}*/
	}
	
	private static void loadModel(MainLoopGame mainLoop, ModelOBJ modelOBJ, String src) {
		 try {
			 	modelOBJ.model = OBJLoader.loadTexturedModel(new File("res/models/"+src));
			 	modelOBJ.model.enableStates();
			 	modelOBJ.modelOBJ = OBJLoader.createTexturedDisplayList(modelOBJ.model);
	        } catch (IOException e) {
	            e.printStackTrace();
	            cleanup();
	        }
    }
	
	
	private static void loadModels(MainLoopGame mainLoop) {
		loadModel(mainLoop, mainLoop.world.model, "Wraith.obj");
		loadModel(mainLoop, mainLoop.world.staff, "Nightwing_Staff.obj");
		loadModel(mainLoop, mainLoop.world.bullet, "Killer_Frost_Ice_Block.obj");
		loadModel(mainLoop, mainLoop.world.palmModel, "MY_PALM.obj");
		loadModel(mainLoop, mainLoop.world.bushModel, "corn.obj");
		loadModel(mainLoop, mainLoop.world.rockModel, "Stones.obj");
		loadModel(mainLoop, mainLoop.world.flaresModel, "Flares.obj");
		loadModel(mainLoop, mainLoop.world.goblin, "Mega_Mech.obj");
		loadModel(mainLoop, mainLoop.world.bombModel, "Bomb.obj");
	}
	
	/**
	 * Do any game-specific cleanup
	 */
	private static void cleanup() {
		// TODO: save anything you want to disk here

		// Stop the sound
		AL.destroy();

		// Close the window
		Display.destroy();
	}

}
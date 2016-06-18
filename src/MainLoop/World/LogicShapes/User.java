package MainLoop.World.LogicShapes;

import MainLoop.MainLoopGame;
import MineGame.MineGame;

public class User {
	
	public static boolean dead = true;
	
	public static int ammoMax = 0;
	
	public static int ammo = ammoMax;
	
	public static int crateFound = 0;
	
	public static int crateFoundMax;
	
	public static void init(MainLoopGame mainLoop) {
		ammo = ammoMax;
		dead = false;
		mainLoop.camera.yaw = 0;
		mainLoop.camera.pitch = 0;
		crateFound = 0;
		crateFoundMax = 0;
	}
	
	public static boolean alive() {
		return dead == false;
	}
	
	public static void makeDead(MainLoopGame mainLoop) {
		dead = true;
	}
	
	public static void removeAmmo() {
		ammo--;
	}
	
	public static void addAmmo(int flares) {
		ammo += flares;
	}
}

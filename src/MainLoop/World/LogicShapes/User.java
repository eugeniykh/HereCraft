package MainLoop.World.LogicShapes;

import MainLoop.MainLoopGame;
import MineGame.MineGame;

public class User {
	
	public static float healthMax = 100.0f;
	
	public static float health = 100.0f;
	
	public static boolean dead = true;
	
	public static int ammoMax = 30;
	
	public static int ammo = ammoMax;
	
	public static void init(MainLoopGame mainLoop) {
		health = 100f;
		ammo = ammoMax;
		dead = false;
		mainLoop.camera.yaw = 0;
		mainLoop.camera.pitch = 0;
	}
	
	public static void healthRegeneration() {
		if (health < healthMax) {
			health += 0.1;
		}
	}
	
	public static boolean alive() {
		return health > 0 && dead == false;
	}
	
	public static void makeDead(MainLoopGame mainLoop) {
		if (!alive()) {
			health = 0;
			dead = true;
		}
	}
	
	public static void removeAmmo() {
		ammo--;
	}
	
	public static void addAmmo(int flares) {
		ammo += flares;
	}
}

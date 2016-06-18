package MainLoop.KeyEventsClick;

import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import Debug.Debug;
import MainLoop.World.LogicShapes.Bullet;
import MainLoop.World.LogicShapes.User;
import MainLoop.FirstPersonCameraController;
import MainLoop.MainLoopGame;

public class KeyEventsHandler {
	
	public void LoopEventClick(MainLoopGame mainLoop) {
		
		if (!mainLoop.camera.up && mainLoop.camera.upComplete) {
			if (Keyboard.isKeyDown(Keyboard.KEY_W)) // move forward
			{
				mainLoop.camera.walkForward(mainLoop.movementSpeed, mainLoop);
				mainLoop.camera.lastDirectUp = 1;
				mainLoop.camera.lastAngleUp = 0;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_S)) // move backwards
			{
				mainLoop.camera.walkBackwards(mainLoop.movementSpeed, mainLoop);
				mainLoop.camera.lastDirectUp = -1;
				mainLoop.camera.lastAngleUp = 0;				
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_A)) // strafe left
			{
				mainLoop.camera.strafeLeft(mainLoop.movementSpeed, mainLoop);
				mainLoop.camera.lastDirectUp = 1;
				mainLoop.camera.lastAngleUp = -90;
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_D)) // strafe right
			{
				mainLoop.camera.strafeRight(mainLoop.movementSpeed, mainLoop);
				mainLoop.camera.lastDirectUp = 1;
				mainLoop.camera.lastAngleUp = 90;
			}
			
			if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) // strafe right
			{
				if (mainLoop.camera.gravityAcceleration==0) {
					mainLoop.camera.up = true;
					mainLoop.camera.gravityAcceleration = -4f;
					mainLoop.camera.yawUp = mainLoop.camera.yaw;
				}
			}
		} else {
			mainLoop.camera.walk(1.5f * mainLoop.movementSpeed, mainLoop.camera.lastDirectUp, mainLoop.camera.lastAngleUp, mainLoop);
		}
		
		if (Mouse.isButtonDown(0)) // left mouse click 
		{
			if (User.ammo > 0 && mainLoop.camera.gravityAcceleration==0) {
				mainLoop.camera.up = true;
				mainLoop.camera.gravityAcceleration = -10f;
				mainLoop.camera.yawUp = mainLoop.camera.yaw;
				User.removeAmmo();
			}
		}
	}
	
	public void MouseHandler(MainLoopGame mainLoop) {
		mainLoop.time = Sys.getTime();
		mainLoop.dt = (mainLoop.time - mainLoop.lastTime) / 1000.0f;
		mainLoop.lastTime = mainLoop.time;

		// distance in mouse movement from the last getDX() call.
		mainLoop.dx = Mouse.getDX();
		// distance in mouse movement from the last getDY() call.
		mainLoop.dy = Mouse.getDY();

		// controll camera yaw from x movement fromt the mouse
		mainLoop.camera.yaw(mainLoop.dx * mainLoop.mouseSensitivity);
		// controll camera pitch from y movement fromt the mouse
		mainLoop.camera.pitch(mainLoop.dy * mainLoop.mouseSensitivity);
	}
}

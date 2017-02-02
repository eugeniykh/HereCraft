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
	public float movementSpeed = 25.0f; // move 10 units per second
	
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

		bulletController(this);
		
		monstersController(this);
		
		world.crateController(this);
		
		world.bombController(this);

   	 	world.needToGenerateNewWorld(this);
		
		// TODO: all your game logic goes here.
		angle += 5.0f;
		angle %= 360;
	}
	
	private final int MONSTER_SEE_ME = 15;
	
	private void monstersController(MainLoopGame mainLoop) {
		ArrayList<Monster> toRemoveMonster = new ArrayList<Monster>();
		boolean healthCaredMonster = false;
		for (Monster monster : mainLoop.world.monsters) {
			
			if (monster.angleTempNeeded == monster.angleTempMax) {
				
				monster.angleTempNeeded = 0;
				
				float angle = (float) (360.0f * Math.random());
				
				if (Utils.distance2Points(monster.Point3Dto2D(), FirstPersonCameraController.Point3Dto2D()) < MONSTER_SEE_ME) {
					angle = -90+Utils.getAngle(new Point((int) -FirstPersonCameraController.position.x, (int) -FirstPersonCameraController.position.z), new Point((int) monster.x, (int) monster.z));
				}
					
				monster.distanceTemp = (int) (18.0f * Math.random()) + 2;
				
				monster.angleTemp = angle;
				
			} else {
				
				if (monster.angle != monster.angleTemp) {
					
						if (monster.angle > monster.angleTemp) {
							
							if (monster.angleTemp - 1 < monster.angle) {
								monster.angle = monster.angleTemp;
							} else {
								monster.angle -= 1;
								
								if (monster.angle < -360) {
									monster.angle += 360;
								}
							}
							
						} else if (monster.angle < monster.angleTemp) {
							
							if (monster.angleTemp + 1 > monster.angle) {
								monster.angle = monster.angleTemp;
							} else {
								monster.angle += 1;
								
								if (monster.angle > 360) {
									monster.angle -= 360;
								}
							}
						}
					
				} else {
			
					monster.incAngleTempNeeded();
				
				}
			
			}
			
			Point centerPointY = new Point(Utils.positionToPoint(monster.x), Utils.positionToPoint(monster.z));
			float positionYVirtual = Monster.getSmallerYByPoint(centerPointY.x, centerPointY.y, world);
			float positionYVirtualFront = Monster.getSmallerYByPoint(centerPointY.x, centerPointY.y+1, world);
			float positionYVirtualBackWards = Monster.getSmallerYByPoint(centerPointY.x, centerPointY.y-1, world);
			float positionYVirtualLeft = Monster.getSmallerYByPoint(centerPointY.x-1, centerPointY.y, world);
			float positionYVirtualRight = Monster.getSmallerYByPoint(centerPointY.x+1, centerPointY.y, world);
			
			float positionYVirtualLeftFront = Monster.getSmallerYByPoint(centerPointY.x-1, centerPointY.y+1, world);
			float positionYVirtualRightBackWards = Monster.getSmallerYByPoint(centerPointY.x+1, centerPointY.y-1, world);
			float positionYVirtualLeftBackWards = Monster.getSmallerYByPoint(centerPointY.x-1, centerPointY.y-1, world);
			float positionYVirtualRightFront = Monster.getSmallerYByPoint(centerPointY.x+1, centerPointY.y+1, world);
			
			float positionYHeighest = (float) (Utils.findMax((double) positionYVirtual, (double) positionYVirtualFront, (double) positionYVirtualBackWards, (double) positionYVirtualLeft, (double) positionYVirtualRight, (double) positionYVirtualLeftFront, (double) positionYVirtualRightBackWards, (double) positionYVirtualLeftBackWards, (double) positionYVirtualRightFront));
			
			
			float addPositionX = monster.distanceTemp * (float)Math.sin(Math.toRadians(monster.angle)) * ((monster.hit) ? -1 : 1);
	    	float addPositionY = monster.distanceTemp * (float)Math.cos(Math.toRadians(monster.angle)) * ((monster.hit) ? -1 : 1);
	    	 
	    	monster.x -= addPositionX;
	    	monster.z += addPositionY;
	    	
	    	// check collision monster
	    	if (positionYHeighest==-15) {
	    		monster.x =  (float) (Math.random() * world.SEE_WORLD * ((Math.random() > 0.5) ? 1 : -1) * 100);
	    		monster.z =  (float) (Math.random() * world.SEE_WORLD * ((Math.random() > 0.5) ? 1 : -1) * 100);
				monster.y = Monster.getSmallerYByPoint(Utils.positionToPoint(monster.x), Utils.positionToPoint(monster.z), world) * 100;
				
	    	}
	    	
	    	if (monster.y - FirstPersonCameraController.gravityAcceleration_Final * monster.gravityAcceleration > positionYHeighest * 100) {
	    		monster.gravityAcceleration += 0.01;
		        monster.y -= FirstPersonCameraController.gravityAcceleration_Final * monster.gravityAcceleration;
		    } else if (monster.y + FirstPersonCameraController.gravityAcceleration_Final * monster.gravityAcceleration < positionYHeighest * 100) {
		        monster.gravityAcceleration += 0.03;
		        monster.y += FirstPersonCameraController.gravityAcceleration_Final * monster.gravityAcceleration;
		    } else {
		        monster.gravityAcceleration = 0;
		        monster.y = positionYHeighest * 100;
		    }
			
			if (monster.fireTemp == monster.fireTempMax) {
		    	float angle = -90+Utils.getAngle(new Point((int) -FirstPersonCameraController.position.x, (int) -FirstPersonCameraController.position.z), new Point((int) monster.x, (int) monster.z));
				if (monster.health > 0 && Math.abs(angle-monster.angle) < 15 && Utils.distance2Points(monster.Point3Dto2D(), FirstPersonCameraController.Point3Dto2D()) < MONSTER_SEE_ME) {
					float positionBulletX = ((monster.type==0) ? 130 : 75) * ((Math.random() > 0.5f) ? -1 : 1);
					float monsterX = monster.x + positionBulletX * (float)Math.sin(Math.toRadians(90-monster.angle));
					float monsterY = monster.z + positionBulletX * (float)Math.cos(Math.toRadians(90-monster.angle));
					float angleBullet = -90+Utils.getAngle(new Point((int) -FirstPersonCameraController.position.x, (int) -FirstPersonCameraController.position.z), new Point((int) (monsterX), (int) monsterY));
					float dX = monsterX + (int) FirstPersonCameraController.position.x;
					float dZ = monsterY + (int) FirstPersonCameraController.position.z;
					float dY = monster.y + (int) FirstPersonCameraController.position.y + 100;
					float pitchBullet = (float) Math.toDegrees(Math.atan2(dY, Math.sqrt(dZ * dZ + dX * dX)) + Math.PI);
					mainLoop.world.bullets.add(new Bullet(monsterX, monster.y+140, monsterY, -180 + angleBullet + (float) (((Math.random() > 0.5f) ? -1 : 1) * Math.random() * 2f) , -pitchBullet + (float) (((Math.random() > 0.5f) ? -1 : 1) * Math.random() * 2f), 1));
				}
				monster.fireTemp = 0;
			} else {
				monster.fireTemp++;
			}
			
			if (monster.health <= 0) {
				monster.size-=0.02f;
			}
			
			if (monster.size<0) {
				toRemoveMonster.add(monster);
			}
			
			if (monster.health > 0 && Utils.getDistance((int) monster.x, (int) monster.y+200, (int) monster.z, (int) -FirstPersonCameraController.position.x, (int) -FirstPersonCameraController.position.y, (int) -FirstPersonCameraController.position.z) < 350) {
    			// check if health more than 0
				if (User.alive()) {
    				User.health -= 1.f;
    				healthCaredMonster = true;
    			} else {
    				User.makeDead();
    			}
    		}
			
			if (monster.hitValue < monster.hitValueMax && monster.hit) {
				monster.hitValue++;
			} else {
				monster.hit = false;
				monster.hitValue = 0;
			}
		}
		if (toRemoveMonster.size()>0) {
			world.monsters.removeAll(toRemoveMonster);
		}
		
		// hack for health temporarily
		glColor3f(1, 1, 1);
		
		if (!healthCaredMonster && User.alive()) {
			User.healthRegeneration();
		}
	}

	private void bulletController(MainLoopGame mainLoop) {
		ArrayList<Bullet> toRemove = new ArrayList<Bullet>();
		for (Bullet bullet : mainLoop.world.bullets) {
			if (bullet.size < 0.01f) {
				toRemove.add(bullet);
			} else {
				
				float addPositionX = (float)Math.sin(Math.toRadians(bullet.anglex)) * bullet.speed;
				
				bullet.x += addPositionX * bullet.directionX;
						
				Point3DVectored currentPoint = new Point3DVectored(Utils.positionToPoint(bullet.x), Utils.positionToPoint(bullet.y), Utils.positionToPoint(bullet.z));
				
				if (mainLoop.world.brickArrayVectored.contains(currentPoint)) {
					bullet.x -= addPositionX * bullet.directionX;
					bullet.directionX = -bullet.directionX;
				}
				
				float addPositionY = (float)Math.cos(Math.toRadians(bullet.anglex)) * bullet.speed;
				
				bullet.z -= addPositionY * bullet.directionY;
					
				currentPoint = new Point3DVectored(Utils.positionToPoint(bullet.x), Utils.positionToPoint(bullet.y), Utils.positionToPoint(bullet.z));
				
				if (mainLoop.world.brickArrayVectored.contains(currentPoint)) {
					bullet.z += addPositionY * bullet.directionY;
					bullet.directionY = -bullet.directionY;
				}
					
				float addPositionZ = (float) Math.sin(Math.toRadians(bullet.angley)) * bullet.speed;
				
				bullet.y -= addPositionZ * bullet.directionZ;
				
				currentPoint = new Point3DVectored(Utils.positionToPoint(bullet.x), Utils.positionToPoint(bullet.y), Utils.positionToPoint(bullet.z));
				
				if (mainLoop.world.brickArrayVectored.contains(currentPoint)) {
					bullet.y += addPositionZ * bullet.directionZ;
					bullet.directionZ = -bullet.directionZ;
				}

				for (Monster monster : mainLoop.world.monsters) {
					
					if (Utils.getDistance((int) monster.x, (int) monster.y+200, (int) monster.z, (int) bullet.x, (int) bullet.y, (int) bullet.z) < 70) {
						if (monster.health > 0) {
							monster.health -= 5.f;
							float angle = -90+Utils.getAngle(new Point((int) -FirstPersonCameraController.position.x, (int) -FirstPersonCameraController.position.z), new Point((int) monster.x, (int) monster.z));
							if (Math.abs(angle-monster.angle) < 15) {
								monster.hit = true;
							} else {
								monster.angle = (float) (360 * Math.random());
							}
							monster.hitBlend = true;
						}
						toRemove.add(bullet);
					}
				}
				
				if (Utils.getDistance((int) -FirstPersonCameraController.position.x, (int) -FirstPersonCameraController.position.y, (int) -FirstPersonCameraController.position.z, (int) bullet.x, (int) bullet.y, (int) bullet.z) < Bullet.speedMax) {
					if (User.alive()) {
						User.health -= 1.5f;
						toRemove.add(bullet);
					} else {
						User.makeDead();
					}
				}
				
				bullet.size -= 0.02f;
				
			}
		}
		if (toRemove.size() > 0) {
			mainLoop.world.bullets.removeAll(toRemove);
		}
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
		
		world.drawStaff(this);

		world.drawBullet(this);
		
		world.drawWorld(this);
		
		glPopMatrix();
		
		GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, Window.getWidth(), Window.getHeight(), 0, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        
        glColor3f(1, 1, 1);
        
        glPushMatrix();

        //int monsterSize = world.monsters.size();
        
        //FontMineGame.drawString("Monsters: "+monsterSize, (float) (Window.getWidth() * 0.90f), 10);
        
        //String fps = String.valueOf(FPSHandler.getFPS());
        
        //FontMineGame.drawString("FPS: "+fps, Window.getWidth() * 0.90f, 10);

        String health = String.valueOf(Math.round(User.health));
        
        FontMineGame.drawString("Health: "+health, 10, 10);
        
        String crateFound = String.valueOf(User.crateFound);
        
        String crateFoundMax = String.valueOf(User.crateFoundMax);
        
        FontMineGame.drawString("Boxes found: "+crateFound, 10, 20);
        
        String ammo = String.valueOf(User.ammo);
        
        FontMineGame.drawString("Ammo: "+ammo, 10, 30);
        
        if (!User.alive()) {
        	FontMineGame.drawString("You lose", (float) (Window.getWidth() * 0.45f), (float) (Window.getHeight() * 0.25f));
        	FontMineGame.drawString("Press R to restart", (float) (Window.getWidth() * 0.43f), (float) (Window.getHeight() * 0.27f));
        }
        
        if (User.alive() && User.crateFound == User.crateFoundMax && world.horizontalGenerateTemp.contains(new Point(0, world.NOT_SEE_WORLD))) {
        	FontMineGame.drawString("You win", (float) (Window.getWidth() * 0.45f), (float) (Window.getHeight() * 0.25f));
        }
        
        FontMineGame.drawString("You should find all boxes", 10, (int)(Window.getHeight()/2.01f));
        
        glPopMatrix();
        
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();

		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GLU.gluPerspective(45.0f, (float) Window.getWidth() / (float) Window.getHeight(), 0.1f, 50000.0f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
}

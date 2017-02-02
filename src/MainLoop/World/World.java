package MainLoop.World;

import java.awt.Point;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.glu.Sphere;

import MainLoop.FirstPersonCameraController;
import MainLoop.MainLoopGame;
import MainLoop.World.LogicShapes.Bullet;
import MainLoop.World.LogicShapes.Monster;
import MainLoop.World.LogicShapes.Palm;
import MainLoop.World.LogicShapes.User;
import MainLoop.World.OBJLoader.ModelOBJ;
import MainLoop.World.Shapes.Crate;
import MainLoop.World.Shapes.Dome;
import MainLoop.World.Shapes.GroundBrick;
import MainLoop.World.Shapes.SideBrick;
import Utils.Utils;
import Vector.Point3D;
import Vector.Point3DVectored;
import Window.Window;

import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.opengl.Texture;
import static org.lwjgl.opengl.GL11.*;

import Debug.Debug;

public class World {
	public Texture sideGrass;
	public Texture grass;
	public Texture grassFonNewTexture;
	public Texture ground;
	
	public ArrayList<Point3D> brickArray = new ArrayList<Point3D>();;
	public ArrayList<Point> horizontalGenerateTemp = new ArrayList<Point>();
	public ArrayList<Point3D> groundBrickArray = new ArrayList<Point3D>();
	
	public ArrayList<Point3DVectored> brickArrayVectored = new ArrayList<Point3DVectored>();
	
	public HashMap<Point, Integer> verticalMap = new HashMap<Point, Integer>();
	
	public ModelOBJ staff = new ModelOBJ();
	public ModelOBJ palmModel = new ModelOBJ();
	public ModelOBJ bushModel = new ModelOBJ();
	public ModelOBJ rockModel = new ModelOBJ();
	public ModelOBJ flaresModel = new ModelOBJ();
	public ModelOBJ bombModel = new ModelOBJ(); 
	
	public Texture staffTexture;
	public Texture palmTexture;
	public Texture skyTexture;
	public Texture bushTexture;
	public Texture rockTexture;
	public Texture flaresTexture;
	public Texture crateTexture;
	public Texture bombTexture; 
	
	public ArrayList<Bullet> bullets = new ArrayList<Bullet>();
	
	public ArrayList<Monster> monsters = new ArrayList<Monster>();
	
	public ArrayList<Palm> palms = new ArrayList<Palm>();
	
	public ArrayList<Palm> bushes = new ArrayList<Palm>();
	
	public ArrayList<Palm> rocks = new ArrayList<Palm>();
	
	public ArrayList<Palm> flares = new ArrayList<Palm>();
	
	public ArrayList<Palm> points = new ArrayList<Palm>();
	
	public ArrayList<Palm> bombs = new ArrayList<Palm>();
	
	public final int SEE_WORLD = 20;
	
	public final int NOT_SEE_WORLD = 45;
	
	public int timer = 0;
	
	public Timer time;
	
	public World() {
		
		timer = 0;
		
		time = new Timer();

        time.schedule(new TimerTask() {
            @Override
            public void run() {
            	timer++;
            }
        }, 3000, 1000); 
	}
	
	public void drawStaff(MainLoopGame mainLoop) {
		
		GL20.glUseProgram( mainLoop.shader.getProgramId() );
		
		glPushMatrix();
		
		glTranslatef(-FirstPersonCameraController.position.x , -FirstPersonCameraController.position.y - 6,  -FirstPersonCameraController.position.z);
				
		glRotatef(90, 0, 0, 1);
		
		glRotatef(90-mainLoop.camera.yaw, 1, 0, 0);

		glTranslatef(0, 0, 10);

		glRotatef(8, 1, 0, 0);
		
		glRotatef(4, 0, 0, 1);
		
		glRotatef(-mainLoop.camera.pitch, 0, 0, 1);
		
		mainLoop.world.staffTexture.bind();
		
		glScalef(20.f, 20.f, 20.f);
		
	    glCallList(mainLoop.world.staff.modelOBJ);
		
		glPopMatrix();
	}
	
	public void drawWorld(MainLoopGame mainLoop) {
		
		GL20.glUseProgram( 0 );
		
		for (Point3D point : brickArray) {
			
			if (!Utils.visiblePoint(point.Point3Dto2D(), mainLoop, 90)) {
				continue;
			}
			
			int cameraIsByYPositionAmendment = (Utils.positionToPoint(FirstPersonCameraController.position.y)<0) ? 0 : Utils.positionToPoint(FirstPersonCameraController.position.y);
			
			if (!Utils.visiblePoint(point.Point3Dto2D(), mainLoop, (int) (35-cameraIsByYPositionAmendment))) {
				continue;
			}
			
			if (!Utils.visiblePoint(point.Point3Dto2D(), mainLoop, (int) (90+90-35+cameraIsByYPositionAmendment))) {
				continue;
			}
			
			if (Utils.distance2Points(point.Point3Dto2D(), FirstPersonCameraController.Point3Dto2D()) > SEE_WORLD) {
				continue;
			}
			
			glPushMatrix();
			
			glTranslatef(point.x * 100, point.y * 100,  point.z * 100);
			
			SideBrick.draw(this, point);
			
			glPopMatrix();
		}
		for (Point3D point : groundBrickArray) {
			
			if (!Utils.visiblePoint(point.Point3Dto2D(), mainLoop, 90)) {
				continue;
			}
			
			int cameraIsByYPositionAmendment = (Utils.positionToPoint(FirstPersonCameraController.position.y)<0) ? 0 : Utils.positionToPoint(FirstPersonCameraController.position.y);
						
			if (!Utils.visiblePoint(point.Point3Dto2D(), mainLoop, (int) (35-cameraIsByYPositionAmendment))) {
				continue;
			}
			
			if (!Utils.visiblePoint(point.Point3Dto2D(), mainLoop, (int) (90+90-35+cameraIsByYPositionAmendment))) {
				continue;
			}
			
			if (Utils.distance2Points(point.Point3Dto2D(), FirstPersonCameraController.Point3Dto2D()) > SEE_WORLD) {
				continue;
			}
			
			glPushMatrix();
			
			glTranslatef(point.x * 100, point.y * 100,  point.z * 100);
			
			GroundBrick.draw(this, point);
			
			glPopMatrix();
		}
		
		drawPalms(mainLoop);
		
		drawBushes(mainLoop);
		
		drawRocks(mainLoop);
		
		GL20.glUseProgram( mainLoop.shader.getProgramId() );
		
		drawFlares(mainLoop);
		
		drawBombs(mainLoop);
		
		drawPoints(mainLoop);
		
		GL20.glUseProgram(0);
		
		drawDome(mainLoop);	
	}
	
	public void drawPalms(MainLoopGame mainLoop) {
		palmTexture.bind();
		
		for (Palm palm : palms) {
		
			int cameraIsByYPositionAmendment = (Utils.positionToPoint(FirstPersonCameraController.position.y)<0) ? 0 : Utils.positionToPoint(FirstPersonCameraController.position.y);
			
			if (!Utils.visiblePoint(palm.Point3Dto2D(), mainLoop, (int) (35-cameraIsByYPositionAmendment))) {
				continue;
			}
			
			if (!Utils.visiblePoint(palm.Point3Dto2D(), mainLoop, (int) (90+90-35+cameraIsByYPositionAmendment))) {
				continue;
			}
			
			if (Utils.distance2Points(palm.Point3Dto2D(), FirstPersonCameraController.Point3Dto2D()) > SEE_WORLD) {
				continue;
			}
			
			glPushMatrix();
			
			glTranslatef(palm.x, palm.y, palm.z);

			glRotatef(palm.angle, 0, 1, 0);
			
			glScalef(0.6f, 0.6f, 0.6f);
			
		    glCallList(palmModel.modelOBJ);
		    
			glPopMatrix();
		
		}
	}
	
	public void drawBushes(MainLoopGame mainLoop) {	
		
		bushTexture.bind();
		
		for (Palm bush : bushes) {
		
			int cameraIsByYPositionAmendment = (Utils.positionToPoint(FirstPersonCameraController.position.y)<0) ? 0 : Utils.positionToPoint(FirstPersonCameraController.position.y);
			
			if (!Utils.visiblePoint(bush.Point3Dto2D(), mainLoop, (int) (35-cameraIsByYPositionAmendment))) {
				continue;
			}
			
			if (!Utils.visiblePoint(bush.Point3Dto2D(), mainLoop, (int) (90+90-35+cameraIsByYPositionAmendment))) {
				continue;
			}
			
			if (Utils.distance2Points(bush.Point3Dto2D(), FirstPersonCameraController.Point3Dto2D()) > SEE_WORLD) {
				continue;
			}
			
			glPushMatrix();
			
			glTranslatef(bush.x, bush.y+50f, bush.z);
			
			glRotatef(bush.angle, 0, 1, 0);

			glScalef(15.f, 15.f, 15.f);
			
			glCallList(bushModel.modelOBJ);
			
			glPopMatrix();
		
		}
	}
	
	public void drawRocks(MainLoopGame mainLoop) {
		
		rockTexture.bind();
		
		for (Palm rock : rocks) {
		
			int cameraIsByYPositionAmendment = (Utils.positionToPoint(FirstPersonCameraController.position.y)<0) ? 0 : Utils.positionToPoint(FirstPersonCameraController.position.y);
			
			if (!Utils.visiblePoint(rock.Point3Dto2D(), mainLoop, (int) (35-cameraIsByYPositionAmendment))) {
				continue;
			}
			
			if (!Utils.visiblePoint(rock.Point3Dto2D(), mainLoop, (int) (90+90-35+cameraIsByYPositionAmendment))) {
				continue;
			}
			
			if (Utils.distance2Points(rock.Point3Dto2D(), FirstPersonCameraController.Point3Dto2D()) > SEE_WORLD) {
				continue;
			}
			
			glPushMatrix();
			
			glTranslatef(rock.x, rock.y+50f, rock.z);
			
			glRotatef(rock.angle, 0, 1, 0);

			glScalef(15.f, 15.f, 15.f);
			
			glCallList(rockModel.modelOBJ);
			
			glPopMatrix();
		
		}
	}
	
	public void drawFlares(MainLoopGame mainLoop) {
		
		flaresTexture.bind();
		
		for (Palm flare : flares) {
		
			int cameraIsByYPositionAmendment = (Utils.positionToPoint(FirstPersonCameraController.position.y)<0) ? 0 : Utils.positionToPoint(FirstPersonCameraController.position.y);
			
			if (!Utils.visiblePoint(flare.Point3Dto2D(), mainLoop, (int) (35-cameraIsByYPositionAmendment))) {
				continue;
			}
			
			if (!Utils.visiblePoint(flare.Point3Dto2D(), mainLoop, (int) (90+90-35+cameraIsByYPositionAmendment))) {
				continue;
			}
			
			if (Utils.distance2Points(flare.Point3Dto2D(), FirstPersonCameraController.Point3Dto2D()) > SEE_WORLD) {
				continue;
			}
			
			glPushMatrix();
			
			glTranslatef(flare.x, flare.y+80f, flare.z);
			
			glRotatef(mainLoop.angle, 0, 1, 0);

			glScalef(300.f, 300.f, 300.f);
			
			glCallList(flaresModel.modelOBJ);
			
			glPopMatrix();
		
		}
	}
	
	public void drawPoints(MainLoopGame mainLoop) {
		
		for (Palm point : points) {
		
			int cameraIsByYPositionAmendment = (Utils.positionToPoint(FirstPersonCameraController.position.y)<0) ? 0 : Utils.positionToPoint(FirstPersonCameraController.position.y);
			
			if (!Utils.visiblePoint(point.Point3Dto2D(), mainLoop, (int) (25-cameraIsByYPositionAmendment))) {
				continue;
			}
			
			if (!Utils.visiblePoint(point.Point3Dto2D(), mainLoop, (int) (90+90-35+cameraIsByYPositionAmendment))) {
				continue;
			}
			
			if (Utils.distance2Points(point.Point3Dto2D(), FirstPersonCameraController.Point3Dto2D()) > SEE_WORLD) {
				continue;
			}
			
			glPushMatrix();
			
			glTranslatef(point.x, point.y + 100.0f, point.z);
			
			//glRotatef(mainLoop.angle, 0, 1, 0);
			
			Crate.draw(this);
			
			glPopMatrix();
		
		}
	}
	
public void drawBombs(MainLoopGame mainLoop) {
	
		bombTexture.bind();
		
		for (Palm point : bombs) {
		
			int cameraIsByYPositionAmendment = (Utils.positionToPoint(FirstPersonCameraController.position.y)<0) ? 0 : Utils.positionToPoint(FirstPersonCameraController.position.y);
			
			if (!Utils.visiblePoint(point.Point3Dto2D(), mainLoop, (int) (25-cameraIsByYPositionAmendment))) {
				continue;
			}
			
			if (!Utils.visiblePoint(point.Point3Dto2D(), mainLoop, (int) (90+90-35+cameraIsByYPositionAmendment))) {
				continue;
			}
			
			if (Utils.distance2Points(point.Point3Dto2D(), FirstPersonCameraController.Point3Dto2D()) > SEE_WORLD) {
				continue;
			}
			
			glPushMatrix();
			
			glTranslatef(point.x, point.y + 100.0f, point.z);
			
			glRotatef(mainLoop.angle, 0, 1, 0);
			
			glScalef(300.f, 300.f, 300.f); 
			
			glCallList(bombModel.modelOBJ);
			
			glPopMatrix();
		
		}
	}
	
	public void drawDome(MainLoopGame mainLoop) {
		
		glPushMatrix();
		
		glTranslatef(-FirstPersonCameraController.position.x, -FirstPersonCameraController.position.y , -FirstPersonCameraController.position.z);
		
		glRotatef(-mainLoop.camera.yaw * 1.01f, 0, 1, 0);
		
		Dome.draw(this);
		
		glPopMatrix();
	}
	
	public void generateWorld() {
		brickArray.add(new Point3D(0, -1, 0));
		palmRandom = (float) Math.random() / 20.f;
		bushRandom = (float) Math.random() / 4.f;
		generateNewWorld(1);
	}
	
	float palmRandom = 0;
	float bushRandom = 0;
	
	private void generateNewPalmForPoint(Point3DVectored point) {
		boolean generatedPalm = false;
		boolean generatedBush = false;
		boolean generateRock = false;
		boolean generateFlare = false;
		boolean generateBomb = false;
		boolean generateCrate = false;
		// generate Palm
		if (Math.random() < palmRandom) {
			float palmX = point.x * 100;
			float palmY = point.z * 100;
			float palmZ = Utils.getSmallerYByPoint(Utils.positionToPoint(palmX), Utils.positionToPoint(palmY), this);
			Palm palm = new Palm(palmX, palmZ * 100, palmY);
			palms.add(palm);
			verticalMap.put(palm.Point3Dto2D(), (int) (palmZ + 5));
			generatedPalm = true;
		}
		// generate Bush
		if (!generatedPalm && Math.random() < bushRandom) {
			float palmX = point.x * 100;
			float palmY = point.z * 100;
			float palmZ = Utils.getSmallerYByPoint(Utils.positionToPoint(palmX), Utils.positionToPoint(palmY), this) * 100;
			bushes.add(new Palm(palmX, palmZ, palmY));
			generatedBush = true;
		}
		// generate Rock
		if (!generatedBush && !generatedPalm && Math.random() < 0.02f) {
			float palmX = point.x * 100;
			float palmY = point.z * 100;
			float palmZ = Utils.getSmallerYByPoint(Utils.positionToPoint(palmX), Utils.positionToPoint(palmY), this) * 100;
			rocks.add(new Palm(palmX, palmZ, palmY));
			generateRock = true;
		}
		// generate Flare
		if (!generatedBush && !generatedPalm && !generateRock && Math.random() < 0.007f) {
			float palmX = point.x * 100;
			float palmY = point.z * 100;
			float palmZ = Utils.getSmallerYByPoint(Utils.positionToPoint(palmX), Utils.positionToPoint(palmY), this) * 100;
			flares.add(new Palm(palmX, palmZ, palmY));
			generateFlare = true;
		}
		// generate Bomb
		if (!generateFlare && !generatedBush && !generatedPalm && !generateRock && Math.random() < 0.01f) {
			float palmX = point.x * 100;
			float palmY = point.z * 100;
			float palmZ = Utils.getSmallerYByPoint(Utils.positionToPoint(palmX), Utils.positionToPoint(palmY), this) * 100;
			bombs.add(new Palm(palmX, palmZ, palmY));
			generateBomb = true;
		}
		// generate Crate
		if (!generateFlare && !generatedBush && !generatedPalm && !generateRock && !generateBomb && Math.random() < 0.002f) {
			float palmX = point.x * 100;
			float palmY = point.z * 100;
			float palmZ = Utils.getSmallerYByPoint(Utils.positionToPoint(palmX), Utils.positionToPoint(palmY), this) * 100;
			points.add(new Palm(palmX, palmZ, palmY));
			User.crateFoundMax++;
			generateCrate = true;
		}
		if (Math.random() < 0.001f) {
			if (point.x > 25 || point.z > 25 || point.x < -25 || point.z < -25) {
				float monsterX = point.x * 100;
				float monsterY = point.z * 100;
				float monsterZ = Monster.getSmallerYByPoint(Utils.positionToPoint(monsterX), Utils.positionToPoint(monsterY), this) * 100;
				monsters.add(new Monster(monsterX, monsterZ, monsterY));
			}
		}*/
	}
	
	private void generateNewWorld(int radius) {
		ArrayList<Point3D> generatedList = new ArrayList<Point3D>();
		for (int i = 0; i < radius; i++) {
				Collections.shuffle(brickArray, new Random(System.nanoTime()));
				ArrayList<Point3D> generatedListPoint = getNewPointGenerated();
				generatedList.addAll(generatedListPoint);
				brickArray.addAll(generatedListPoint);
		}
		generateGroundBrick();
		for (Point3D point : generatedList) {
			if (!(point.x > NOT_SEE_WORLD || point.z > NOT_SEE_WORLD || point.x < -NOT_SEE_WORLD || point.z < -NOT_SEE_WORLD)) {
				generateNewPalmForPoint(point.Point3DtoPoint3DVectored());
			}
		}
	}
	
	private int generateNewPointSeed(Point point) {
		double random = Math.random();
		int result = ((random > 0.1f && random < 0.9f) ? 0 : ((random < 0.1f) ? -1 : -1));
		int resultMapOptional = (existingMap(point, 0)) ? 4 : result;
		return (existingMap(point, 3)) ? -5 : resultMapOptional;
	}
	
	private ArrayList<Point3D> getBrickArrayLastGenerated() {
		ArrayList<Point3D> needed = new ArrayList<Point3D>();
		for (Point3D point : brickArray) {
			if (!point.stopClosest) {
				continue;
			}
			if (point.closest.size()<8) {
				needed.add(point);
			} else {
				point.stopClosest = false;
			}
		}
		return needed;
	}
	
	private ArrayList<Point3D> getNewPointGenerated() {
		ArrayList<Point3D> newPointArray = new ArrayList<Point3D>();
		ArrayList<Point3D> lastGeneratedBrickArray = getBrickArrayLastGenerated();
		for (Point3D point : lastGeneratedBrickArray) {
			int zonesTopRight = generateNewPointSeed(new Point(point.x, point.z + 1));
			int zonesDownRight = generateNewPointSeed(new Point(point.x + 1, point.z));
			int zonesDownLeft = generateNewPointSeed(new Point(point.x, point.z - 1));
			int zonesTopLeft = generateNewPointSeed(new Point(point.x - 1, point.z));
			if (zonesTopRight != -5 && !horizontalGenerateTemp.contains(new Point(point.x, point.z + 1))) {
				horizontalGenerateTemp.add(new Point(point.x, point.z + 1));
				Point3D newPoint = new Point3D(point.x, point.y + zonesTopRight, point.z + 1);
				brickArrayVectored.add(newPoint.Point3DtoPoint3DVectored());
				newPointArray.add(newPoint);
			}
			if (zonesDownLeft != -5 && !horizontalGenerateTemp.contains(new Point(point.x, point.z - 1))) {
				horizontalGenerateTemp.add(new Point(point.x, point.z - 1));
				Point3D newPoint = new Point3D(point.x, point.y + zonesDownLeft, point.z - 1);
				brickArrayVectored.add(newPoint.Point3DtoPoint3DVectored());
				newPointArray.add(newPoint);
			}
			if (zonesTopLeft != -5 && !horizontalGenerateTemp.contains(new Point(point.x - 1, point.z))) {
				horizontalGenerateTemp.add(new Point(point.x - 1, point.z));
				Point3D newPoint = new Point3D(point.x - 1, point.y + zonesTopLeft, point.z);
				brickArrayVectored.add(newPoint.Point3DtoPoint3DVectored());
				newPointArray.add(newPoint);
			}
			if (zonesDownRight != -5 && !horizontalGenerateTemp.contains(new Point(point.x + 1, point.z))) {
				horizontalGenerateTemp.add(new Point(point.x + 1, point.z));
				Point3D newPoint = new Point3D(point.x + 1, point.y + zonesDownRight, point.z);
				brickArrayVectored.add(newPoint.Point3DtoPoint3DVectored());
				newPointArray.add(newPoint);
			}
			
			if (zonesTopRight != -5 && !horizontalGenerateTemp.contains(new Point(point.x + 1, point.z + 1))) {
				horizontalGenerateTemp.add(new Point(point.x + 1, point.z + 1));
				Point3D newPoint = new Point3D(point.x + 1, point.y + zonesTopRight, point.z + 1);
				brickArrayVectored.add(newPoint.Point3DtoPoint3DVectored());
				newPointArray.add(newPoint);
			}
			if (zonesDownRight != -5 && !horizontalGenerateTemp.contains(new Point(point.x + 1, point.z - 1))) {
				horizontalGenerateTemp.add(new Point(point.x + 1, point.z - 1));
				Point3D newPoint = new Point3D(point.x + 1, point.y + zonesDownRight, point.z - 1);
				brickArrayVectored.add(newPoint.Point3DtoPoint3DVectored());
				newPointArray.add(newPoint);
			}
			if (zonesTopLeft != -5 && !horizontalGenerateTemp.contains(new Point(point.x - 1, point.z + 1))) {
				horizontalGenerateTemp.add(new Point(point.x - 1, point.z + 1));
				Point3D newPoint = new Point3D(point.x - 1, point.y + zonesTopLeft, point.z + 1);
				brickArrayVectored.add(newPoint.Point3DtoPoint3DVectored());
				newPointArray.add(newPoint);
			}
			if (zonesDownLeft != -5 && !horizontalGenerateTemp.contains(new Point(point.x - 1, point.z - 1))) {
				horizontalGenerateTemp.add(new Point(point.x - 1, point.z - 1));
				Point3D newPoint = new Point3D(point.x - 1, point.y + zonesDownLeft, point.z - 1);
				brickArrayVectored.add(newPoint.Point3DtoPoint3DVectored());
				newPointArray.add(newPoint);
			}
		}
		return newPointArray;
	}
	
	private void generateGroundBrick() {
		ArrayList<Point3D> lastGeneratedBrickArray = getBrickArrayLastGenerated();
		for (Point3D point : lastGeneratedBrickArray) {
			for (Point3D pointInner : lastGeneratedBrickArray) {
				if (pointInner.Point3Dto2D().equals(new Point(point.x, point.z + 1))) {
					if (!point.closest.contains(pointInner)) {
						point.closest.add(pointInner);
					}
				}
				if (pointInner.Point3Dto2D().equals(new Point(point.x, point.z - 1))) {
					if (!point.closest.contains(pointInner)) {
						point.closest.add(pointInner);
					}
				}
				if (pointInner.Point3Dto2D().equals(new Point(point.x - 1, point.z))) {
					if (!point.closest.contains(pointInner)) {
						point.closest.add(pointInner);
					}
				}
				if (pointInner.Point3Dto2D().equals(new Point(point.x + 1, point.z))) {
					if (!point.closest.contains(pointInner)) {
						point.closest.add(pointInner);
					}
				}
				
				if (pointInner.Point3Dto2D().equals(new Point(point.x + 1, point.z + 1))) {
					if (!point.closest.contains(pointInner)) {
						point.closest.add(pointInner);
					}
				}
				if (pointInner.Point3Dto2D().equals(new Point(point.x + 1, point.z - 1))) {
					if (!point.closest.contains(pointInner)) {
						point.closest.add(pointInner);
					}
				}
				if (pointInner.Point3Dto2D().equals(new Point(point.x - 1, point.z + 1))) {
					if (!point.closest.contains(pointInner)) {
						point.closest.add(pointInner);
					}
				}
				if (pointInner.Point3Dto2D().equals(new Point(point.x - 1, point.z - 1))) {
					if (!point.closest.contains(pointInner)) {
						point.closest.add(pointInner);
					}
				}
			}
		}
		for (Point3D point : lastGeneratedBrickArray) {
			int smallerY = 0;
			for (Point3D pointClosest : point.closest) {
				if (pointClosest.y<smallerY) {
					smallerY = pointClosest.y;
				}
			}
			for (int i=point.y-1; i>smallerY; i--) {
				Point3D newPoint = new Point3D(point.x , i , point.z);
				for (Point3D pointClosest : point.closest) {
					if (pointClosest.Point3Dto2D().equals(new Point(newPoint.x, newPoint.z + 1))) {
						if (!newPoint.closest.contains(pointClosest)) {
							newPoint.closest.add(pointClosest);
						}
					}
					if (pointClosest.Point3Dto2D().equals(new Point(newPoint.x, newPoint.z - 1))) {
						if (!newPoint.closest.contains(pointClosest)) {
							newPoint.closest.add(pointClosest);
						}
					}
					if (pointClosest.Point3Dto2D().equals(new Point(newPoint.x - 1, newPoint.z))) {
						if (!newPoint.closest.contains(pointClosest)) {
							newPoint.closest.add(pointClosest);
						}
					}
					if (pointClosest.Point3Dto2D().equals(new Point(newPoint.x + 1, newPoint.z))) {
						if (!newPoint.closest.contains(pointClosest)) {
							newPoint.closest.add(pointClosest);
						}
					}
					
					if (pointClosest.Point3Dto2D().equals(new Point(newPoint.x + 1, newPoint.z + 1))) {
						if (!newPoint.closest.contains(pointClosest)) {
							newPoint.closest.add(pointClosest);
						}
					}
					if (pointClosest.Point3Dto2D().equals(new Point(newPoint.x + 1, newPoint.z - 1))) {
						if (!newPoint.closest.contains(pointClosest)) {
							newPoint.closest.add(pointClosest);
						}
					}
					if (pointClosest.Point3Dto2D().equals(new Point(newPoint.x - 1, newPoint.z + 1))) {
						if (!newPoint.closest.contains(pointClosest)) {
							newPoint.closest.add(pointClosest);
						}
					}
					if (pointClosest.Point3Dto2D().equals(new Point(newPoint.x - 1, newPoint.z - 1))) {
						if (!newPoint.closest.contains(pointClosest)) {
							newPoint.closest.add(pointClosest);
						}
					}
				}
				if (!groundBrickArray.contains(newPoint)) {
					brickArrayVectored.add(newPoint.Point3DtoPoint3DVectored());
					groundBrickArray.add(newPoint);
				}
			}
			if (!verticalMap.containsKey(point.Point3Dto2D())) 
			verticalMap.put(point.Point3Dto2D(), point.y);
		}
	}
	
	private boolean existingMap(Point point, int add) {
		return point.x > NOT_SEE_WORLD + add || point.y > NOT_SEE_WORLD + add || point.x < -NOT_SEE_WORLD - add || point.y < -NOT_SEE_WORLD - add;
	}
	
	public void needToGenerateNewWorld(MainLoopGame mainLoop) {
		int radius = NOT_SEE_WORLD+3;
		
		Point pointRight = new Point(radius,0);//Utils.positionToPoint(FirstPersonCameraController.position.x + radius), Utils.positionToPoint(FirstPersonCameraController.position.z));
		Point pointLeft = new Point(0,radius);//Utils.positionToPoint(FirstPersonCameraController.position.x - radius), Utils.positionToPoint(FirstPersonCameraController.position.z));
		Point pointTop = new Point(-radius,0);//Utils.positionToPoint(FirstPersonCameraController.position.x), Utils.positionToPoint(FirstPersonCameraController.position.z + radius));
		Point pointDown = new Point(0,-radius);//Utils.positionToPoint(FirstPersonCameraController.position.x), Utils.positionToPoint(FirstPersonCameraController.position.z - radius));
		
		if (!horizontalGenerateTemp.contains(pointRight) && !existingMap(pointRight, 3) || !horizontalGenerateTemp.contains(pointLeft) && !existingMap(pointLeft, 3) ||
			!horizontalGenerateTemp.contains(pointTop) && !existingMap(pointTop, 3) || !horizontalGenerateTemp.contains(pointDown) && !existingMap(pointDown, 3)) {
			if (mainLoop.angle % 20 == 0) {
				generateNewWorld(1);
			}
		}
	}
	
	public void getPortal(MainLoopGame mainLoop) {
		ArrayList<Palm> toRemove = new ArrayList<Palm>();
		for (Palm flare : flares) {
			if (Utils.getDistance((int) flare.x, (int) flare.y + 200, (int) flare.z, (int) -FirstPersonCameraController.position.x, (int) -FirstPersonCameraController.position.y, (int) -FirstPersonCameraController.position.z) < 250) {
				toRemove.add(flare);
				User.addAmmo(1);
				break;
			}
		}
		if (toRemove.size() > 0) {
			flares.removeAll(toRemove);
		}
	}
	
	public void crateController(MainLoopGame mainLoop) {
		ArrayList<Palm> toRemove = new ArrayList<Palm>();
		for (Palm point : points) {
			if (Utils.getDistance((int) point.x, (int) point.y + 200, (int) point.z, (int) -FirstPersonCameraController.position.x, (int) -FirstPersonCameraController.position.y, (int) -FirstPersonCameraController.position.z) < 250) {
				toRemove.add(point);
				User.crateFound++;
				break;
			}
		}
		if (toRemove.size() > 0) {
			points.removeAll(toRemove);
		}
	}
	
	public void bombController(MainLoopGame mainLoop) {
		ArrayList<Palm> toRemove = new ArrayList<Palm>();
		for (Palm point : bombs) {
			if (Utils.getDistance((int) point.x, (int) point.y + 200, (int) point.z, (int) -FirstPersonCameraController.position.x, (int) -FirstPersonCameraController.position.y, (int) -FirstPersonCameraController.position.z) < 250) {
				toRemove.add(point);
				// check if health more than 0
				if (User.alive()) {
    				User.health -= 30.f;
    			} else {
    				User.makeDead();
    			}
				break;
			}
			for (Monster monster : mainLoop.world.monsters) {
				if (Utils.getDistance((int) monster.x, (int) monster.y, (int) monster.z, (int) point.x, (int) point.y, (int) point.z) < 250) {
					monster.health -= 30.f;
					monster.hitBlend = true;
					toRemove.add(point);
					break;
				}
			}
		}
		if (toRemove.size() > 0) {
			bombs.removeAll(toRemove);
		}
	}
}

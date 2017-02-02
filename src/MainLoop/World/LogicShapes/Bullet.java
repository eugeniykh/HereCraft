package MainLoop.World.LogicShapes;

import java.awt.Point;

import Utils.Utils;
import Vector.Point3DVectored;

public class Bullet {
	
	public float x, y, z;
	
	public float anglex, angley;
	
	public int directionX = 1, directionY = 1, directionZ = 1;
	
	public float size = 1.0f;

	public static final int speedMax = 80;
	
	public float speed = speedMax;
	
	public int owner = 0;
	
	public Bullet(float x, float y, float z, float anglex, float angley, int owner) {
		this.x = x;
		this.y = y;
		this.z = z;	
		this.anglex = anglex;
		this.angley = angley;
		this.owner = owner;
		
		float addPositionX = (float)Math.sin(Math.toRadians(this.anglex)) * this.speed;
		
		this.x += addPositionX * this.directionX;
				
		float addPositionY = (float)Math.cos(Math.toRadians(this.anglex)) * this.speed;
		
		this.z -= addPositionY * this.directionY;
			
		float addPositionZ = (float) Math.sin(Math.toRadians(this.angley)) * this.speed;
		
		this.y -= addPositionZ * this.directionZ;
	}
	
	public Point Point3Dto2D() {
    	return new Point(Utils.positionToPoint(x), Utils.positionToPoint(z));
    }
}

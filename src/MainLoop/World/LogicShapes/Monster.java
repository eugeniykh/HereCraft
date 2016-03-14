package MainLoop.World.LogicShapes;

import java.awt.Point;

import MainLoop.World.World;
import Utils.Utils;

public class Monster {
	public float x, y, z, angle, angleTemp = 0, distanceTemp = 5, angleTempNeeded, gravityAcceleration;
	
	public float health = 100.0f;
	
	public int texture = 0;
	
	public final int angleTempMax = 40;
	
	public float size = 1.0f;
	
	public boolean hit = false, hitBlend = false;
	
	public int hitValue = 0, hitValueMax = 20;
	
	public int fireTemp = 0, fireTempMax = 5;
	
	public int type = 0;
	
	public Monster(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		// random texture switching
		texture = (int) Math.round(Math.random() * 1);
		
		type = (int) Math.round(Math.random() * 1);
	}
	
	public void incAngleTempNeeded() {
		angleTempNeeded++;
	}
	
    public Point Point3Dto2D() {
    	return new Point(Utils.positionToPoint(x), Utils.positionToPoint(z));
    }
    
	public static int getSmallerYByPoint(int posX, int posY, World world) {
    	int positionY = -15;
        if (world.verticalMap.get(new Point(posX, posY))!=null) {
        	positionY = world.verticalMap.get(new Point(posX, posY));
        	if (world.palms.contains(new Palm(posX * 100, -1, posY * 100))) {
        		positionY -= 5;
        	}
        }
        return positionY;
    }
}

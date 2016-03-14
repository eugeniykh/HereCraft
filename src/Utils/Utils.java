package Utils;

import java.awt.Point;

import MainLoop.FirstPersonCameraController;
import MainLoop.MainLoopGame;
import MainLoop.World.World;
import MainLoop.World.LogicShapes.Palm;

public class Utils {

	public static float distance2Points(Point pointA, Point pointB) {
		return (float) Math.sqrt(((float) pointA.x - (float) pointB.x) * ((float) pointA.x - (float) pointB.x)
				+ ((float) pointA.y - (float) pointB.y) * ((float) pointA.y - (float) pointB.y));
	}

	public static boolean visiblePoint(Point p1, MainLoopGame mainLoop, int angle) {
		int x = (int) (FirstPersonCameraController.Point3Dto2D().x
				- (float) Math.sin(Math.toRadians(-mainLoop.camera.yaw - angle)) * 15f);
		int y = (int) (FirstPersonCameraController.Point3Dto2D().y
				- (float) Math.cos(Math.toRadians(-mainLoop.camera.yaw - angle)) * 15f);
		return pointNearLine(new Point(x, y), new Point((int) FirstPersonCameraController.Point3Dto2D().x,
				(int) FirstPersonCameraController.Point3Dto2D().y), p1) > -50;
	}

	public static float pointNearLine(Point line_point1, Point line_point2, Point testPoint) {
		return (line_point2.x - line_point1.x) * (testPoint.y - line_point1.y)
				- (line_point2.y - line_point1.y) * (testPoint.x - line_point1.x);
	}
	
    public static int positionToPoint(float pos) {
    	return (int) Math.round(pos / 100.f);
    }

    public static float getAngle(Point target, Point object) {
        float angle = (float) Math.toDegrees(Math.atan2(target.y - object.y, target.x - object.x));

        if(angle < 0){
            angle += 360;
        }

        return angle;
    }
    
    /**
     * Returns distance between 3D set of coords
     * 
     * @param x1
     *            first x coord
     * @param y1
     *            first y coord
     * @param z1
     *            first z coord
     * @param x2
     *            second x coord
     * @param y2
     *            second y coord
     * @param z2
     *            second z coord
     * @return distance between coords
     */
    public static double getDistance(float x1, float y1, float z1, float x2, float y2, float z2)
    {
      float dx = x1 - x2;
      float dy = y1 - y2;
      float dz = z1 - z2;

      // We should avoid Math.pow or Math.hypot due to perfomance reasons
      return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
    
	public static double findMax(double... vals) {
		double max = Double.NEGATIVE_INFINITY;

		for (double d : vals) {
			if (d > max)
				max = d;
		}

		return max;
	}
	
	public static int getSmallerYByPoint(int posX, int posY, World world) {
    	int positionY = 15;
        if (world.verticalMap.get(new Point(posX, posY))!=null) {
        	positionY = world.verticalMap.get(new Point(posX, posY));
        }
        return positionY;
    }
}

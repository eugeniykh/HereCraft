package MainLoop.World.LogicShapes;

import java.awt.Point;

import MainLoop.World.World;
import Utils.Utils;
import Vector.Point3DVectored;

public class Palm {
	public float x, y, z, angle;
	
	public Palm(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.angle = (float) (Math.random() * 360);
	}
	
	public Point Point3Dto2D() {
    	return new Point(Utils.positionToPoint(x), Utils.positionToPoint(z));
    }
	
	@Override
	public boolean equals(Object v) {
		boolean retVal = false;

		if (v instanceof Palm) {
			Palm ptr = (Palm) v;
			retVal = ptr.x == this.x && ptr.z == this.z;
		}

		return retVal;
	}
}

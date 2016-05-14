package Vector;

import java.awt.Point;
import java.util.ArrayList;

public class Point3D {
	public int x, y, z;
	public Point3D(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public ArrayList<Point3D> closest = new ArrayList<Point3D>();
	
	public boolean stopClosest = true;
	
	public Point Point3Dto2D() {
		return new Point(x, z);
	}
	
	public Point3DVectored Point3DtoPoint3DVectored() {
		return new Point3DVectored(x, y, z);
	}
	
	@Override
	public boolean equals(Object v) {
		boolean retVal = false;

		if (v instanceof Point3D) {
			Point3D ptr = (Point3D) v;
			retVal = ptr.x == this.x && ptr.y == this.y && ptr.z == this.z;
		}

		return retVal;
	}
}

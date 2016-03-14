package Vector;

import java.awt.Point;

public class Point3DVectored {
	public int x, y, z;

	public Point3DVectored(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	public boolean equals(Object v) {
		boolean retVal = false;

		if (v instanceof Point3DVectored) {
			Point3DVectored ptr = (Point3DVectored) v;
			retVal = ptr.x == this.x && ptr.y == this.y && ptr.z == this.z;
		}

		return retVal;
	}
	
	public Point Point3Dto2D() {
		return new Point(x, z);
	}
}

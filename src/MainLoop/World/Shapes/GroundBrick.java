package MainLoop.World.Shapes;

import static org.lwjgl.opengl.GL11.glPopMatrix;

import org.lwjgl.opengl.GL11;

import MainLoop.World.World;
import Vector.Point3D;

public class GroundBrick {

	public static void draw(World world, Point3D point) {
		
		GL11.glPushMatrix();
		
		GL11.glScalef(50.f, 50.f, 50.f);
		
		world.ground.bind();
		
		if (!point.closest.contains(new Point3D(point.x, point.y, point.z + 1))) {
		
	        GL11.glBegin(GL11.GL_QUADS); 
	        // право
		        GL11.glTexCoord2f(0, 0);
		        GL11.glVertex3f( 1.0f, 1.0f, 1.0f);
		        GL11.glTexCoord2f(1, 0);
		        GL11.glVertex3f(-1.0f, 1.0f, 1.0f);
		        GL11.glTexCoord2f(1, 1);
		        GL11.glVertex3f(-1.0f,-1.0f, 1.0f);
		        GL11.glTexCoord2f(0, 1);
		        GL11.glVertex3f( 1.0f,-1.0f, 1.0f);
	        GL11.glEnd();
        
		}
       
		if (!point.closest.contains(new Point3D(point.x, point.y, point.z - 1))) {
		
	        GL11.glBegin(GL11.GL_QUADS); 
	        // лево
		        GL11.glTexCoord2f(1, 1);
		        GL11.glVertex3f( 1.0f,-1.0f,-1.0f);
		        GL11.glTexCoord2f(0, 1);
		        GL11.glVertex3f(-1.0f,-1.0f,-1.0f);
		        GL11.glTexCoord2f(0, 0);
		        GL11.glVertex3f(-1.0f, 1.0f,-1.0f);
		        GL11.glTexCoord2f(1, 0);
		        GL11.glVertex3f( 1.0f, 1.0f,-1.0f);
	        GL11.glEnd();
        
		}
		
		if (!point.closest.contains(new Point3D(point.x - 1, point.y, point.z))) {
        
	        GL11.glBegin(GL11.GL_QUADS); 
	        // перед 
	        	GL11.glTexCoord2f(0, 0);
		        GL11.glVertex3f(-1.0f, 1.0f, 1.0f);
		        GL11.glTexCoord2f(1, 0);
		        GL11.glVertex3f(-1.0f, 1.0f,-1.0f);
		        GL11.glTexCoord2f(1, 1);
		        GL11.glVertex3f(-1.0f,-1.0f,-1.0f);
		        GL11.glTexCoord2f(0, 1);
		        GL11.glVertex3f(-1.0f,-1.0f, 1.0f);
	        GL11.glEnd();
	        
		}
		
		if (!point.closest.contains(new Point3D(point.x + 1, point.y, point.z))) {
        
	        GL11.glBegin(GL11.GL_QUADS); 
	        // назад
	        	GL11.glTexCoord2f(0, 0);
		        GL11.glVertex3f( 1.0f, 1.0f,-1.0f);
		        GL11.glTexCoord2f(1, 0);
		        GL11.glVertex3f( 1.0f, 1.0f, 1.0f);
		        GL11.glTexCoord2f(1, 1);
		        GL11.glVertex3f( 1.0f,-1.0f, 1.0f);
		        GL11.glTexCoord2f(0, 1);
		        GL11.glVertex3f( 1.0f,-1.0f,-1.0f);
	        GL11.glEnd();    
	        
		}
        
		glPopMatrix();
	}
}

package MainLoop.World.Shapes;

import static org.lwjgl.opengl.GL11.glPopMatrix;

import org.lwjgl.opengl.GL11;

import MainLoop.World.World;
import Vector.Point3D;

public class Crate {
		
		public static void draw(World world) {
			
			world.crateTexture.bind();
			
			GL11.glPushMatrix();
			
			GL11.glScalef(50.f, 50.f, 50.f);

				GL11.glBegin(GL11.GL_QUADS); 
			 	GL11.glTexCoord2f(0, 0);
			        GL11.glVertex3f( 1.0f, 1.0f,-1.0f);
			        GL11.glTexCoord2f(1, 0);
			        GL11.glVertex3f(-1.0f, 1.0f,-1.0f);   
			        GL11.glTexCoord2f(1, 1);
			        GL11.glVertex3f(-1.0f, 1.0f, 1.0f);
			        GL11.glTexCoord2f(0, 1);
			        GL11.glVertex3f( 1.0f, 1.0f, 1.0f); 
		        GL11.glEnd();
		        GL11.glBegin(GL11.GL_QUADS); 
			        GL11.glTexCoord2f(0, 0);
			        GL11.glVertex3f( 1.0f, 1.0f, 1.0f);
			        GL11.glTexCoord2f(1, 0);
			        GL11.glVertex3f(-1.0f, 1.0f, 1.0f);
			        GL11.glTexCoord2f(1, 1);
			        GL11.glVertex3f(-1.0f,-1.0f, 1.0f);
			        GL11.glTexCoord2f(0, 1);
			        GL11.glVertex3f( 1.0f,-1.0f, 1.0f);
		        GL11.glEnd();
		        GL11.glBegin(GL11.GL_QUADS); 
			        GL11.glTexCoord2f(1, 1);
			        GL11.glVertex3f( 1.0f,-1.0f,-1.0f);
			        GL11.glTexCoord2f(0, 1);
			        GL11.glVertex3f(-1.0f,-1.0f,-1.0f);
			        GL11.glTexCoord2f(0, 0);
			        GL11.glVertex3f(-1.0f, 1.0f,-1.0f);
			        GL11.glTexCoord2f(1, 0);
			        GL11.glVertex3f( 1.0f, 1.0f,-1.0f);
		        GL11.glEnd();
		        GL11.glBegin(GL11.GL_QUADS); 
		        	GL11.glTexCoord2f(0, 0);
			        GL11.glVertex3f(-1.0f, 1.0f, 1.0f);
			        GL11.glTexCoord2f(1, 0);
			        GL11.glVertex3f(-1.0f, 1.0f,-1.0f);
			        GL11.glTexCoord2f(1, 1);
			        GL11.glVertex3f(-1.0f,-1.0f,-1.0f);
			        GL11.glTexCoord2f(0, 1);
			        GL11.glVertex3f(-1.0f,-1.0f, 1.0f);
		        GL11.glEnd();
		        GL11.glBegin(GL11.GL_QUADS); 
		        	GL11.glTexCoord2f(0, 0);
			        GL11.glVertex3f( 1.0f, 1.0f,-1.0f);
			        GL11.glTexCoord2f(1, 0);
			        GL11.glVertex3f( 1.0f, 1.0f, 1.0f);
			        GL11.glTexCoord2f(1, 1);
			        GL11.glVertex3f( 1.0f,-1.0f, 1.0f);
			        GL11.glTexCoord2f(0, 1);
			        GL11.glVertex3f( 1.0f,-1.0f,-1.0f);
		        GL11.glEnd(); 
	        
			glPopMatrix();
	}
}

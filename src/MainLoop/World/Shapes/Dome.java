package MainLoop.World.Shapes;

import static org.lwjgl.opengl.GL11.glPopMatrix;

import org.lwjgl.opengl.GL11;

import MainLoop.World.World;

public class Dome {
	
	public static void draw(World world) {
		
		GL11.glPushMatrix();
		
		GL11.glScalef(4000.f, 4000.f, 4000.f);
        
        world.skyTexture.bind();
		
		GL11.glBegin(GL11.GL_QUADS); 
        // верх
		 	GL11.glTexCoord2f(1, 1);
	        GL11.glVertex3f( 1.0f, 1.0f,-1.0f);
	        GL11.glTexCoord2f(0, 1);
	        GL11.glVertex3f(-1.0f, 1.0f,-1.0f);   
	        GL11.glTexCoord2f(0, 0);
	        GL11.glVertex3f(-1.0f, 1.0f, 1.0f);
	        GL11.glTexCoord2f(1, 0);
	        GL11.glVertex3f( 1.0f, 1.0f, 1.0f); 
        
        GL11.glEnd();
       
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
        
		glPopMatrix();
	}
}

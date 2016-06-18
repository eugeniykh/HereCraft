package MainLoop;

import java.awt.Point;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

import Debug.Debug;
import MainLoop.World.World;
import Utils.Utils;
 
//First Person Camera Controller
public class FirstPersonCameraController
{
    //3d vector to store the camera's position in
    public static Vector3f    position    = null;
    
    //3d vector to store the camera's position in
    private static Vector3f    positionBackWards    = null;
    private static Vector3f    positionNewVirtual    = null;
    
    
    //the rotation around the Y axis of the camera
    public float        yaw         = 0.0f;
    //the rotation around the X axis of the camera
    public float       pitch       = 0.0f;
    
    public final static float gravityAcceleration_Final = 9.8f;
    
    public float gravityAcceleration = 0;
    
    public boolean up = false, upComplete = false;
    
    public float yawUp;
    public int lastDirectUp, lastAngleUp;

    //Constructor that takes the starting x, y, z location of the camera
    public FirstPersonCameraController(float x, float y, float z)
    {
        //instantiate position Vector3f to the x y z params.
        position = new Vector3f(x, y, z);
        positionBackWards = new Vector3f(x, y, z);
        positionNewVirtual = new Vector3f(x, y, z);
    }
    
    //increment the camera's current yaw rotation
    public void yaw(float amount)
    {
        //increment the yaw by the amount param
        yaw += amount;
    }
     
    //increment the camera's current yaw rotation
    public void pitch(float amount)
    {
        //increment the pitch by the amount param
        pitch -= amount;
    	if (pitch<-90f) {
    		pitch = -90f;
    	}
    	if (pitch>90f) {
    		pitch = 90f;
    	}
    }
    
    //moves the camera forward relative to its current rotation (yaw)
    public void walk(float distance, int direct, int angleDirect, MainLoopGame mainLoop)
    {
    	 float yawFront = ((!up && upComplete) ? yaw : yawUp);
    	
    	 float addPositionX = distance * (float)Math.sin(Math.toRadians(yawFront + angleDirect));
    	 float addPositionY = distance * (float)Math.cos(Math.toRadians(yawFront + angleDirect));
    	
    	 positionBackWards.x = position.x;
    	 positionBackWards.z = position.z;
    	 
    	 position.x -= addPositionX * direct;
    	 position.z += addPositionY * direct;
    	 
    	 positionNewVirtual.x = position.x - addPositionX * 2.5f * direct;
    	 positionNewVirtual.z = position.z + addPositionY * 2.5f * direct;
    	 
    	 mainLoop.world.getPortal(mainLoop);
    	 
    	 positionBackWards.y = (gravityAcceleration != 0) ? Utils.positionToPoint(-position.y)-3 : getSmallerYByPoint(Utils.positionToPoint(-positionBackWards.x), Utils.positionToPoint(-positionBackWards.z), mainLoop.world);
    	 
    	 if (getSmallerYByPoint(Utils.positionToPoint(-positionNewVirtual.x), Utils.positionToPoint(-positionNewVirtual.z), mainLoop.world)-positionBackWards.y>1) {
    		 position.x += addPositionX * direct;
    		 position.z -= addPositionY * direct;
    		 
    		 float TempYaw360 = (float) ((yawFront + angleDirect) - (Math.floor((yawFront + angleDirect) / 360) * 360));
    		 
    		 float tempFirstAngle = TempYaw360;
    		 
    		 if (TempYaw360 >= 0 && TempYaw360 <= 45.0f) {
    			 	tempFirstAngle = 0.0f;
				} else if (TempYaw360 > 45.0f && TempYaw360 <= 90.0f) {
					tempFirstAngle = 90.0f;
				} else if (TempYaw360 > 90 && TempYaw360 < 135.0f) {
					tempFirstAngle = 90.0f;
				} else if (TempYaw360 > 135.0f && TempYaw360 <= 180.0f) {
					tempFirstAngle = 180.0f;
				} else if (TempYaw360 > 180.0f && TempYaw360 <= 225.0f) {
					tempFirstAngle = 180.0f;
				} else if (TempYaw360 > 225.0f && TempYaw360 <= 270.0f) {
					tempFirstAngle = 270.0f;
				} else if (TempYaw360 > 270.0f && TempYaw360 <= 315.0f) {
					tempFirstAngle = 270.0f;
				} else if (TempYaw360 > 315.0f && TempYaw360 <= 360.0f) {
					tempFirstAngle = 0.0f;
				}
    		 
    		 float addPositionXAfter = distance * (float)Math.sin(Math.toRadians(tempFirstAngle));
        	 float addPositionYAfter = distance * (float)Math.cos(Math.toRadians(tempFirstAngle));
        	 
        	 positionBackWards.x = position.x;
        	 positionBackWards.z = position.z;
        	 
        	 position.x -= addPositionXAfter * direct;
        	 position.z += addPositionYAfter * direct;
        	 
        	 positionNewVirtual.x = position.x - addPositionXAfter * 2.5f * direct;
        	 positionNewVirtual.z = position.z + addPositionYAfter * 2.5f * direct;
        	 
        	 positionBackWards.y = (gravityAcceleration != 0) ? Utils.positionToPoint(-position.y)-3 : getSmallerYByPoint(Utils.positionToPoint(-positionBackWards.x), Utils.positionToPoint(-positionBackWards.z), mainLoop.world);
        	 
        	 if (getSmallerYByPoint(Utils.positionToPoint(-positionNewVirtual.x), Utils.positionToPoint(-positionNewVirtual.z), mainLoop.world)- positionBackWards.y>1) {
         		
        		 position.x += addPositionXAfter * direct;
            	 position.z -= addPositionYAfter * direct;
        		 
        		 if (TempYaw360 >= 0 && TempYaw360 <= 45.0f) {
						TempYaw360 = 90.0f;
					} else if (TempYaw360 > 45.0f && TempYaw360 <= 90.0f) {
						TempYaw360 = 0.0f;
					} else if (TempYaw360 > 90 && TempYaw360 < 135.0f) {
						TempYaw360 = 180.0f;
					} else if (TempYaw360 > 135.0f
							&& TempYaw360 <= 180.0f) {
						TempYaw360 = 90.0f;
					} else if (TempYaw360 > 180.0f
							&& TempYaw360 <= 225.0f) {
						TempYaw360 = 270.0f;
					} else if (TempYaw360 > 225.0f
							&& TempYaw360 <= 270.0f) {
						TempYaw360 = 180.0f;
					} else if (TempYaw360 > 270.0f
							&& TempYaw360 <= 315.0f) {
						TempYaw360 = 0.0f;
					} else if (TempYaw360 > 315.0f
							&& TempYaw360 <= 360.0f) {
						TempYaw360 = 270.0f;
					}
        		 
        		 float addPositionXAfter2 = distance * (float)Math.sin(Math.toRadians(TempYaw360));
            	 float addPositionYAfter2 = distance * (float)Math.cos(Math.toRadians(TempYaw360));
            	 
            	 position.x -= addPositionXAfter2 * direct;
            	 position.z += addPositionYAfter2 * direct; // BACK
            	 
            	 positionNewVirtual.x = position.x - addPositionXAfter2 * 2.5f * direct;
            	 positionNewVirtual.z = position.z + addPositionYAfter2 * 2.5f * direct;
            	 
            	 positionBackWards.y = (gravityAcceleration != 0) ? Utils.positionToPoint(-position.y)-3 : getSmallerYByPoint(Utils.positionToPoint(-positionBackWards.x), Utils.positionToPoint(-positionBackWards.z), mainLoop.world);
            	 
            	 if (getSmallerYByPoint(Utils.positionToPoint(-positionNewVirtual.x), Utils.positionToPoint(-positionNewVirtual.z), mainLoop.world)-positionBackWards.y>1) {
              		
            		 position.x += addPositionXAfter2 * direct;
                	 position.z -= addPositionYAfter2 * direct; // BACK
            	 }
        	 }
    	 }
    }
    
  //moves the camera backward relative to its current rotation (yaw)
    public void walkForward(float distance, MainLoopGame mainLoop)
    {
    	walk(distance, 1, 0, mainLoop);
    }
     
    //moves the camera backward relative to its current rotation (yaw)
    public void walkBackwards(float distance, MainLoopGame mainLoop)
    {
    	walk(distance, -1, 0, mainLoop);
    }
     
    //strafes the camera left relitive to its current rotation (yaw)
    public void strafeLeft(float distance, MainLoopGame mainLoop)
    {
    	walk(distance, 1, -90, mainLoop);
    }
     
    //strafes the camera right relitive to its current rotation (yaw)
    public void strafeRight(float distance, MainLoopGame mainLoop)
    {
    	walk(distance, 1, 90, mainLoop);
    }
    
    public int getSmallerYByPoint(int posX, int posY, World world) {
    	int positionY = 40;
        if (world.verticalMap.get(new Point(posX, posY))!=null) {
        	positionY = world.verticalMap.get(new Point(posX, posY));
        }
        return positionY;
    }
    
    //translates and rotate the matrix so that it looks through the camera
    //this dose basic what gluLookAt() does
    public void lookThrough(World world)
    {
    	//roatate the pitch around the X axis
        GL11.glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        //roatate the yaw around the Y axis
        GL11.glRotatef(yaw, 0.0f, 1.0f, 0.0f);
        //translate to the position vector's location
        float positionYVirtual = getSmallerYByPoint(Utils.positionToPoint(-position.x), Utils.positionToPoint(-position.z), world)+2;
        if (position.y - gravityAcceleration_Final * gravityAcceleration > positionYVirtual * -100 && !up) {
        	gravityAcceleration += 0.1;
        	position.y -= gravityAcceleration_Final * gravityAcceleration;
        } else if (position.y + gravityAcceleration_Final * gravityAcceleration < positionYVirtual * -100 || up) {
        	gravityAcceleration += 0.3;
        	position.y += gravityAcceleration_Final * gravityAcceleration;
        } else {
        	gravityAcceleration = 0;
            position.y = (positionYVirtual * -100);
            upComplete = true;
        }
        if (up && gravityAcceleration > 0 ) {
        	up = false;
        	upComplete = false;
        }
        GL11.glTranslatef(position.x, position.y , position.z);
    }
   
    public static Point Point3Dto2D() {
    	return new Point(Utils.positionToPoint(-position.x), Utils.positionToPoint(-position.z));
    }
    
}


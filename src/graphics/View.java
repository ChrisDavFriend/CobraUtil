package graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.gluPerspective;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import data.ExceptionHandle;

public class View {
	
	public final static char X = 'X', Y = 'Y', Z = 'Z';
	
	private static enum Dimension{
		
		X, Y, Z;
		
		private float location, rotation;
		
		public void setLocation(float location){
			this.location = location;
		}
		
		public void alterLocation(float amount){
			location += amount;
		}
		
		public float getLocation(){
			return location;
		}
		
		public void setRotation(float rotation){
			this.rotation = rotation;
		}
		
		public void alterRotation(float amount){
			float newRot = rotation + amount;
			if(newRot > 360)newRot -= 360*((int)newRot/360);
			if(newRot < 0)newRot = 360 - (360*(Math.abs(newRot)/360));
			rotation = newRot;
		}
		
		public float getRotation(){
			return rotation;
		}
		
	}
	
	public static short WINDOW_WIDTH, WINDOW_HEIGHT;
	private static float fovY;
	
	public static void readyView(){
		readyView(Display.getDisplayMode(), "CobraUtil - unamed window", 70f, true, false);
	}
	
	public static void readyView(DisplayMode display, String windowName, float fovY, boolean vSync, boolean fullscreen){
		try {
			Display.setDisplayMode(display);
			Display.setTitle(windowName);
			Display.setVSyncEnabled(vSync);
			Display.setFullscreen(fullscreen);
			Display.create();
		} catch (LWJGLException e) {
			System.err.println("Couldn't create display!");
			e.printStackTrace();
			System.exit(0);
		}
		
		WINDOW_WIDTH = (short) Display.getWidth();
		WINDOW_HEIGHT = (short) Display.getHeight();
		
		System.out.println("Display created. Native display pixel count: (x|y) " + WINDOW_WIDTH + "|" + WINDOW_HEIGHT);
		
		restartView();
		
		View.fovY = fovY;
		
	}
	
	public static void restartView(){
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		gluPerspective(fovY, (float)WINDOW_WIDTH/WINDOW_HEIGHT, 0.01f, 1000f);
		glViewport(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
	}

	/**
	 * Get the location of the requested dimension.
	 * @param dimension
	 * @return Location of the dimension, -1 if no such dimension exists.
	 */
	public static float getDimensionLocation(char dimension){
		Dimension d = Dimension.valueOf(Character.toString(dimension));
		if(d == null){
			ExceptionHandle.noSuchDimension(dimension);
			return -1f;
		}
		return d.getLocation();
	}
	
	/**
	 * Get the rotation of the requested dimension.
	 * @param dimension
	 * @return Rotation of the dimension, -1 if no such dimension exists.
	 */
	public static float getDimensionRotation(char dimension){
		Dimension d = Dimension.valueOf(Character.toString(dimension));
		if(d == null){
			ExceptionHandle.noSuchDimension(dimension);
			return -1f;
		}
		return d.getRotation();
	}
	
	/**
	 * @return The field of view(y). 
	 */
	public static float getFieldOfView(){
		return fovY;
	}
	
	/**
	 * Resets the View's dimension locations to their original point. (0,0,0)
	 */
	public static void resetViewLocation(){
		for(Dimension d : Dimension.values())
			d.setLocation(0f);
		embedMatrix();
	}
	
	/**
	 * Resets the requested dimension's location to 0.
	 * @param dimension
	 */
	public static void resetViewLocation(char dimension){
		Dimension d = Dimension.valueOf(Character.toString(dimension));
		if(d == null)ExceptionHandle.noSuchDimension(dimension);
		else{
			d.setLocation(0f);
			embedMatrix();
		}
	}
	
	/**
	 * Resets the View's dimension rotations to their original values. (0,0,0)
	 */
	public static void resetViewRotation(){
		for(Dimension d : Dimension.values())
			d.setRotation(0f);
		embedMatrix();
	}
	
	/**
	 * Resets the requested dimension's rotation to 0.
	 * @param dimension 
	 */
	public static void resetViewRotation(char dimension){
		Dimension d = Dimension.valueOf(Character.toString(dimension));
		if(d == null)ExceptionHandle.noSuchDimension(dimension);
		else{
			d.setRotation(0f);
			embedMatrix();
		}
	}
	
	/**
	 * Alter the requested dimensions location by the given amount. 
	 * (d.location += amount)
	 * @param dimension
	 * @param amount
	 */
	public static void alterViewLocation(char dimension, float amount){
		Dimension d = Dimension.valueOf(Character.toString(dimension));
		if(d == null)ExceptionHandle.noSuchDimension(dimension);
		else{
			d.alterLocation(amount);
			embedMatrix();
		}
	}
	
	/**
	 * Alter the requested dimensions location by the given amount. 
	 * (d.rotation += amount)
	 * @param dimension
	 * @param amount
	 */
	public static void alterViewRotation(char dimension, float amount){
		Dimension d = Dimension.valueOf(Character.toString(dimension));
		if(d == null)ExceptionHandle.noSuchDimension(dimension);
		else{
			d.alterRotation(amount);
			embedMatrix();
		}
	}
	
	/**
	 * Change the field of view by the given amount.
	 * @param amount
	 */
	public static void alterFieldOfView(float amount){
		float newFov = fovY + amount;
		if(newFov > 0 && newFov <= 180){
			fovY = newFov;
			restartView();
		}
		else ExceptionHandle.printToErrorStream("Altering the Field of View with the amount " + amount + " will go beyond 180 degrees or less than 0 degrees. (result: " + newFov + ")");
	}
	
	/**
	 * Sets the requested dimension's location to the given value.
	 * @param dimension
	 * @param value
	 */
	public static void setViewLocation(char dimension, float value){
		Dimension d = Dimension.valueOf(Character.toString(dimension));
		if(d == null)ExceptionHandle.noSuchDimension(dimension);
		else{
			d.setLocation(value);
			embedMatrix();
		}
	}
	
	/**
	 * Sets the requested dimension's rotation to the given value.
	 * @param dimension
	 * @param value
	 */
	public static void setViewRotation(char dimension, float value){
		Dimension d = Dimension.valueOf(Character.toString(dimension));
		if(d == null)ExceptionHandle.noSuchDimension(dimension);
		else{
			d.setRotation(value);
			embedMatrix();
		}
	}
	
	/**
	 * Pops matrix, then apply all of the dimensions values to it.
	 */
	private static void embedMatrix(){
		glPopMatrix();
		glPushMatrix();
		
		glRotatef(Dimension.X.getRotation(), 1f, 0f, 0f);
		glRotatef(Dimension.Y.getRotation(), 0f, 1f, 0f);
		glRotatef(Dimension.Z.getRotation(), 0f, 0f, 1f);
		glTranslatef(Dimension.X.getLocation(), Dimension.Y.getLocation(), Dimension.Z.getLocation());

	}
	

}

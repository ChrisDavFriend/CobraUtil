package control;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import org.lwjgl.opengl.Display;

import data.ExceptionHandle;

public class Loop {
	
	private static Game game;
	
	public static enum State{NULL, INIT, RUNNING, FINISHING, CLOSING}
	public static State currentState = State.NULL;
	
	private static int updateRate = 30;
	private static double delta;
	
	public static void readyLoop(Game game){
		currentState = State.INIT;
		Loop.game = game;
		game.init();
	}
	
	public static void begin(){
		
		long time = System.nanoTime()/1000000;
		
		while(!Display.isCloseRequested()){
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			
			long newTime = (System.nanoTime()/1000000);
			delta = newTime - time;
			time = newTime;
			
			game.update(delta);
			
			
			Display.update();
			Display.sync(updateRate);
		}
	}
	
	public static void setTargetedUpdateRate(int rate){
		if(rate > 0)updateRate = rate;
		else ExceptionHandle.printToErrorStream("Targeted frame rate " + rate + " is below zero! The rate will not be set.");
	}
	
	public static double getDelta(){
		return delta;
	}
	
}

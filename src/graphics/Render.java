package graphics;

import static org.lwjgl.opengl.GL11.GL_BACK;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_COORD_ARRAY;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TRIANGLE_STRIP;
import static org.lwjgl.opengl.GL11.GL_VERTEX_ARRAY;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glCullFace;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDisableClientState;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnableClientState;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glTexCoordPointer;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertexPointer;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glGenBuffers;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;

import data.CharData;

public class Render {
	
	static FloatBuffer vertexBuffer, textureBuffer;
	static int vertexHandler, textureHandler;
	
	public static int font_arial, fontSize = 32;
	
	public interface RenderLayer{}
	
	static Map<RenderLayer, LinkedList<RenderObject>> renderStack2D = new HashMap<RenderLayer, LinkedList<RenderObject>>();
	static LinkedList<RenderObject> renderStack3D = new LinkedList<RenderObject>();
	
	public static boolean in3DSpace;
	
	/**
	 * "Create" the Renderer with the Display width/height.
	 */
	public static void readyRenderer(){
		readyRenderer(2000);
	}
	
	/**
	 * "Create" the Renderer.
	 * @param windowWidth
	 * @param windowHeight
	 */
	public static void readyRenderer(int verticesLimit, RenderLayer... layers){

		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
		glEnable(GL_TEXTURE_2D);
	    glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		
		vertexBuffer = BufferUtils.createFloatBuffer(verticesLimit);
		textureBuffer = BufferUtils.createFloatBuffer(verticesLimit);
		
		vertexHandler = glGenBuffers();
		textureHandler = glGenBuffers();
		
		try {
			font_arial = Spritesheet.createSpritesheet(new File(Render.class.getResource("/res/font_arial.png").toURI()));
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		for(RenderLayer layer : layers){
			renderStack2D.put(layer, new LinkedList<RenderObject>());
		}
		
		in3DSpace = false;
		
		glPushMatrix();
		
	}
	
	public static void setRenderLayers(RenderLayer... layers){
		renderStack2D.clear();
		for(RenderLayer layer : layers){
			renderStack2D.put(layer, new LinkedList<RenderObject>());
		}
	}
	
	
	private static void renderTexturedModel(float x, float y, float z, float rotX, float rotY, float rotZ, TexturedModel tm){
	    
	    putAndBindBuffers(tm.getVertices(), tm.getTextureCoords());
	    
        glPushMatrix();
        
		glTranslatef(x, y, z);
		
		glRotatef(rotX, 1f, 0f, 0f);
		glRotatef(rotY, 0f, 1f, 0f);
		glRotatef(rotZ, 0f, 0f, 1f);
		
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 24);
        
        glPopMatrix();
        
	}
	
	private static void renderBillboard(float x, float y, float z, TexturedModel tm){
		
		putAndBindBuffers(tm.getVertices(), tm.getTextureCoords());
        
        glPushMatrix();
        
		glTranslatef(x, y, z);
		
		glRotatef(360f - View.getDimensionRotation(View.Y), 0f, 1f, 0f);
		
        glDrawArrays(GL_TRIANGLE_STRIP, 0, 24);
        
        glPopMatrix();
        
	}
	
	private static void render2DQuad(float x, float y, float width, float height, float angle, Sprite sprite){;
		
		putAndBindBuffers(
				new float[]{
					x, y + height, 
					x + width, y + height,
					x, y,
					x + width, y
				}, sprite.getTextureCoords()
		);
		
		glPushMatrix();
		
		glTranslatef(x+(width/2), y+(height/2), 0);
		glRotatef(angle, 0.0f, 0.0f, 1.0f);
		glTranslatef(-(x+(width/2)), -(y+(height/2)), 0);
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
		
		glPopMatrix();
		
	}
	
	private static void renderText(float x, float y, float r , float g, float b, float alpha, String text){
		
		int sheetWidth = Spritesheet.getSpritesheet(font_arial).getSheetWidth();
		int gridSize = sheetWidth/(sheetWidth/16);
		final float cellSize = 1.0f / gridSize;
		final float ySpace = y + fontSize;
		
		CharData lastChar = null;
		float padding = 0;
		for(int i = 0; i < text.length(); i++){
			float fontSizePercentile = (Spritesheet.getSpritesheet(font_arial).getSheetWidth()/gridSize)  / fontSize;//What to divide certain numbers by in the case the font size is not the same size as the texture sheet
			int asciiCode = (int) text.charAt(i);
			float cellX = ((int) asciiCode % gridSize) * cellSize;
			float cellY = ((int) asciiCode / gridSize) * cellSize;
			if(lastChar != null)padding += (lastChar.getXDimensions() + lastChar.getPadding()) / fontSizePercentile;
			try{
				byte character = Byte.parseByte(Character.toString(text.charAt(i)));
				String numName;
				switch(character){
				case 1:
					numName = "ONE";
				case 2:
					numName = "TWO";
				case 3:
					numName = "THREE";
				case 4:
					numName = "FOUR";
				case 5:
					numName = "FIVE";
				case 6:
					numName = "SIX";
				case 7:
					numName = "SEVEN";
				case 8:
					numName = "EIGHT";
				case 9:
					numName = "NINE";
				default:
					numName = "ZERO";
				}
				lastChar = CharData.valueOf(numName);
			} catch(NumberFormatException e){
				if(text.charAt(i) == ' ') lastChar = CharData.space;
				else lastChar = CharData.valueOf(Character.toString(text.charAt(i)));
			}
			float xSpace = (float) ((x - ((fontSize - (lastChar.getXDimensions() / fontSizePercentile)) / 2)) + padding);
			
			/* How the above works:
			 * Take the true size in pixels of the current character, lastChar.getXDimensions() / fontSizePercentile, and have it subtracted by the current font size.
			 * You are left with the empty space to the left and right of the character. Then, you divide this number in half, and subtract it from the requested X position. What this does,
			 * is take the whitespace to the left of the current character on the sheet(assuming it's centered), and moves the whole draw back that amount of space. This centers the characters
			 * on the exact position they are expected to be in, essentially erasing empty space.
			 */
			
			
			putAndBindBuffers(
					new float[]{
						xSpace, ySpace,
						xSpace + fontSize, ySpace,
						xSpace, y,
						xSpace + fontSize, y,
					},
					new float[]{
							cellX, cellY+cellSize,
							cellX+cellSize, cellY+cellSize, 
							cellX, cellY,
							cellX+cellSize, cellY,
					}
			);
			
			
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			
			glColor4f(r, g, b, alpha);
			glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
			glColor4f(1f, 1f, 1f, 1f);
			
		}
		
	}
	
	private static void putAndBindBuffers(float[] vertices, float[] texCoords){
	    vertexBuffer.clear();
	    vertexBuffer.put(vertices);
	    vertexBuffer.flip();

	    textureBuffer.clear();
	    textureBuffer.put(texCoords);
	    textureBuffer.flip();
		
		
		glBindBuffer(GL_ARRAY_BUFFER, vertexHandler);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
        glVertexPointer((in3DSpace ? 3 : 2), GL_FLOAT, 0, 0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        
        glBindBuffer(GL_ARRAY_BUFFER, textureHandler);
	    glBufferData(GL_ARRAY_BUFFER, textureBuffer, GL_STATIC_DRAW);
	    glTexCoordPointer(2, GL_FLOAT, 0, 0);
	    glBindBuffer(GL_ARRAY_BUFFER, 0);
	}

	public static void requestDrawTexturedSquare(float x, float y, float width, float height, float angle, Sprite sprite, RenderLayer layer){
		renderStack2D.get(layer).add(new RenderTextureObject(x, y, width, height, angle, sprite));
	}
	
	public static void requestDrawText(float x, float y, float r, float g, float b, float alpha, String text, RenderLayer layer){
		renderStack2D.get(layer).add(new RenderTextObject(x, y, r, g, b, alpha, text));
	}
	
	public static void requestDrawText(float x, float y, String text, RenderLayer layer){
		renderStack2D.get(layer).add(new RenderTextObject(x, y, 0f, 0f, 0f, 1f, text));
	}
	
	public static void requestDrawTexturedModel(float x, float y, float z, float rotX, float rotY, float rotZ, TexturedModel tm){
		renderStack3D.add(new RenderTexturedModelObject(x, y, z, rotX, rotY, rotZ, tm));
	}
	
	public static void requestDrawBillboard(float x, float y, float z, TexturedModel billboard){
		renderStack3D.add(new RenderBillboardObject(x, y, z, billboard));
	}
	
	
	public static void startRender(){
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_TEXTURE_COORD_ARRAY);
		
		if(!renderStack3D.isEmpty()){
			if(!in3DSpace)start3DSpace();
			for(RenderObject ro : renderStack3D)ro.render();
			renderStack3D.clear();
		}
		
		for(RenderLayer layer : renderStack2D.keySet()){
			if(!renderStack2D.get(layer).isEmpty()){
				if(in3DSpace)start2DSpace();
				break;
			}
		}
		
		for(RenderLayer layer : renderStack2D.keySet()){
			for(RenderObject ro : renderStack2D.get(layer)) ro.render();
			renderStack2D.get(layer).clear();
		}
		
		
		glDisableClientState(GL_VERTEX_ARRAY);
		glDisableClientState(GL_TEXTURE_COORD_ARRAY);
	}
	
	private static void start2DSpace(){
		
		if(in3DSpace)end3DSpace();
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_BLEND);
		glLoadIdentity();
		
	}
	
	private static void start3DSpace(){
		if(in3DSpace){
			System.err.println("We are already in 3D space!");
			return;
		}
		View.restartView();
		glMatrixMode(GL_MODELVIEW);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		
		in3DSpace = true;
		
	}
	
	private static void end3DSpace(){
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		glClearDepth(1);
		glDisable(GL_DEPTH_TEST);
		in3DSpace = false;
	}
	
	
	private static interface RenderObject {
		public void render();
	}
	
	private static class RenderTextureObject implements RenderObject{
		float x, y, width, height, angle;
		Sprite sprite;
		
		public RenderTextureObject(float x, float y, float width, float height, float angle, Sprite sprite){
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.angle = angle;
			this.sprite = sprite;
		}
		
		public void render(){
			Render.render2DQuad(x, y, width, height, angle, sprite);
		}
		
	}
	
	private static class RenderTextObject implements RenderObject{
		float x, y, r, g, b, alpha;
		String text;
		
		public RenderTextObject(float x, float y, float r, float g, float b, float alpha, String text){
			this.x = x;
			this.y = y;
			this.r = r;
			this.g = g;
			this.b = b;
			this.alpha = alpha;
			this.text = text;
		}
		
		
		public void render(){
			Render.renderText(x, y, r, g, b, alpha, text);
		}
		
	}
	
	private static class RenderTexturedModelObject implements RenderObject {
		TexturedModel tm;
		float x, y, z, rotX, rotY, rotZ;
		
		public RenderTexturedModelObject(float x, float y, float z, float rotX, float rotY, float rotZ, TexturedModel tm){
			this.x = x;
			this.y = y;
			this.z = z;
			this.rotX = rotX;
			this.rotY = rotY;
			this.rotZ = rotZ;
			this.tm = tm;
		}
		
		public void render(){
			Render.renderTexturedModel(x, y, z, rotX, rotY, rotZ, tm);
		}
		
	}
	
	private static class RenderBillboardObject implements RenderObject {

		TexturedModel tm;
		float x, y, z;
		
		public RenderBillboardObject(float x, float y, float z, TexturedModel tm){
			this.x = x;
			this.y = y;
			this.z = z;
			this.tm = tm;
		}
		
		
		@Override
		public void render() {
			Render.renderBillboard(x, y, z, tm);
		}
		
	}

	
}

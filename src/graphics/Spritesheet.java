package graphics;

import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

import data.ExceptionHandle;

public class Spritesheet {
	
	private static int boundSheet = 0;
	private static Map<Integer, Spritesheet> sheetLibrary = new HashMap<Integer, Spritesheet>();
	
	
	private int OGL_ID, sheetWidth, sheetHeight;
	private Map<String, Sprite> sprites = new HashMap<String, Sprite>();
	
	/**
	 * Create a new spritesheet with the given URL.
	 * @param file Spritesheet file
	 */
	private Spritesheet(File file){
		try{
			BufferedImage image = ImageIO.read(file.toURI().toURL().openStream());
			OGL_ID = loadSpritesheet(image);
			sheetWidth = image.getHeight();
			sheetHeight = image.getWidth();
		} catch(IOException e){
			e.printStackTrace();
		}
		sheetLibrary.put(OGL_ID, this);
	}
	
	public static int createSpritesheet(File file){
		return new Spritesheet(file).getID();
	}
	
	public void bind(){
		if(boundSheet == OGL_ID)ExceptionHandle.sheetAlreadyBound();
		else {
			glBindTexture(GL_TEXTURE_2D, OGL_ID);
			boundSheet = OGL_ID;
		}
	}
	
	public void addSprite(int sheetPosition, float xSize, float ySize, String spriteKey){
		sprites.put(spriteKey, new Sprite(sheetPosition, xSize, ySize, this));
	}
	
	public Sprite getSprite(String spriteKey){
		return sprites.get(spriteKey);
	}
	
	public int getSheetWidth(){
		return sheetWidth;
	}
	
	public int getSheetHeigth(){
		return sheetHeight;
	}
	
	public int getID(){
		return OGL_ID;
	}
	
	public static Spritesheet getBoundSpritesheet(){
		if(sheetLibrary.isEmpty()){
			ExceptionHandle.noSpritesheetsLoaded();
			return null;
		}else return sheetLibrary.get(boundSheet);
	}
	
	public static Spritesheet getSpritesheet(int key){
		return sheetLibrary.get(key);
	}
	
	/**
	 * Loads a spritesheet image into OpenGL.
	 * @param url The URL from which a stream will be opened with. (Can use file.toURL())
	 * @return ID that OpenGL uses to recognize the image.
	 */
	private static int loadSpritesheet(BufferedImage image){
		final int BYTES_PER_PIXEL = 4;
		
		int[] pixels = new int[image.getWidth() * image.getHeight()];
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
		
		ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * BYTES_PER_PIXEL);
		
		for(int y = 0; y < image.getHeight(); y++){
		    for(int x = 0; x < image.getWidth(); x++){
		        int pixel = pixels[y * image.getWidth() + x];
		        buffer.put((byte) ((pixel >> 16) & 0xFF));
		buffer.put((byte) ((pixel >> 8) & 0xFF));    
		buffer.put((byte) (pixel & 0xFF));           
		buffer.put((byte) ((pixel >> 24) & 0xFF));   
		    }
		}
		
		buffer.flip();
		
		int sheetID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, sheetID); 

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

		return sheetID;
	}

}

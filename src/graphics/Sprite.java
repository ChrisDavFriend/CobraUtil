package graphics;

public class Sprite {
	
	private final float[] texCoords;
	
	public Sprite(int sheetPosition, float xSize, float ySize, Spritesheet spritesheet){
		int gridSize = spritesheet.getSheetWidth()/(spritesheet.getSheetWidth()/16);
		float size = 1.0f/gridSize;
		
		float texX = (sheetPosition % gridSize) * size;
		float texY = (sheetPosition / gridSize) * size;
		
		texCoords = new float[]{
				texX,  texY+size,
				texX+size, texY+size,
				texX, texY, 
				texX+size, texY,
		};
	}
	
	public float[] getTextureCoords(){
		return texCoords;
	}

}

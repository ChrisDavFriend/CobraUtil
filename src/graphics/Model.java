package graphics;


public abstract class Model {
	
	final float[] vertices;
	
	
	public Model(float[] vertices){
		this.vertices = vertices;
	}
	
	public float[] getVertices(){
		return vertices;
	}
	
	/**
	 * Create a model that represents a cube
	 * @param radX Half X size
	 * @param radY Half Y size
	 * @param radZ Half Z size
	 * @param texturePos 0 = Front face, 1 = Right, 2 = Back, 3 = Left, 4 = Top, 5 = Bottom
	 * @param sheetID
	 * @return
	 */
	public static TexturedModel createCubeModel(float radX, float radY, float radZ, int[] texturePos, int sheetID){
		
		int sheetWidth = Spritesheet.getSpritesheet(sheetID).getSheetWidth();
		
		int gridSize = sheetWidth/(sheetWidth/16);
		float size = 1.0f/gridSize;
		
		float[] texX = new float[6];
		float[] texY = new float[6];
		
		for(int i = 0; i < 6; i++){
			int j = 0;
			if(i >= texturePos.length)j = i;
			texX[i] = (texturePos[i - j] % gridSize) * size;
			texY[i] = (texturePos[i - j] / gridSize) * size;
		}
		
		return new TexturedModel(
				new float[]{
						//Front face
						-radX, radY, radZ,
						-radX, -radY, radZ,
						radX, radY, radZ,
			            radX, -radY, radZ,
		
			            // Right face
			            radX, -radY, radZ,
			            radX, -radY, -radZ,
			            radX, radY, radZ,
			            radX, radY, -radZ,
			            
			            // Back face
			            radX, radY, -radZ,
			            radX, -radY, -radZ,
			            -radX, radY, -radZ,
			            -radX, -radY, -radZ,
			            
			            // Left face
			            -radX, -radY, -radZ,
			            -radX, -radY, radZ,
			            -radX, radY, -radZ,
			            -radX, radY, radZ,
			            
			            // Top face
			            -radX, radY, radZ,
			            radX, radY, radZ,
			            -radX, radY, -radZ,
			            radX, radY, -radZ,
			            
			            // Bottom face
			            -radX, -radY, radZ,
			            -radX, -radY, -radZ,
			            radX, -radY, radZ,
			            radX, -radY, -radZ
					},
					new float[]{
						
						//Front face
						texX[0], texY[0],
						texX[0] + size, texY[0],
						texX[0], texY[0] + size,
						texX[0] + size, texY[0] + size,
			            
			            //Right face
						texX[1] + size, texY[1] + size,
						texX[1] + size, texY[1],
						texX[1], texY[1] + size,
						texX[1], texY[1],
			            
			            //Back face
						texX[2], texY[2],
						texX[2], texY[2] + size,
						texX[2] + size, texY[2],
						texX[2] + size, texY[2] + size,
			            
			            //Left face
						texX[3] + size, texY[3] + size,
						texX[3], texY[3] + size,
						texX[3] + size, texY[3],
						texX[3], texY[3],
			            
			            //Top face
			            texX[4], texY[4],
						texX[4] + size, texY[4],
						texX[4], texY[4] + size,
						texX[4] + size, texY[4] + size,
			            
			            //Bottom face
						texX[5], texY[5],
						texX[5] + size, texY[5],
						texX[5], texY[5] + size,
						texX[5] + size, texY[5] + size
						
				},
				sheetID
		);
	}
	
	public static TexturedModel createBillboardModel(float halfWidth, float halfHeight, int texturePos, int sheetID){
		
		
		int sheetWidth = Spritesheet.getSpritesheet(sheetID).getSheetWidth();
		
		int gridSize = sheetWidth/(sheetWidth/16);
		float size = 1.0f/gridSize;
		
		float texX = (texturePos % gridSize) * size;
		float texY = (texturePos / gridSize) * size;
		
		
		return new TexturedModel(
				new float[]{
						-halfWidth, halfHeight, 0f,
			            -halfWidth, -halfHeight, 0f,
						halfWidth, halfHeight, 0f,
			            halfWidth, -halfHeight, 0f
				},
				new float[]{
						texX, texY,
						texX + size, texY,
						texX, texY + size,
						texX + size, texY + size,
				},
				sheetID
		);
		
	}
	
	
	
}

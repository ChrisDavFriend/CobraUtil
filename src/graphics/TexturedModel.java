package graphics;


public class TexturedModel extends Model {
	
	float[] textureCoords;
	int sheetID;
	
	public TexturedModel(float[] vertices, float[] textureCoords, int sheetID){
		super(vertices);
		this.textureCoords = textureCoords;
		this.sheetID = sheetID;
	}

	
	public float[] getTextureCoords(){
		return textureCoords;
	}
	
	public int getTextureSheetID(){
		return sheetID;
	}
}

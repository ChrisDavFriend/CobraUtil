package data;

import graphics.Render;
import graphics.TexturedModel;

public class ModelSet {
	
	TexturedModel[] models;
	private float[] x, y, z;
	private float[] rotX, rotY, rotZ;
	private float uniRotX, uniRotY, uniRotZ;
	
	public ModelSet(TexturedModel... models){
		this.models = models;
		x = new float[models.length];
		y = new float[models.length];
		z = new float[models.length];
		rotX = new float[models.length];
		rotY = new float[models.length];
		rotZ = new float[models.length];
	}
	
	public void render(float uniX, float uniY, float uniZ){
		for(int i = 0; i < models.length; i++){
			Render.requestDrawTexturedModel(x[i] + uniX, y[i] - uniY, z[i] + uniZ, rotX[i] + uniRotX, rotY[i] + uniRotY, rotZ[i] + uniRotZ, models[i]);
		}
	}
	

	public float getX(int pos) {
		return x[pos];
	}

	public void setX(int pos, float x) {
		this.x[pos] = x;
	}

	public float getY(int pos) {
		return y[pos];
	}

	public void setY(int pos, float y) {
		this.y[pos] = y;
	}

	public float getZ(int pos) {
		return z[pos];
	}

	public void setZ(int pos, float z) {
		this.z[pos] = z;
	}

	public float getRotX(int mNumber) {
		return rotX[mNumber];
	}

	public void setRotX(int mNumber, float rotX) {
		this.rotX[mNumber] = rotX;
	}
	
	public float getRotY(int mNumber) {
		return rotY[mNumber];
	}

	public void setRotY(int mNumber, float rotY) {
		this.rotY[mNumber] = rotY;
	}
	
	public float getRotZ(int mNumber) {
		return rotZ[mNumber];
	}

	public void setRotZ(int mNumber, float rotZ) {
		this.rotZ[mNumber] = rotZ;
	}
	
	public void setUniversalRotX(float rotX){
		uniRotX = rotX;
	}
	
	public void setUniversalRotY(float rotY){
		uniRotY = rotY;
	}
	
	public void setUniversalRotZ(float rotZ){
		uniRotZ = rotZ;
	}

}

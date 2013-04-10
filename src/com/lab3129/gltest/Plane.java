package com.lab3129.gltest;


public class Plane extends Mesh {
	private float size;

	public Plane(float size) {
		super();
		this.size = size;
		float s = this.size / 2;
		float vertices[] = { -s, s, 0, -s, -s, 0, s, -s, 0, s, s, 0 };
		float textureVerts[] = {0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f};
		short indices[] = { 0, 1, 2, 0, 2, 3 };
		
		this.setVertices(vertices);
		this.setIndices(indices);
		this.setTextureCoordinates(textureVerts);
		
	}
	

}

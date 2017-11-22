package com.gammarush.engine.graphics.model.quad;

public class Quad {
	private NonTexturedMesh mesh;
	
	public final float WIDTH;
	public final float HEIGHT;
	
	public Quad(float width, float height) {
		this.WIDTH = width;
		this.HEIGHT = height;
		
		float[] vertices = new float[] {
			-width / 2, -height / 2, 0f,
			-width / 2, height / 2, 0f,
			width / 2, height / 2, 0f,
			width / 2, -height / 2, 0f
		};
		
		byte[] indices = new byte[] {
			0, 1, 2,
			2, 3, 0
		};
		
		mesh = new NonTexturedMesh(vertices, indices);
	}
	
	public void render() {
		bind();
		draw();
		unbind();
	}
	
	public void draw() {
		mesh.draw();
	}
	
	public void bind() {
		mesh.bind();
	}
	
	public void unbind() {
		mesh.unbind();
	}
	
	public NonTexturedMesh getMesh() {
		return mesh;
	}
	
}

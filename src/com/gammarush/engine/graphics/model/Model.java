package com.gammarush.engine.graphics.model;

public class Model {
	
	private Mesh mesh;
	private Texture texture = null;
	private Texture normalMap = null;
	private TextureArray blendMap = null;
	
	public final float WIDTH;
	public final float HEIGHT;
	
	public Model(float width, float height) {
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
				
		float[] textureCoordinates = new float[] {
			0, 0,
			0, 1,
			1, 1,
			1, 0
		};
		
		mesh = new Mesh(vertices, indices, textureCoordinates);
	}
	
	public Model(float width, float height, Texture texture) {
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
				
		float[] textureCoordinates = new float[] {
			0, 0,
			0, 1,
			1, 1,
			1, 0
		};
		
		mesh = new Mesh(vertices, indices, textureCoordinates);
		
		this.texture = texture;
	}
	
	public Model(float width, float height, Texture texture, Texture normalMap) {
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
				
		float[] textureCoordinates = new float[] {
			0, 0,
			0, 1,
			1, 1,
			1, 0
		};
		
		mesh = new Mesh(vertices, indices, textureCoordinates);
		
		this.texture = texture;
		this.normalMap = normalMap;
	}
	
	public Model(float width, float height, Texture texture, Texture normalMap, TextureArray blendMap) {
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
				
		float[] textureCoordinates = new float[] {
			0, 0,
			0, 1,
			1, 1,
			1, 0
		};
		
		mesh = new Mesh(vertices, indices, textureCoordinates);
		
		this.texture = texture;
		this.normalMap = normalMap;
		this.blendMap = blendMap;
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
		texture.bind();
	}
	
	public void unbind() {
		mesh.unbind();
		texture.unbind();
	}
	
	public Mesh getMesh() {
		return mesh;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public Texture getNormalMap() {
		return normalMap;
	}
	
	public TextureArray getBlendMap() {
		return blendMap;
	}

}

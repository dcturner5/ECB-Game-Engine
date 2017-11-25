package com.gammarush.engine.tiles;

import java.util.ArrayList;

import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.model.TextureArray;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.graphics.model.Texture;
import com.gammarush.engine.math.matrix.Matrix4f;
import com.gammarush.engine.math.vector.Vector2i;
import com.gammarush.engine.math.vector.Vector3f;

public class Tile {
	public static final int DEFAULT = 0;
	
	public static final int WATER = 1;
	
	public static final int TREE = 2;
	
	public static final int PLAINS_GROUND = 8;
	public static final int PLAINS_GROUND_ACCENT = PLAINS_GROUND + 1;
	public static final int PLAINS_ROAD = PLAINS_GROUND + 2;
	
	public static final int FOREST_GROUND = 16;
	public static final int FOREST_GROUND_ACCENT = FOREST_GROUND + 1;
	public static final int FOREST_ROAD = FOREST_GROUND + 2;
	
	public static final int DESERT_GROUND = 24;
	public static final int DESERT_GROUND_ACCENT = DESERT_GROUND + 1;
	public static final int DESERT_ROAD = DESERT_GROUND + 2;
	
	public static final int CLIFF = 64;
	public static final int CLIFF_UP = CLIFF + 1;
	public static final int CLIFF_DOWN = CLIFF + 2;
	public static final int CLIFF_LEFT = CLIFF + 3;
	public static final int CLIFF_RIGHT = CLIFF + 4;
	public static final int CLIFF_UPLEFT = CLIFF + 5;
	public static final int CLIFF_UPRIGHT = CLIFF + 6;
	public static final int CLIFF_DOWNLEFT = CLIFF + 7;
	public static final int CLIFF_DOWNRIGHT = CLIFF + 8;
	public static final int CLIFF_UPLEFT_IN = CLIFF + 9;
	public static final int CLIFF_UPRIGHT_IN = CLIFF + 10;
	public static final int CLIFF_DOWNLEFT_IN = CLIFF + 11;
	public static final int CLIFF_DOWNRIGHT_IN = CLIFF + 12;
	
	public static final int WIDTH = 16 * Renderer.SCALE;
	public static final int HEIGHT = 16 * Renderer.SCALE;
	
	public Model model;
	
	public boolean solid;
	
	public int blendType;
	public Vector2i blendWeight = null;
	
	public static final int BLEND_TYPE_NEUTRAL = 0;
	public static final int BLEND_TYPE_RECESSIVE = 1;
	public static final int BLEND_TYPE_DOMINANT = 2;
	
	public static final int TEXTURE_LOCATION = 0;
	public static final int NORMAL_MAP_LOCATION = 1;
	public static final int BLEND_MAP_MASK_LOCATION = 2;
	public static final int BLEND_MAP_TEXTURE_LOCATION = 3;
	
	public static final Texture DEFAULT_NORMAL_MAP = new Texture("res/tiles/normal/default.png");
	public static final TextureArray DEFAULT_BLEND_MAP = new TextureArray("res/tiles/blend/default.png", 8);
	
	public Tile(Texture texture, boolean solid, int blendType) {
		this.model = new Model(WIDTH, HEIGHT, texture, DEFAULT_NORMAL_MAP, DEFAULT_BLEND_MAP);
		this.solid = solid;
		this.blendType = blendType;
	}
	
	public Tile(Texture texture, Texture normalMap, boolean solid, int blendType) {
		this.model = new Model(WIDTH, HEIGHT, texture, normalMap, DEFAULT_BLEND_MAP);
		this.solid = solid;
		this.blendType = blendType;
	}
	
	public Tile(Texture texture, TextureArray blendMap, boolean solid, int blendType) {
		this.model = new Model(WIDTH, HEIGHT, texture, DEFAULT_NORMAL_MAP, blendMap);
		this.solid = solid;
		this.blendType = blendType;
	}
	
	public Tile(Texture texture, Texture normalMap, TextureArray blendMap, boolean solid, int blendType) {
		this.model = new Model(WIDTH, HEIGHT, texture, normalMap, blendMap);
		this.solid = solid;
		this.blendType = blendType;
	}
	
	public Tile(Texture texture, TextureArray blendMap, Vector2i blendWeight, boolean solid, int blendType) {
		this.model = new Model(WIDTH, HEIGHT, texture, DEFAULT_NORMAL_MAP, blendMap);
		this.solid = solid;
		this.blendType = blendType;
		this.blendWeight = blendWeight;
	}
	
	public Tile(Texture texture, Texture normalMap, TextureArray blendMap, Vector2i blendWeight, boolean solid, int blendType) {
		this.model = new Model(WIDTH, HEIGHT, texture, normalMap, blendMap);
		this.solid = solid;
		this.blendType = blendType;
		this.blendWeight = blendWeight;
	}
	
	public void update() {
		
	}
	
	public void render(ArrayList<Vector3f> positions, Renderer renderer) {
		model.getMesh().bind();
		model.getTexture().bind(TEXTURE_LOCATION);
		model.getNormalMap().bind(NORMAL_MAP_LOCATION);
		
		for(Vector3f p : positions) {
			prepare(p, renderer);
			model.draw();
		}
		
		model.getMesh().unbind();
		model.getTexture().unbind(TEXTURE_LOCATION);
		model.getNormalMap().unbind(NORMAL_MAP_LOCATION);
	}
	
	public void render(ArrayList<Vector3f> positions, ArrayList<BlendData> blendDatas, Renderer renderer) {
		model.getMesh().bind();
		model.getTexture().bind(TEXTURE_LOCATION);
		model.getBlendMap().bind(BLEND_MAP_MASK_LOCATION);
		model.getNormalMap().bind(NORMAL_MAP_LOCATION);
		
		for(int i = 0; i < positions.size(); i++) {
			BlendData blendData = blendDatas.get(i);
			prepare(positions.get(i), blendData.indices);
			if(blendData.tile == null) continue;
			blendData.tile.model.getTexture().bind(BLEND_MAP_TEXTURE_LOCATION);
			model.draw();
			blendData.tile.model.getTexture().unbind(BLEND_MAP_TEXTURE_LOCATION);
		}
		
		model.getMesh().unbind();
		model.getTexture().unbind(TEXTURE_LOCATION);
		model.getNormalMap().unbind(NORMAL_MAP_LOCATION);
		model.getBlendMap().unbind(BLEND_MAP_MASK_LOCATION);
	}
	
	public void prepare(Vector3f position, Renderer renderer) {
		Renderer.TILE.setUniformMat4f("ml_matrix", Matrix4f.translate(position.add(new Vector3f(WIDTH / 2, HEIGHT / 2, 0))));
	}
	
	public void prepare(Vector3f position, int[] blendIndices) {
		Renderer.TILE.setUniformMat4f("ml_matrix", Matrix4f.translate(position.add(new Vector3f(WIDTH / 2, HEIGHT / 2, 0))));
		Renderer.TILE.setUniform1iv("blend_indices", blendIndices);
	}
}

package com.gammarush.engine.tiles;

import java.util.ArrayList;

import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.model.TextureArray;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.graphics.model.Texture;
import com.gammarush.engine.math.matrix.Matrix4f;
import com.gammarush.engine.math.vector.Vector2i;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.utils.json.JSON;

public class Tile {
	
	public static final int BLEND_TYPE_NEUTRAL = 0;
	public static final int BLEND_TYPE_RECESSIVE = 1;
	public static final int BLEND_TYPE_DOMINANT = 2;
	
	public static final int TEXTURE_LOCATION = 0;
	public static final int NORMAL_MAP_LOCATION = 1;
	public static final int BLEND_MAP_MASK_LOCATION = 2;
	public static final int BLEND_MAP_TEXTURE_LOCATION = 3;
	
	public static final Texture DEFAULT_NORMAL_MAP = new Texture("res/tiles/normal/default.png");
	public static final TextureArray DEFAULT_BLEND_MAP = new TextureArray("res/tiles/blend/default.png", 8);
	
	public static final int WIDTH = 16 * Renderer.SCALE;
	public static final int HEIGHT = 16 * Renderer.SCALE;
	
	private int id;
	private String name;
	private boolean solid;
	private Model model;
	
	private int blendType;
	private Vector2i blendWeight;
	
	public Tile(int id, JSON json) {
		this.id = id;
		this.name = (String) json.getJSON("name");
		this.solid = (boolean) json.getJSON("solid");
		
		Texture texture = new Texture((String) json.getJSON("texture"));
		Texture normalMap = json.getJSON("normalMap") != null ? new Texture((String) json.getJSON("normalMap")) : DEFAULT_NORMAL_MAP;
		TextureArray blendMap = json.getJSON("blendMap") != null ? new TextureArray((String) json.getJSON("blendMap"), 8) : DEFAULT_BLEND_MAP;
		this.model = new Model(WIDTH, HEIGHT, texture, normalMap, blendMap);
		
		this.blendType = json.getJSON("blendType") != null ? (int) json.getJSON("blendType") : BLEND_TYPE_NEUTRAL;
		this.blendWeight = json.getJSON("blendWeight") != null ? new Vector2i((int) json.getJSON("blendWeight.x"), (int) json.getJSON("blendWeight.y")) : null;
	}
	
	public void update() {
		
	}
	
	public void render(ArrayList<Vector3f> positions) {
		model.getMesh().bind();
		model.getTexture().bind(TEXTURE_LOCATION);
		model.getNormalMap().bind(NORMAL_MAP_LOCATION);
		
		for(Vector3f p : positions) {
			prepare(p);
			model.draw();
		}
		
		model.getMesh().unbind();
		model.getTexture().unbind(TEXTURE_LOCATION);
		model.getNormalMap().unbind(NORMAL_MAP_LOCATION);
	}
	
	public void render(ArrayList<Vector3f> positions, ArrayList<BlendData> blendDatas) {
		model.getMesh().bind();
		model.getTexture().bind(TEXTURE_LOCATION);
		model.getBlendMap().bind(BLEND_MAP_MASK_LOCATION);
		model.getNormalMap().bind(NORMAL_MAP_LOCATION);
		
		for(int i = 0; i < positions.size(); i++) {
			BlendData blendData = blendDatas.get(i);
			prepare(positions.get(i), blendData.indices);
			if(blendData.tile == null) continue;
			//blendData.tile.model.getTexture().bind(BLEND_MAP_TEXTURE_LOCATION);
			model.draw();
			//blendData.tile.model.getTexture().unbind(BLEND_MAP_TEXTURE_LOCATION);
		}
		
		model.getMesh().unbind();
		model.getTexture().unbind(TEXTURE_LOCATION);
		model.getNormalMap().unbind(NORMAL_MAP_LOCATION);
		model.getBlendMap().unbind(BLEND_MAP_MASK_LOCATION);
	}
	
	public void prepare(Vector3f position) {
		Renderer.TILE.setUniformMat4f("ml_matrix", Matrix4f.translate(position.add(new Vector3f(WIDTH / 2, HEIGHT / 2, 0))));
	}
	
	public void prepare(Vector3f position, int[] blendIndices) {
		Renderer.TILE.setUniformMat4f("ml_matrix", Matrix4f.translate(position.add(new Vector3f(WIDTH / 2, HEIGHT / 2, 0))));
		Renderer.TILE.setUniform1iv("blend_indices", blendIndices);
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean getSolid() {
		return solid;
	}

	public int getBlendType() {
		return blendType;
	}
	
	public Vector2i getBlendWeight() {
		return blendWeight;
	}
}

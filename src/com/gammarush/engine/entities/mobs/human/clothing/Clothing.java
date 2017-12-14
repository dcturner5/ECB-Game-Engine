package com.gammarush.engine.entities.mobs.human.clothing;

import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.mobs.animations.AnimationData;
import com.gammarush.engine.entities.mobs.human.Human;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.graphics.model.Texture;
import com.gammarush.engine.graphics.model.TextureArray;
import com.gammarush.engine.math.matrix.Matrix4f;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.utils.json.JSON;

public class Clothing {
	
	public static final int CLOTHING_TYPE_HAIR = 0;
	public static final int CLOTHING_TYPE_HEAD = 1;
	public static final int CLOTHING_TYPE_BODY = 2;
	
	public static final int CLOTHING_POOL_COMMON = 0;
	public static final int CLOTHING_POOL_RARE = 1;
	
	public static final int WIDTH = 16 * Renderer.SCALE;
	public static final int HEIGHT = 16 * Renderer.SCALE;
	
	private int id;
	private String name;
	private int type;
	private int layer;
	private int pool;
	
	public Model model;
	
	public Clothing(JSON json) {
		this.id = (int) json.getJSON("id");
		this.name = (String) json.getJSON("name");
		
		String rawType = (String) json.getJSON("type");
		if(rawType.equals("hair")) {
			this.type = CLOTHING_TYPE_HAIR;
			layer = 0;
		}
		else if(rawType.equals("head")) {
			this.type = CLOTHING_TYPE_HEAD;
			layer = 1;
		}
		else if(rawType.equals("body")) {
			this.type = CLOTHING_TYPE_BODY;
			layer = 2;
		}
		
		this.pool = (String) json.getJSON("pool") == "common" ? CLOTHING_POOL_COMMON : CLOTHING_POOL_RARE;
		
		Texture texture = new TextureArray((String) json.getJSON("texture"), 16);
		this.model = new Model(Human.WIDTH, Human.HEIGHT, texture);
	}
	
	public void render(ClothingBatch batch) {
		model.getMesh().bind();
		model.getTexture().bind(Mob.TEXTURE_LOCATION);
		for(int i = 0; i < batch.positions.size(); i++) {
			prepare(batch.positions.get(i), batch.animations.get(i));
			model.draw();
		}
		model.getMesh().unbind();
		model.getTexture().unbind(Mob.TEXTURE_LOCATION);
	}
	
	public void prepare(Vector3f position, AnimationData animation) {
		Renderer.MOB.setUniformMat4f("ml_matrix", Matrix4f.translate(position.add(0, 0, layer * .0001f + .0001f)).multiply(Matrix4f.rotate(0).add(new Vector3f(WIDTH / 2, HEIGHT / 2, 0)))
				.multiply(Matrix4f.scale(new Vector3f(WIDTH / model.WIDTH, HEIGHT / model.HEIGHT, 0))));
		Renderer.MOB.setUniform1i("sprite_index", animation.index + animation.direction * animation.width);
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public int getLayer() {
		return layer;
	}
	
	public int getPool() {
		return pool;
	}
	
	public int getType() {
		return type;
	}

}

package com.gammarush.engine.entities.items;

import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.entities.EntityTemplate;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.graphics.model.Texture;
import com.gammarush.engine.math.matrix.Matrix4f;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.utils.json.JSON;

public class ItemTemplate extends EntityTemplate {
	
	public static final int POOL_COMMON = 0;
	public static final int POOL_RARE = 1;
	
	public static final int WIDTH = 16 * Renderer.SCALE;
	public static final int HEIGHT = 16 * Renderer.SCALE;

	private int pool;
	
	public Model model;
	
	public ItemTemplate(int id, JSON json) {
		super(id, json);
		
		String pool = json.getString("pool");
		if(pool.equals("common")) {
			this.pool = POOL_COMMON;
		}
		else if(pool.equals("rare")) {
			this.pool = POOL_RARE;
		}
		
		Texture texture = new Texture("res/entities/items/" + json.getString("name") + ".png");
		this.model = new Model(texture);
	}
	
	public void render(ItemBatch batch) {
		model.getMesh().bind();
		model.getTexture().bind(Entity.TEXTURE_LOCATION);
		for(int i = 0; i < batch.positions.size(); i++) {
			prepare(batch.positions.get(i), batch.offsets.get(i));
			model.draw();
		}
		model.getMesh().unbind();
		model.getTexture().unbind(Entity.TEXTURE_LOCATION);
	}
	
	public void prepare(Vector3f position, Vector2f offset) {
		Renderer.DEFAULT.setUniformMat4f("ml_matrix", Matrix4f.translate(position.add(offset.x, offset.y, 0)).multiply(Matrix4f.rotate(0).add(new Vector3f(WIDTH / 2, HEIGHT / 2, 0)))
				.multiply(Matrix4f.scale(new Vector3f(WIDTH, HEIGHT, 0))));
	}
	
	public int getPool() {
		return pool;
	}

}

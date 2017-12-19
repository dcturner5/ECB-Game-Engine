package com.gammarush.engine.entities.items;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.math.matrix.Matrix4f;
import com.gammarush.engine.math.vector.Vector3f;

public class WorldItem extends Entity {
	
	private Item item;
	private float time = 0;
	private float offset = 0;

	public WorldItem(Item item, Vector3f position, Game game) {
		super(position, Item.WIDTH, Item.HEIGHT, item.model, game);
		this.item = item;
	}
	
	@Override
	public void update(double delta) {
		time += .05;
		if(time >= 6.28) time = 0;
		offset = (float) Math.abs(Math.sin(time) * 4);
	}
	
	@Override
	public void render() {
		
	}
	
	@Override
	public void prepare() {
		Renderer.DEFAULT.setUniformMat4f("ml_matrix", Matrix4f.translate(position.add(0, offset, 0)).multiply(Matrix4f.rotate(rotation).add(new Vector3f(width / 2, height / 2, 0)))
				.multiply(Matrix4f.scale(new Vector3f(width / model.WIDTH, height / model.HEIGHT, 0))));
	}
	
	public Item getItem() {
		return item;
	}

}

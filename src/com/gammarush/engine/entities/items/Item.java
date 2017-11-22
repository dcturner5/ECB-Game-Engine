package com.gammarush.engine.entities.items;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.math.matrix.Matrix4f;
import com.gammarush.engine.math.vector.Vector3f;

public class Item extends Entity {
	
	public static final int WOOD = 1;
	public static final int STONE = 2;
	
	public int id;
	private float time = 0;
	private float offset = 0;

	public Item(int id, Vector3f position, int width, int height, Model model, Game game) {
		super(position, width, height, model, game);
		position.z = Renderer.ENTITY_LAYER + getTilePosition().y / game.world.height;
		
		this.id = id;
	}
	
	@Override
	public void update(double delta) {
		time += .05;
		if(time >= 6.28) time = 0;
		offset = (float) Math.abs(Math.sin(time) * 4);
	}
	
	@Override
	public void prepare() {
		Renderer.DEFAULT.setUniformMat4f("ml_matrix", Matrix4f.translate(position.add(0, offset, 0)).multiply(Matrix4f.rotate(rotation).add(new Vector3f(width / 2, height / 2, 0)))
				.multiply(Matrix4f.scale(new Vector3f(width / model.WIDTH, height / model.HEIGHT, 0))));
	}
	
	public static ItemData getItemData(int type) {
		Model model = null;
		
		switch(type) {
		case WOOD:
			model = Wood.MODEL;
			break;
		}
		
		return new ItemData(model);
	}

}

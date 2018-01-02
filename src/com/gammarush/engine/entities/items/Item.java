package com.gammarush.engine.entities.items;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.entities.components.PhysicsComponent;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector3f;

public class Item extends Entity {
	
	private ItemTemplate template;
	private float time = 0;
	private Vector2f offset = new Vector2f();

	public Item(ItemTemplate template, Vector3f position, Game game) {
		super(position, ItemTemplate.WIDTH, ItemTemplate.HEIGHT, template.model, game);
		this.template = template;
		
		setSolid(false);
		addComponent(new PhysicsComponent(this));
	}
	
	@Override
	public void update(double delta) {
		super.update(delta);
		
		time += .05;
		if(time >= 6.28) time = 0;
		offset.y = (float) Math.abs(Math.sin(time) * 4);
	}
	
	public ItemTemplate getTemplate() {
		return template;
	}

	public Vector2f getOffset() {
		return offset;
	}

}

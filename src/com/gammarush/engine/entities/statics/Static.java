package com.gammarush.engine.entities.statics;

import com.gammarush.engine.entities.Color;
import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.world.World;

public class Static extends Entity {
	
	private StaticTemplate template;
	private Color color;
	
	public Static(StaticTemplate template, Vector2f position) {
		super(new Vector3f(position.x, position.y, Renderer.ENTITY_LAYER + 1 + template.getZIndex()), template.getWidth(), template.getHeight(), template.getModel());
		this.template = template;
		setSolid(template.getSolid());
		setCollisionBox(template.getCollisionBox());
	}
	
	public Static(StaticTemplate template, Vector2f position, World world) {
		super(new Vector3f(position.x, position.y, Renderer.ENTITY_LAYER + 1 + template.getZIndex()), template.getWidth(), template.getHeight(), template.getModel());
		this.template = template;
		setSolid(template.getSolid());
		setCollisionBox(template.getCollisionBox());
		setWorld(world);
	}
	
	@Override
	public void update(double delta) {
		position.z = Renderer.ENTITY_LAYER + getNormalizedWorldPosition().y + template.getZIndex();
	}
	
	public StaticTemplate getTemplate() {
		return template;
	}
	
	public Color getColor() {
		return color;
	}

}

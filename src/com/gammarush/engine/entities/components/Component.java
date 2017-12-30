package com.gammarush.engine.entities.components;

import com.gammarush.engine.entities.Entity;

public abstract class Component {
	
	private int priority;
	private Entity entity;
	
	public Component(int priority, Entity entity) {
		this.priority = priority;
		this.entity = entity;
	}
	
	public abstract void update(double delta);
	
	public void render() {
		
	}
	
	public int getPriority() {
		return priority;
	}
	
	public Entity getEntity() {
		return entity;
	}

}

package com.gammarush.engine.entities.components;

import com.gammarush.engine.entities.Entity;

public abstract class Component {
	
	private String name;
	private String[] dependencies;
	private int priority;
	private Entity entity;
	
	private boolean permanentlyEnabled = false;
	
	public Component(String name, String[] dependencies, int priority, Entity entity) {
		this.name = name;
		this.dependencies = dependencies;
		this.priority = priority;
		this.entity = entity;
	}
	
	public abstract void update(double delta);
	
	public void render() {}
	
	public String getName() {
		return name;
	}
	
	public String[] getDependencies() {
		return dependencies;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public Entity getEntity() {
		return entity;
	}
	
	public boolean isPermanentlyEnabled() {
		return permanentlyEnabled;
	}
	
	protected void setPermanentlyEnabled(boolean permanentlyEnabled) {
		this.permanentlyEnabled = permanentlyEnabled;
	}

}

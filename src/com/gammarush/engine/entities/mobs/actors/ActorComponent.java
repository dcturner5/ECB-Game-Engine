package com.gammarush.engine.entities.mobs.actors;

import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.entities.mobs.components.MobComponent;

public abstract class ActorComponent extends MobComponent {
	
	private Actor actor;

	public ActorComponent(String name, String[] dependencies, int priority, Entity entity) {
		super(name, dependencies, priority, entity);
		this.actor = (Actor) entity;
	}
	
	public Actor getActor() {
		return actor;
	}

}

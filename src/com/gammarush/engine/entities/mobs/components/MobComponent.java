package com.gammarush.engine.entities.mobs.components;

import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.entities.components.Component;
import com.gammarush.engine.entities.mobs.Mob;

public abstract class MobComponent extends Component {
	
	private Mob mob;

	public MobComponent(String name, String[] dependencies, int priority, Entity entity) {
		super(name, dependencies, priority, entity);
		this.mob = (Mob) entity;
	}
	
	public Mob getMob() {
		return mob;
	}

}

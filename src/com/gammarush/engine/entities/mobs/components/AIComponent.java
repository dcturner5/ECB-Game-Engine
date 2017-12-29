package com.gammarush.engine.entities.mobs.components;

import com.gammarush.engine.entities.Entity;

public class AIComponent extends MobComponent {
	
	public static final int PRIORITY = 0;

	public AIComponent(Entity entity) {
		super(PRIORITY, entity);
	}

	@Override
	public void update(double delta) {
		
	}

}

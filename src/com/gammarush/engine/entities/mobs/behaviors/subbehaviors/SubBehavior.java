package com.gammarush.engine.entities.mobs.behaviors.subbehaviors;

import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.mobs.behaviors.Behavior;

public class SubBehavior {
	
	protected Behavior behavior;
	protected Mob entity;
	
	public boolean complete = false;
	public boolean successful = true;
	
	public SubBehavior(Behavior behavior) {
		this.behavior = behavior;
		this.entity = behavior.entity;
	}

	public void update() {
		
	}

}

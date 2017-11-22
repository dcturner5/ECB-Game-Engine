package com.gammarush.engine.entities.mobs.behaviors.subbehaviors;

import com.gammarush.engine.entities.interactives.Interactive;
import com.gammarush.engine.entities.mobs.behaviors.Behavior;

public class InteractSubBehavior extends SubBehavior {

	public InteractSubBehavior(Behavior behavior) {
		super(behavior);
	}
	
	@Override
	public void update() {
		Interactive e = entity.getInteractive();
		if(e != null) {
			e.activate(entity);
		}
		else {
			successful = false;
		}
		
		complete = true;
	}

}

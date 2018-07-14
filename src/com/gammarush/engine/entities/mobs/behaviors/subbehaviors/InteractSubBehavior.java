package com.gammarush.engine.entities.mobs.behaviors.subbehaviors;

import com.gammarush.engine.entities.Interactive;
import com.gammarush.engine.entities.mobs.behaviors.Behavior;

public class InteractSubBehavior extends SubBehavior {

	public InteractSubBehavior(Behavior behavior) {
		super(behavior);
	}
	
	@Override
	public void update(double delta) {
		Interactive e = getMob().getInteractive();
		if(e != null) {
			e.activate(getMob());
		}
		else {
			successful = false;
		}
		
		complete = true;
	}

}

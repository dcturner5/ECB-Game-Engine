package com.gammarush.engine.entities.mobs.behaviors.subbehaviors;

import com.gammarush.engine.entities.mobs.behaviors.Behavior;
import com.gammarush.engine.utils.Timer;

public class WaitSubBehavior extends SubBehavior {
	
	public Timer timer;

	public WaitSubBehavior(int time, Behavior behavior) {
		super(behavior);
		timer = new Timer(time);
	}
	
	@Override
	public void update() {
		timer.update();
		if(timer.isReady()) {
			complete = true;
		}
	}

}

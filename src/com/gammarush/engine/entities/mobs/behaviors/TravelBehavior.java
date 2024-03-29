package com.gammarush.engine.entities.mobs.behaviors;

import com.gammarush.engine.entities.mobs.behaviors.subbehaviors.SubBehavior;
import com.gammarush.engine.entities.mobs.behaviors.subbehaviors.TravelSubBehavior;
import com.gammarush.engine.entities.mobs.behaviors.subbehaviors.WaitSubBehavior;
import com.gammarush.engine.math.vector.Vector2i;

public class TravelBehavior extends Behavior {
	
	public static final int PRIORITY = 2;
	
	private Vector2i destination;

	public TravelBehavior(Vector2i destination) {
		super(PRIORITY);
		this.destination = destination;
	}
	
	@Override
	public void init() {
		//Use for traveling to interactives
		/*
		queue.add(new TravelSubBehavior(interactivePosition, this));
		queue.add(new InteractSubBehavior(this));
		queue.add(new WaitSubBehavior(300, this));
		*/
		queue.add(new TravelSubBehavior(destination, this));
		queue.add(new WaitSubBehavior(60, this));
	}
	
	@Override
	public void update(double delta) {
		if(!queue.isEmpty()) {
			SubBehavior b = queue.get(0);
			if(!b.getComplete()) b.update(delta);
			else queue.remove(b);
		}
		else {
			//((IdleBehavior) getAIComponent().idle).setOrigin(getMob().getPosition());
			complete = true;
		}
	}

}

package com.gammarush.engine.entities.mobs.behaviors;

import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.mobs.behaviors.subbehaviors.SubBehavior;
import com.gammarush.engine.entities.mobs.behaviors.subbehaviors.TravelSubBehavior;
import com.gammarush.engine.entities.mobs.behaviors.subbehaviors.WaitSubBehavior;
import com.gammarush.engine.math.vector.Vector2i;

public class TravelBehavior extends Behavior {
	
	public static final int PRIORITY = 2;

	public TravelBehavior(Vector2i destination, Mob entity) {
		super(entity, PRIORITY);
		
		//Use for traveling to interactives
		/*
		queue.add(new TravelSubBehavior(interactivePosition, this));
		queue.add(new InteractSubBehavior(this));
		queue.add(new WaitSubBehavior(300, this));
		*/
		queue.add(new TravelSubBehavior(destination, this));
		queue.add(new WaitSubBehavior(300, this));
	
	}
	
	@Override
	public void update() {
		if(!queue.isEmpty()) {
			SubBehavior b = queue.get(0);
			if(!b.complete) b.update();
			else queue.remove(b);
		}
		else {
			((IdleBehavior) entity.idle).setOrigin(entity.getPosition());
			complete = true;
		}
	}

}

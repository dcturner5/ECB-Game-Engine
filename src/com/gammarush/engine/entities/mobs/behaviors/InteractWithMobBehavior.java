package com.gammarush.engine.entities.mobs.behaviors;

import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.mobs.behaviors.subbehaviors.SubBehavior;
import com.gammarush.engine.entities.mobs.behaviors.subbehaviors.TravelSubBehavior;
import com.gammarush.engine.math.vector.Vector2i;

public class InteractWithMobBehavior extends Behavior {
	
	public static final int PRIORITY = 3;
	
	private Mob target;
	
	public InteractWithMobBehavior(Mob target) {
		super(PRIORITY);
		this.target = target;
	}
	
	@Override
	public void init() {
		getMob().setInteractingMob(target);
		target.setInteractingMob(getMob());
		queue.add(new TravelSubBehavior(getTargetPosition(), this));
	}

	@Override
	public void update(double delta) {
		if(!queue.isEmpty()) {
			SubBehavior b = queue.get(0);
			if(!b.getComplete()) b.update(delta);
			else queue.remove(b);
		}
		else {
			if(target.isInteractingWithMob() && target.getInteractingMob().equals(getMob())) updateDirection();
			else complete = true;
		}
	}
	
	public void updateDirection() {
		switch(target.getDirection()) {
		case Mob.DIRECTION_UP:
			getMob().setDirection(Mob.DIRECTION_DOWN);
			break;
		case Mob.DIRECTION_DOWN:
			getMob().setDirection(Mob.DIRECTION_UP);
			break;
		case Mob.DIRECTION_LEFT:
			getMob().setDirection(Mob.DIRECTION_RIGHT);
			break;
		case Mob.DIRECTION_RIGHT:
			getMob().setDirection(Mob.DIRECTION_LEFT);
			break;
		}
	}
	
	public Vector2i getTargetPosition() {
		Vector2i offset = new Vector2i();
		switch(target.getDirection()) {
		case Mob.DIRECTION_UP:
			offset.y = -1;
			break;
		case Mob.DIRECTION_DOWN:
			offset.y = 1;
			break;
		case Mob.DIRECTION_LEFT:
			offset.x = -1;
			break;
		case Mob.DIRECTION_RIGHT:
			offset.x = 1;
			break;
		}
		return target.getTilePosition().add(offset);
	}

}

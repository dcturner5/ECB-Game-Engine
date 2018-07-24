package com.gammarush.engine.entities.mobs.behaviors;

import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.mobs.behaviors.subbehaviors.AttackSubBehavior;
import com.gammarush.engine.entities.mobs.behaviors.subbehaviors.SubBehavior;
import com.gammarush.engine.entities.mobs.behaviors.subbehaviors.TravelSubBehavior;
import com.gammarush.engine.entities.mobs.components.AttackComponent;

public class AttackBehavior extends Behavior {
	
	public static final int PRIORITY = 3;
	
	private Mob target;

	public AttackBehavior(Mob target) {
		super(PRIORITY);
		this.target = target;
	}
	
	@Override
	public void init() {
		queue.add(new TravelSubBehavior(target.getCenterTilePosition(), this));
	}
	
	@Override
	public void update(double delta) {
		if(!queue.isEmpty()) {
			SubBehavior b = queue.get(0);
			if(!b.getComplete()) b.update(delta);
			else queue.remove(b);
		}
		else {
			float distance = getMob().getPosition().sub(target.getPosition()).magnitude();
			AttackComponent ac = (AttackComponent) getMob().getComponent("attack");
			if(distance < ac.getRange()) {
				queue.add(new AttackSubBehavior(this));
			}
			else {
				queue.add(new TravelSubBehavior(target.getTilePosition(), this));
			}
		}
	}

}

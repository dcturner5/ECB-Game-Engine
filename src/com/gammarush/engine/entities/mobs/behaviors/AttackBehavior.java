package com.gammarush.engine.entities.mobs.behaviors;

import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.mobs.behaviors.subbehaviors.AttackSubBehavior;
import com.gammarush.engine.entities.mobs.behaviors.subbehaviors.SubBehavior;
import com.gammarush.engine.entities.mobs.behaviors.subbehaviors.TravelSubBehavior;
import com.gammarush.engine.entities.mobs.components.AttackComponent;

public class AttackBehavior extends Behavior {
	
	public static final int PRIORITY = 3;
	
	private Mob target;
	private AttackSubBehavior attackSubBehavior;

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
			float dx = getMob().getPosition().x - target.getPosition().x;
			float dy = getMob().getPosition().y - target.getPosition().y;
			
			float distance; int direction;
			if(Math.abs(dx) > Math.abs(dy)) {
				distance = Math.abs(dx);
				direction = dx < 0 ? Mob.DIRECTION_RIGHT : Mob.DIRECTION_LEFT;
			}
			else {
				distance = Math.abs(dy);
				direction = dy < 0 ? Mob.DIRECTION_DOWN : Mob.DIRECTION_UP;
			}
			
			AttackComponent ac = (AttackComponent) getMob().getComponent("attack");
			if(ac != null && distance <= ac.getRange()) {
				getMob().setDirection(direction);
				
				attackSubBehavior = new AttackSubBehavior(this);
				queue.add(attackSubBehavior);
			}
			else {
				if(attackSubBehavior != null && attackSubBehavior.getComplete()) {
					//System.out.println("ATTACK HIT: " + attackSubBehavior.getSuccessful());
					attackSubBehavior = null;
				}
				queue.add(new TravelSubBehavior(target.getTilePosition(), this));
			}
		}
	}

}

package com.gammarush.engine.entities.mobs.behaviors.subbehaviors;

import com.gammarush.engine.entities.mobs.behaviors.Behavior;
import com.gammarush.engine.entities.mobs.components.AttackComponent;

public class AttackSubBehavior extends SubBehavior {

	public AttackSubBehavior(Behavior behavior) {
		super(behavior);
	}

	@Override
	public void update(double delta) {
		AttackComponent ac = (AttackComponent) getMob().getComponent("attack");
		successful = ac.attack();
		complete = true;
	}

}

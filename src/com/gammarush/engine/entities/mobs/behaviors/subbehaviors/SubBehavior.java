package com.gammarush.engine.entities.mobs.behaviors.subbehaviors;

import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.mobs.behaviors.Behavior;
import com.gammarush.engine.entities.mobs.components.AIComponent;

public abstract class SubBehavior {
	
	private Behavior behavior;
	private AIComponent aiComponent;
	protected boolean complete = false;
	protected boolean successful = true;
	
	public SubBehavior(Behavior behavior) {
		this.behavior = behavior;
		this.aiComponent = behavior.getAIComponent();
	}

	public abstract void update(double delta);
	
	public Behavior getBehavior() {
		return behavior;
	}
	public AIComponent getAIComponent() {
		return aiComponent;
	}
	
	public Mob getMob() {
		return aiComponent.getMob();
	}
	
	public boolean getComplete() {
		return complete;
	}
	
	public boolean getSuccessful() {
		return successful;
	}

}

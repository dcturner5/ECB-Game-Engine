package com.gammarush.engine.entities.mobs.behaviors;

import java.util.ArrayList;

import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.mobs.behaviors.subbehaviors.SubBehavior;
import com.gammarush.engine.entities.mobs.components.AIComponent;

public abstract class Behavior {
	
	private int priority;
	
	private AIComponent aiComponent;
	protected boolean complete = false;
	protected ArrayList<SubBehavior> queue = new ArrayList<SubBehavior>();
	
	public Behavior(int priority, AIComponent aiComponent) {
		this.priority = priority;
		this.aiComponent = aiComponent;
	}
	
	public abstract void update(double delta);
	
	public int getPriority() {
		return priority;
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
	
}

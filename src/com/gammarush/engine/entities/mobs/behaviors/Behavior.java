package com.gammarush.engine.entities.mobs.behaviors;

import java.util.ArrayList;

import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.mobs.behaviors.subbehaviors.SubBehavior;

public class Behavior {
	
	public Mob entity;
	public ArrayList<SubBehavior> queue = new ArrayList<SubBehavior>();
	public int priority;
	public boolean complete = false;
	
	public Behavior(Mob entity, int priority) {
		this.entity = entity;
		this.priority = priority;
	}
	
	public void update() {
		
	}
	
}

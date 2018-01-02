package com.gammarush.engine.entities.mobs.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.gammarush.engine.astar.AStar;
import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.entities.mobs.behaviors.Behavior;
import com.gammarush.engine.entities.mobs.behaviors.IdleBehavior;

public class AIComponent extends MobComponent {
	
	public static final String NAME = "ai";
	public static final String[] DEPENDENCIES = new String[]{"physics"};
	public static final int PRIORITY = 0;
	
	private AStar astar;
	private ArrayList<Behavior> behaviors = new ArrayList<Behavior>();

	public AIComponent(Entity entity) {
		super(NAME, DEPENDENCIES, PRIORITY, entity);
		astar = new AStar(entity.getWorld());
		addBehavior(new IdleBehavior(this));
	}

	@Override
	public void update(double delta) {
		if(!behaviors.isEmpty()) {
			Behavior b = behaviors.get(0);
			if(!b.getComplete()) {
				b.update(delta);
			}
			else {
				removeBehavior(b);
			}
		}
	}
	
	public boolean addBehavior(Behavior behavior) {
		boolean success = behaviors.add(behavior);
		Collections.sort(behaviors, new Comparator<Behavior>() {
			@Override
			public int compare(Behavior b1, Behavior b2) {
				if(b1.getPriority() > b2.getPriority()) return -1;
				if(b1.getPriority() < b2.getPriority()) return 1;
				return 0;
			}
		});
		return success;
	}
	
	public boolean removeBehavior(Behavior behavior) {
		boolean success = behaviors.remove(behavior);
		Collections.sort(behaviors, new Comparator<Behavior>() {
			@Override
			public int compare(Behavior b1, Behavior b2) {
				if(b1.getPriority() > b2.getPriority()) return -1;
				if(b1.getPriority() < b2.getPriority()) return 1;
				return 0;
			}
		});
		return success;
	}
	
	public AStar getAStar() {
		return astar;
	}

}

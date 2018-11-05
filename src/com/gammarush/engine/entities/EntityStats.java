package com.gammarush.engine.entities;

import java.util.HashMap;

public class EntityStats {
	
	private HashMap<String, Integer> stats = new HashMap<String, Integer>();
	
	public EntityStats() {
		stats = new HashMap<String, Integer>();
	}
	
	public int getStat(String name) {
		if(stats.containsKey(name)) {
			return stats.get(name);
		}
		return 0;
	}
	
	protected void setStat(String name, int value) {
		stats.put(name, value);
	}
	
}

package com.gammarush.engine.entities.mobs.actors;

import java.util.ArrayList;
import java.util.HashMap;

public class ActorHashMap {
	
	private HashMap<Integer, Actor> idMap = new HashMap<Integer, Actor>();
	private HashMap<String, Actor> nameMap = new HashMap<String, Actor>();
	private ArrayList<Actor> array = new ArrayList<Actor>();
	
	public Actor get(int id) {
		return idMap.get(id);
	}
	
	public Actor get(String name) {
		return nameMap.get(name);
	}
	
	public int getId(String name) {
		Actor e = nameMap.get(name);
		if(e == null) return -1;
		return e.getId();
	}
	
	public void put(Actor e) {
		idMap.put(e.getId(), e);
		nameMap.put(e.getName(), e);
		array.add(e);
	}
	
	public Actor getRandom() {
		return array.get((int) (Math.random() * array.size()));
	}

}

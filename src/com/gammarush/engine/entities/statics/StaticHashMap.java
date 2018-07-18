package com.gammarush.engine.entities.statics;

import java.util.ArrayList;
import java.util.HashMap;

public class StaticHashMap {
	
	private HashMap<Integer, StaticTemplate> idMap = new HashMap<Integer, StaticTemplate>();
	private HashMap<String, StaticTemplate> nameMap = new HashMap<String, StaticTemplate>();
	private ArrayList<StaticTemplate> array = new ArrayList<StaticTemplate>();
	
	public StaticTemplate get(int id) {
		return idMap.get(id);
	}
	
	public StaticTemplate get(String name) {
		return nameMap.get(name);
	}
	
	public int getId(String name) {
		StaticTemplate e = nameMap.get(name);
		if(e == null) return -1;
		return e.getId();
	}
	
	public void put(StaticTemplate e) {
		idMap.put(e.getId(), e);
		nameMap.put(e.getName(), e);
		array.add(e);
	}
	
	public StaticTemplate getRandom() {
		return array.get((int) (Math.random() * array.size()));
	}

}

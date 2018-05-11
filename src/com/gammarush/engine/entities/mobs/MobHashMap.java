package com.gammarush.engine.entities.mobs;

import java.util.ArrayList;
import java.util.HashMap;

public class MobHashMap {
	
	private HashMap<Integer, MobTemplate> idMap = new HashMap<Integer, MobTemplate>();
	private HashMap<String, MobTemplate> nameMap = new HashMap<String, MobTemplate>();
	private ArrayList<MobTemplate> array = new ArrayList<MobTemplate>();
	
	public MobTemplate get(int id) {
		return idMap.get(id);
	}
	
	public MobTemplate get(String name) {
		return nameMap.get(name);
	}
	
	public int getId(String name) {
		MobTemplate e = nameMap.get(name);
		if(e == null) return -1;
		return e.getId();
	}
	
	public void put(MobTemplate e) {
		idMap.put(e.getId(), e);
		nameMap.put(e.getName(), e);
		array.add(e);
	}
	
	public MobTemplate getRandom() {
		return array.get((int) (Math.random() * array.size()));
	}

}

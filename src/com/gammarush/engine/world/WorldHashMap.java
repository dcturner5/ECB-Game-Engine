package com.gammarush.engine.world;

import java.util.ArrayList;
import java.util.HashMap;

public class WorldHashMap {
	
	private HashMap<Integer, World> idMap = new HashMap<Integer, World>();
	private HashMap<String, World> nameMap = new HashMap<String, World>();
	private ArrayList<World> array = new ArrayList<World>();
	
	public World get(int id) {
		return idMap.get(id);
	}
	
	public World get(String name) {
		return nameMap.get(name);
	}
	
	public int getId(String name) {
		World e = nameMap.get(name);
		if(e == null) return -1;
		return e.getId();
	}
	
	public void put(World e) {
		idMap.put(e.getId(), e);
		nameMap.put(e.getName(), e);
		array.add(e);
	}

}

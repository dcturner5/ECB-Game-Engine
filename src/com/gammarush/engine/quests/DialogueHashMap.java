package com.gammarush.engine.quests;

import java.util.ArrayList;
import java.util.HashMap;

public class DialogueHashMap {
	
	private HashMap<Integer, Dialogue> idMap = new HashMap<Integer, Dialogue>();
	private HashMap<String, Dialogue> nameMap = new HashMap<String, Dialogue>();
	private ArrayList<Dialogue> array = new ArrayList<Dialogue>();
	
	public Dialogue get(int id) {
		return idMap.get(id);
	}
	
	public Dialogue get(String name) {
		return nameMap.get(name);
	}
	
	public int getId(String name) {
		Dialogue e = nameMap.get(name);
		if(e == null) return -1;
		return e.getId();
	}
	
	public void put(Dialogue e) {
		idMap.put(e.getId(), e);
		nameMap.put(e.getName(), e);
		array.add(e);
	}
	
	public Dialogue getRandom() {
		return array.get((int) (Math.random() * array.size()));
	}

}

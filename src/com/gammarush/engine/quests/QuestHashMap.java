package com.gammarush.engine.quests;

import java.util.ArrayList;
import java.util.HashMap;

public class QuestHashMap {
	
	private HashMap<Integer, Quest> idMap = new HashMap<Integer, Quest>();
	private HashMap<String, Quest> nameMap = new HashMap<String, Quest>();
	private ArrayList<Quest> array = new ArrayList<Quest>();
	
	public Quest get(int id) {
		return idMap.get(id);
	}
	
	public Quest get(String name) {
		return nameMap.get(name);
	}
	
	public ArrayList<Quest> getArray() {
		return array;
	}
	
	public int getId(String name) {
		Quest e = nameMap.get(name);
		if(e == null) return -1;
		return e.getId();
	}
	
	public void put(Quest e) {
		idMap.put(e.getId(), e);
		nameMap.put(e.getName(), e);
		array.add(e);
	}
	
	public Quest getRandom() {
		return array.get((int) (Math.random() * array.size()));
	}

}

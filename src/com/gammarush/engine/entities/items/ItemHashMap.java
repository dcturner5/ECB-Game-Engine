package com.gammarush.engine.entities.items;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemHashMap {
	
	private HashMap<Integer, Item> idMap = new HashMap<Integer, Item>();
	private HashMap<String, Item> nameMap = new HashMap<String, Item>();
	private ArrayList<Item> array = new ArrayList<Item>();
	
	public Item get(int id) {
		return idMap.get(id);
	}
	
	public Item get(String name) {
		return nameMap.get(name);
	}
	
	public int getId(String name) {
		Item e = nameMap.get(name);
		if(e == null) return -1;
		return e.getId();
	}
	
	public void put(Item e) {
		idMap.put(e.getId(), e);
		nameMap.put(e.getName(), e);
		array.add(e);
	}
	
	public Item getRandom() {
		return array.get((int) (Math.random() * array.size()));
	}

}

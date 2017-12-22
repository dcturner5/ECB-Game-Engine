package com.gammarush.engine.entities.items;

import java.util.ArrayList;
import java.util.HashMap;

public class ItemHashMap {
	
	private HashMap<Integer, ItemTemplate> idMap = new HashMap<Integer, ItemTemplate>();
	private HashMap<String, ItemTemplate> nameMap = new HashMap<String, ItemTemplate>();
	private ArrayList<ItemTemplate> array = new ArrayList<ItemTemplate>();
	
	public ItemTemplate get(int id) {
		return idMap.get(id);
	}
	
	public ItemTemplate get(String name) {
		return nameMap.get(name);
	}
	
	public int getId(String name) {
		ItemTemplate e = nameMap.get(name);
		if(e == null) return -1;
		return e.getId();
	}
	
	public void put(ItemTemplate e) {
		idMap.put(e.getId(), e);
		nameMap.put(e.getName(), e);
		array.add(e);
	}
	
	public ItemTemplate getRandom() {
		return array.get((int) (Math.random() * array.size()));
	}

}

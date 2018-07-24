package com.gammarush.engine.entities.items.clothing;

import java.util.ArrayList;
import java.util.HashMap;

public class ClothingHashMap {
	
	private HashMap<Integer, ClothingTemplate> idMap = new HashMap<Integer, ClothingTemplate>();
	private HashMap<String, ClothingTemplate> nameMap = new HashMap<String, ClothingTemplate>();
	private ArrayList<ClothingTemplate> array = new ArrayList<ClothingTemplate>();
	
	public ClothingTemplate get(int id) {
		return idMap.get(id);
	}
	
	public ClothingTemplate get(String name) {
		return nameMap.get(name);
	}
	
	public int getId(String name) {
		ClothingTemplate e = nameMap.get(name);
		if(e == null) return -1;
		return e.getId();
	}
	
	public void put(ClothingTemplate e) {
		idMap.put(e.getId(), e);
		nameMap.put(e.getName(), e);
		array.add(e);
	}
	
	public ClothingTemplate getRandom() {
		return array.get((int) (Math.random() * array.size()));
	}
	
	public ClothingTemplate getRandom(String type, int pool) {
		ArrayList<ClothingTemplate> poolArray = new ArrayList<ClothingTemplate>();
		for(ClothingTemplate c : array) {
			if(c.getType().equals(type) && c.getPool() == pool) poolArray.add(c);
		}
		return poolArray.get((int) (Math.random() * poolArray.size()));
	}
	
	public ClothingTemplate getRandomByType(String type) {
		ArrayList<ClothingTemplate> poolArray = new ArrayList<ClothingTemplate>();
		for(ClothingTemplate c : array) {
			if(c.getType().equals(type)) poolArray.add(c);
		}
		return poolArray.get((int) (Math.random() * poolArray.size()));
	}
	
	public ClothingTemplate getRandomByPool(int pool) {
		ArrayList<ClothingTemplate> poolArray = new ArrayList<ClothingTemplate>();
		for(ClothingTemplate c : array) {
			if(c.getPool() == pool) poolArray.add(c);
		}
		return poolArray.get((int) (Math.random() * poolArray.size()));
	}

}

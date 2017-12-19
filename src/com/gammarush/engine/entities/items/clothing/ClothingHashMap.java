package com.gammarush.engine.entities.items.clothing;

import java.util.ArrayList;
import java.util.HashMap;

public class ClothingHashMap {
	
	private HashMap<Integer, Clothing> idMap = new HashMap<Integer, Clothing>();
	private HashMap<String, Clothing> nameMap = new HashMap<String, Clothing>();
	private ArrayList<Clothing> array = new ArrayList<Clothing>();
	
	public Clothing get(int id) {
		return idMap.get(id);
	}
	
	public Clothing get(String name) {
		return nameMap.get(name);
	}
	
	public int getId(String name) {
		Clothing e = nameMap.get(name);
		if(e == null) return -1;
		return e.getId();
	}
	
	public void put(Clothing e) {
		idMap.put(e.getId(), e);
		nameMap.put(e.getName(), e);
		array.add(e);
	}
	
	public Clothing getRandom() {
		return array.get((int) (Math.random() * array.size()));
	}
	
	public Clothing getRandom(int type, int pool) {
		ArrayList<Clothing> poolArray = new ArrayList<Clothing>();
		for(Clothing c : array) {
			if(c.getType() == type && c.getPool() == pool) poolArray.add(c);
		}
		return poolArray.get((int) (Math.random() * poolArray.size()));
	}
	
	public Clothing getRandomByType(int type) {
		ArrayList<Clothing> poolArray = new ArrayList<Clothing>();
		for(Clothing c : array) {
			if(c.getType() == type) poolArray.add(c);
		}
		return poolArray.get((int) (Math.random() * poolArray.size()));
	}
	
	public Clothing getRandomByPool(int pool) {
		ArrayList<Clothing> poolArray = new ArrayList<Clothing>();
		for(Clothing c : array) {
			if(c.getPool() == pool) poolArray.add(c);
		}
		return poolArray.get((int) (Math.random() * poolArray.size()));
	}

}

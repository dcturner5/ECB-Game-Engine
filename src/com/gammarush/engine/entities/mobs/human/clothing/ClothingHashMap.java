package com.gammarush.engine.entities.mobs.human.clothing;

import java.util.HashMap;

public class ClothingHashMap {
	
	private HashMap<Integer, Clothing> idMap = new HashMap<Integer, Clothing>();
	private HashMap<String, Clothing> nameMap = new HashMap<String, Clothing>();
	
	public Clothing get(int id) {
		return idMap.get(id);
	}
	
	public Clothing get(String name) {
		return nameMap.get(name);
	}
	
	public int getId(String name) {
		Clothing clothing = nameMap.get(name);
		if(clothing == null) {
			return -1;
		}
		
		return clothing.getId();
	}
	
	public void put(Clothing clothing) {
		idMap.put(clothing.getId(), clothing);
		nameMap.put(clothing.getName(), clothing);
	}

}

package com.gammarush.engine.entities.animations;

import java.util.ArrayList;
import java.util.HashMap;

public class AnimationHashMap {
	
	private HashMap<String, Animation> nameMap = new HashMap<String, Animation>();
	private ArrayList<Animation> array = new ArrayList<Animation>();
	
	public AnimationHashMap(Animation... animations) {
		for(Animation e : animations) {
			put(e);
		}
	}
	
	public boolean contains(String name) {
		return nameMap.containsKey(name);
	}
	
	public Animation get(int id) {
		return array.get(id);
	}
	
	public Animation get(String name) {
		return nameMap.get(name);
	}
	
	public ArrayList<Animation> getArray() {
		return array;
	}
	
	public void put(Animation e) {
		nameMap.put(e.getName(), e);
		array.add(e);
	}

}

package com.gammarush.engine.scripts;

import java.util.ArrayList;
import java.util.HashMap;

import com.gammarush.axil.AxilScript;

public class ScriptHashMap {
	
	private HashMap<String, AxilScript> nameMap = new HashMap<String, AxilScript>();
	private ArrayList<AxilScript> array = new ArrayList<AxilScript>();
	
	public AxilScript get(int index) {
		return array.get(index);
	}
	
	public AxilScript get(String name) {
		return nameMap.get(name);
	}
	
	public ArrayList<AxilScript> getArray() {
		return array;
	}
	
	public void put(String name, AxilScript e) {
		nameMap.put(name, e);
		array.add(e);
	}
	
	public AxilScript getRandom() {
		return array.get((int) (Math.random() * array.size()));
	}
	
}

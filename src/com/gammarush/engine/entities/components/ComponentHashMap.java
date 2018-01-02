package com.gammarush.engine.entities.components;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class ComponentHashMap {
	
	private HashMap<String, Component> nameMap = new HashMap<String, Component>();
	private ArrayList<Component> array = new ArrayList<Component>();
	
	public boolean contains(String name) {
		return nameMap.containsKey(name);
	}
	
	public Component get(int id) {
		return array.get(id);
	}
	
	public Component get(String name) {
		return nameMap.get(name);
	}
	
	public ArrayList<Component> getArray() {
		return array;
	}
	
	public void put(Component e) {
		boolean success = true;
		String missing = "";
		for(String name : e.getDependencies()) {
			if(!contains(name)) {
				success = false;
				missing = name;
			}
		}
		
		if(success) {
			nameMap.put(e.getName(), e);
			array.add(e);
			sort();
		}
		else {
			System.err.println("Component Dependency Conditions Not Met\n"
					+ "Component Name: " + e.getName() + "\n"
					+ "Missing Dependency: " + missing);
			System.exit(-1);
		}
	}
	
	public void remove(String name) {
		Component e = nameMap.remove(name);
		if(e != null) array.remove(e);
	}
	
	public void sort() {
		Collections.sort(array, new Comparator<Component>() {
			@Override
			public int compare(Component c1, Component c2) {
				if(c1.getPriority() < c2.getPriority()) return -1;
				if(c1.getPriority() > c2.getPriority()) return 1;
				return 0;
			}
		});
	}
	
}

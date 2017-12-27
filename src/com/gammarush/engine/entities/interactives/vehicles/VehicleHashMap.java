package com.gammarush.engine.entities.interactives.vehicles;

import java.util.ArrayList;
import java.util.HashMap;

public class VehicleHashMap {
	
	private HashMap<Integer, VehicleTemplate> idMap = new HashMap<Integer, VehicleTemplate>();
	private HashMap<String, VehicleTemplate> nameMap = new HashMap<String, VehicleTemplate>();
	private ArrayList<VehicleTemplate> array = new ArrayList<VehicleTemplate>();
	
	private HashMap<Integer, WheelTemplate> idWheelMap = new HashMap<Integer, WheelTemplate>();
	private HashMap<String, WheelTemplate> nameWheelMap = new HashMap<String, WheelTemplate>();
	private ArrayList<WheelTemplate> wheelArray = new ArrayList<WheelTemplate>();
	
	public VehicleTemplate get(int id) {
		return idMap.get(id);
	}
	
	public VehicleTemplate get(String name) {
		return nameMap.get(name);
	}
	
	public int getId(String name) {
		VehicleTemplate e = nameMap.get(name);
		if(e == null) return -1;
		return e.getId();
	}
	
	public void put(VehicleTemplate e) {
		idMap.put(e.getId(), e);
		nameMap.put(e.getName(), e);
		array.add(e);
	}
	
	public VehicleTemplate getRandom() {
		return array.get((int) (Math.random() * array.size()));
	}
	
	public WheelTemplate getWheel(int id) {
		return idWheelMap.get(id);
	}
	
	public WheelTemplate getWheel(String name) {
		return nameWheelMap.get(name);
	}
	
	public int getWheelId(String name) {
		WheelTemplate e = nameWheelMap.get(name);
		if(e == null) return -1;
		return e.getId();
	}
	
	public void putWheel(WheelTemplate e) {
		idWheelMap.put(e.getId(), e);
		nameWheelMap.put(e.getName(), e);
		wheelArray.add(e);
	}
	
	public WheelTemplate getRandomWheel() {
		return wheelArray.get((int) (Math.random() * wheelArray.size()));
	}
	
}

package com.gammarush.engine.tiles;

import java.util.HashMap;

public class TileHashMap {
	
	private HashMap<Integer, Tile> idMap = new HashMap<Integer, Tile>();
	private HashMap<String, Tile> nameMap = new HashMap<String, Tile>();
	
	public Tile get(int id) {
		return idMap.get(id);
	}
	
	public Tile get(String name) {
		return nameMap.get(name);
	}
	
	public int getId(String name) {
		Tile tile = nameMap.get(name);
		if(tile == null) {
			return -1;
		}
		
		return tile.getId();
	}
	
	public void put(Tile tile) {
		idMap.put(tile.getId(), tile);
		nameMap.put(tile.getName(), tile);
	}
	
}

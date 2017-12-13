package com.gammarush.engine.tiles;

import java.util.HashMap;

public class TileHashMap {
	
	private HashMap<Integer, Tile> tileIdMap = new HashMap<Integer, Tile>();
	private HashMap<String, Tile> tileNameMap = new HashMap<String, Tile>();
	
	public Tile get(int id) {
		return tileIdMap.get(id);
	}
	
	public Tile get(String name) {
		return tileNameMap.get(name);
	}
	
	public int getId(String name) {
		Tile tile = tileNameMap.get(name);
		if(tile == null) {
			return -1;
		}
		
		return tile.getId();
	}
	
	public void put(Tile tile) {
		tileIdMap.put(tile.getId(), tile);
		tileNameMap.put(tile.getName(), tile);
	}
	
}

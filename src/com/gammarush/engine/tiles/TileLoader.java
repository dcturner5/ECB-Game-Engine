package com.gammarush.engine.tiles;

import java.util.ArrayList;
import java.util.HashMap;

import com.gammarush.engine.utils.json.JSON;
import com.gammarush.engine.utils.json.JSONLoader;

public class TileLoader {

	public static TileHashMap load(String path) {
		TileHashMap result = new TileHashMap();
		JSON json = JSONLoader.load(path);
		
		@SuppressWarnings("unchecked")
		ArrayList<JSON> tiles = (ArrayList<JSON>) json.getJSON("tiles");
		for(JSON tile : tiles) {
			result.put(new Tile(tile));
		}
		
		return result;
	}
	
	public static HashMap<String, Tile> loadByName(String path) {
		HashMap<String, Tile> result = new HashMap<String, Tile>();
		JSON json = JSONLoader.load(path);
		
		@SuppressWarnings("unchecked")
		ArrayList<JSON> tiles = (ArrayList<JSON>) json.getJSON("tiles");
		for(JSON tile : tiles) {
			String name = (String) tile.get("name");
			result.put(name, new Tile(tile));
		}
		
		return result;
	}

}

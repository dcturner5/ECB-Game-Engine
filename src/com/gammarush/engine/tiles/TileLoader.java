package com.gammarush.engine.tiles;

import java.util.ArrayList;

import com.gammarush.engine.utils.json.JSON;
import com.gammarush.engine.utils.json.JSONLoader;

public class TileLoader {

	public static TileHashMap load(String path) {
		TileHashMap result = new TileHashMap();
		JSON json = JSONLoader.load(path);
		
		@SuppressWarnings("unchecked")
		ArrayList<JSON> array = (ArrayList<JSON>) json.getJSON("tiles");
		for(JSON element : array) {
			result.put(new Tile(element));
		}
		
		return result;
	}

}

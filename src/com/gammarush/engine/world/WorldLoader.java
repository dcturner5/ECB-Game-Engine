package com.gammarush.engine.world;

import java.util.ArrayList;

import com.gammarush.engine.utils.json.JSON;
import com.gammarush.engine.utils.json.JSONLoader;

public class WorldLoader {
	
	public static WorldHashMap load(String path, WorldManager worldManager) {
		WorldHashMap result = new WorldHashMap();
		JSON json = JSONLoader.load(path);
		
		ArrayList<JSON> array = json.getArray("worlds");
		for(int i = 0; i < array.size(); i++) {
			JSON element = array.get(i);
			result.put(new World(i, element, worldManager));
		}
		
		return result;
	}

}

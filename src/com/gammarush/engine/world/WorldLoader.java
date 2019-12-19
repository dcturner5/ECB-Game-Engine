package com.gammarush.engine.world;

import java.util.ArrayList;

import com.gammarush.engine.utils.json.JSON;
import com.gammarush.engine.utils.json.JSONLoader;

public class WorldLoader {
	
	public static void load(String path, WorldManager worldManager) {
		WorldHashMap worlds = new WorldHashMap();
		JSON json = JSONLoader.load(path);
		
		ArrayList<String> files = json.getStringArray("files");
		for(int i = 0; i < files.size(); i++) {
			JSON worldJson = JSONLoader.load(files.get(i));
			ArrayList<JSON> array = worldJson.getArray("worlds");
			for(int j = 0; j < array.size(); j++) {
				JSON element = array.get(j);
				worlds.put(new World(i, element, worldManager));
			}
		}
		worldManager.setWorldHashMap(worlds);
		worldManager.setWorld(json.getString("default"));
	}

}

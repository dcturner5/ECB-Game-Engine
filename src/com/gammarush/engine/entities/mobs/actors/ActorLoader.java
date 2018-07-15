package com.gammarush.engine.entities.mobs.actors;

import java.util.ArrayList;

import com.gammarush.engine.utils.json.JSON;
import com.gammarush.engine.utils.json.JSONLoader;

public class ActorLoader {
	
	public static ActorHashMap load(String path) {
		ActorHashMap result = new ActorHashMap();
		JSON json = JSONLoader.load(path);
		
		ArrayList<JSON> array = json.getArray("actors");
		for(int i = 0; i < array.size(); i++) {
			JSON element = array.get(i);
			result.put(new Actor(i, element));
		}
		
		return result;
	}

}

package com.gammarush.engine.entities.mobs;

import java.util.ArrayList;

import com.gammarush.engine.utils.json.JSON;
import com.gammarush.engine.utils.json.JSONLoader;

public class MobLoader {
	
	public static MobHashMap load(String path) {
		MobHashMap result = new MobHashMap();
		JSON json = JSONLoader.load(path);
		
		ArrayList<JSON> array = json.getArray("mobs");
		for(int i = 0; i < array.size(); i++) {
			JSON element = array.get(i);
			result.put(new MobTemplate(i, element));
		}
		
		return result;
	}

}

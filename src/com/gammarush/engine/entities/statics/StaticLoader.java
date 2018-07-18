package com.gammarush.engine.entities.statics;

import java.util.ArrayList;

import com.gammarush.engine.utils.json.JSON;
import com.gammarush.engine.utils.json.JSONLoader;

public class StaticLoader {
	
	public static StaticHashMap load(String path) {
		StaticHashMap result = new StaticHashMap();
		JSON json = JSONLoader.load(path);
		
		ArrayList<JSON> array = json.getArray("statics");
		for(int i = 0; i < array.size(); i++) {
			JSON element = array.get(i);
			result.put(new StaticTemplate(i, element));
		}
		
		return result;
	}

}

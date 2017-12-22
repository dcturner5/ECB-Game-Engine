package com.gammarush.engine.entities.items;

import java.util.ArrayList;

import com.gammarush.engine.utils.json.JSON;
import com.gammarush.engine.utils.json.JSONLoader;

public class ItemLoader {
	public static ItemHashMap load(String path) {
		ItemHashMap result = new ItemHashMap();
		JSON json = JSONLoader.load(path);
		
		@SuppressWarnings("unchecked")
		ArrayList<JSON> array = (ArrayList<JSON>) json.getJSON("items");
		for(int i = 0; i < array.size(); i++) {
			JSON element = array.get(i);
			result.put(new ItemTemplate(i, element));
		}
		
		return result;
	}
}

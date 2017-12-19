package com.gammarush.engine.entities.items.clothing;

import java.util.ArrayList;

import com.gammarush.engine.utils.json.JSON;
import com.gammarush.engine.utils.json.JSONLoader;

public class ClothingLoader {
	
	public static ClothingHashMap load(String path) {
		ClothingHashMap result = new ClothingHashMap();
		JSON json = JSONLoader.load(path);
		
		@SuppressWarnings("unchecked")
		ArrayList<JSON> array = (ArrayList<JSON>) json.getJSON("clothing");
		for(int i = 0; i < array.size(); i++) {
			JSON element = array.get(i);
			result.put(new Clothing(i, element));
		}
		
		return result;
	}

}

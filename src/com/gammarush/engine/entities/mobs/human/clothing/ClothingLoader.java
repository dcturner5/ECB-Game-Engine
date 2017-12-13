package com.gammarush.engine.entities.mobs.human.clothing;

import java.util.ArrayList;

import com.gammarush.engine.utils.json.JSON;
import com.gammarush.engine.utils.json.JSONLoader;

public class ClothingLoader {
	
	public static ClothingHashMap load(String path) {
		ClothingHashMap result = new ClothingHashMap();
		JSON json = JSONLoader.load(path);
		
		@SuppressWarnings("unchecked")
		ArrayList<JSON> array = (ArrayList<JSON>) json.getJSON("clothing");
		for(JSON element : array) {
			result.put(new Clothing(element));
		}
		
		return result;
	}

}
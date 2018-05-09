package com.gammarush.engine.entities.animations;

import java.util.ArrayList;

import com.gammarush.engine.utils.json.JSON;
import com.gammarush.engine.utils.json.JSONLoader;

public class AnimationLoader {
	public static AnimationHashMap load(String path) {
		AnimationHashMap result = new AnimationHashMap();
		JSON json = JSONLoader.load(path);
		
		ArrayList<JSON> array = json.getArray("animations");
		for(int i = 0; i < array.size(); i++) {
			JSON element = array.get(i);
			result.put(new Animation(element));
		}
		
		return result;
	}
}

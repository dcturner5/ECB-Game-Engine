package com.gammarush.engine.entities.interactives.vehicles;

import com.gammarush.engine.utils.json.JSON;
import com.gammarush.engine.utils.json.JSONLoader;

public class VehicleLoader {
	
	public static JSON load(String path) {
		JSON json = JSONLoader.load(path);
		return (JSON) json.getJSON("data");
	}
	
}

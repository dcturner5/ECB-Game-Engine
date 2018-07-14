package com.gammarush.engine.entities.vehicles;

import java.util.ArrayList;

import com.gammarush.engine.utils.json.JSON;
import com.gammarush.engine.utils.json.JSONLoader;

public class VehicleLoader {
	
	public static VehicleHashMap load(String path) {
		VehicleHashMap result = new VehicleHashMap();
		JSON json = JSONLoader.load(path);
		
		@SuppressWarnings("unchecked")
		ArrayList<JSON> array = (ArrayList<JSON>) json.getJSON("vehicles");
		for(int i = 0; i < array.size(); i++) {
			JSON element = array.get(i);
			result.put(new VehicleTemplate(i, element));
		}
		
		@SuppressWarnings("unchecked")
		ArrayList<JSON> wheelArray = (ArrayList<JSON>) json.getJSON("wheels");
		for(int i = 0; i < wheelArray.size(); i++) {
			JSON element = wheelArray.get(i);
			result.putWheel(new WheelTemplate(i, element));
		}
		
		return result;
	}
	
}

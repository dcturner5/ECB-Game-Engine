package com.gammarush.engine.utils.json;

import java.util.ArrayList;
import java.util.HashMap;

import com.gammarush.engine.entities.animations.Animation;
import com.gammarush.engine.entities.animations.AnimationHashMap;

public class JSON extends HashMap<String, Object> {
	
	private static final long serialVersionUID = 494413607983759569L;

	@SuppressWarnings("unchecked")
	public Object getJSON(String string) {
		String[] path = string.split("\\.");
		Object map = this;
		for(int i = 0; i < path.length; i++) {
			String object = path[i];
			int open = object.indexOf('['), close = object.indexOf(']');
			if(open != -1 && close != -1) {
				int index = Integer.parseInt(object.substring(open + 1, close));
				object = object.substring(0, open);
				map = ((ArrayList<Object>) ((JSON) map).get(object)).get(index);
			}
			else {
				map = ((JSON) map).get(object);
			}
		}
		return map;
	}
	
	public AnimationHashMap getAnimationHashMap(String string) {
		AnimationHashMap result = new AnimationHashMap();
		ArrayList<JSON> array = getArray(string);
		for(JSON element : array) {
			result.put(new Animation(element));
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<JSON> getArray(String string) {
		return (ArrayList<JSON>) getJSON(string);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<ArrayList<JSON>> get2DArray(String string) {
		return (ArrayList<ArrayList<JSON>>) getJSON(string);
	}
	
	public boolean getBoolean(String string) {
		Object json = getJSON(string);
		if(json == null) return false;
		return (boolean) json;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Boolean> getBooleanArray(String string) {
		return (ArrayList<Boolean>) getJSON(string);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<ArrayList<Boolean>> getBoolean2DArray(String string) {
		return (ArrayList<ArrayList<Boolean>>) getJSON(string);
	}
	
	public float getFloat(String string) {
		Object json = getJSON(string);
		return json instanceof Float ? (float) json : (int) json * 1.0f;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Float> getFloatArray(String string) {
		return (ArrayList<Float>) getJSON(string);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<ArrayList<Float>> getFloat2DArray(String string) {
		return (ArrayList<ArrayList<Float>>) getJSON(string);
	}
	
	public int getInteger(String string) {
		return (int) getJSON(string);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Integer> getIntegerArray(String string) {
		return (ArrayList<Integer>) getJSON(string);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<ArrayList<Integer>> getInteger2DArray(String string) {
		return (ArrayList<ArrayList<Integer>>) getJSON(string);
	}
	
	public String getString(String string) {
		return (String) getJSON(string);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> getStringArray(String string) {
		return (ArrayList<String>) getJSON(string);
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<ArrayList<String>> getString2DArray(String string) {
		return (ArrayList<ArrayList<String>>) getJSON(string);
	}
	
}

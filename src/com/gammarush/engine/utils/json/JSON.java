package com.gammarush.engine.utils.json;

import java.util.ArrayList;
import java.util.HashMap;

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
	
	@SuppressWarnings("unchecked")
	public ArrayList<JSON> getArray(String string) {
		return (ArrayList<JSON>) getJSON(string);
	}
	
	public float getFloat(String string) {
		Object json = getJSON(string);
		return json instanceof Float ? (float) json : (int) json * 1.0f;
	}
	
	public int getInteger(String string) {
		return (int) getJSON(string);
	}
	
	public String getString(String string) {
		return (String) getJSON(string);
	}

}

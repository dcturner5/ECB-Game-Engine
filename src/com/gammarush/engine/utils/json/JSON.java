package com.gammarush.engine.utils.json;

import java.util.ArrayList;
import java.util.HashMap;

public class JSON extends HashMap<String, Object> {
	
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

}

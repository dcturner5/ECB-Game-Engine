package com.gammarush.engine.entities;

import com.gammarush.engine.math.vector.Vector4f;
import com.gammarush.engine.utils.json.JSON;

public class Color {
	
	private String name;
	private Vector4f primary;
	private Vector4f secondary;
	
	public Color() {
		primary = new Vector4f();
		secondary = new Vector4f();
	}
	
	public Color(JSON json) {
		name = json.getString("name");
		primary = json.getColor("primary");
		secondary = json.getColor("secondary");
	}
	
	public String getName() {
		return name;
	}
	
	public Vector4f getPrimary() {
		return primary;
	}
	
	public Vector4f getSecondary() {
		return secondary;
	}
	
}

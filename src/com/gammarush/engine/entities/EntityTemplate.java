package com.gammarush.engine.entities;

import com.gammarush.engine.utils.json.JSON;

public class EntityTemplate {
	
	private int id;
	private String name;
	
	public EntityTemplate(int id, JSON json) {
		this.id = id;
		this.name = json.getString("name");
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

}

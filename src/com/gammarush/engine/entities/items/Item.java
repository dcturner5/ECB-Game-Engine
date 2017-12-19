package com.gammarush.engine.entities.items;

import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.graphics.model.Texture;
import com.gammarush.engine.utils.json.JSON;

public class Item {
	
	public static final int POOL_COMMON = 0;
	public static final int POOL_RARE = 1;
	
	public static final int WIDTH = 16 * Renderer.SCALE;
	public static final int HEIGHT = 16 * Renderer.SCALE;
	
	private int id;
	private String name;
	private int pool;
	
	public Model model;
	
	public Item(int id, JSON json) {
		this.id = id;
		this.name = (String) json.getJSON("name");
		
		String pool = (String) json.getJSON("pool");
		if(pool.equals("common")) {
			this.pool = POOL_COMMON;
		}
		else if(pool.equals("rare")) {
			this.pool = POOL_RARE;
		}
		
		Texture texture = new Texture((String) json.getJSON("texture"));
		this.model = new Model(WIDTH, HEIGHT, texture);
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public int getPool() {
		return pool;
	}

}

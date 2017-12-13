package com.gammarush.engine.entities.mobs.human.clothing;

import com.gammarush.engine.entities.mobs.human.Human;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.graphics.model.Texture;
import com.gammarush.engine.utils.json.JSON;

public class Clothing {
	
	public Model model;
	
	private int id;
	private String name;
	
	public Clothing(JSON json) {
		this.id = (int) json.getJSON("id");
		this.name = (String) json.getJSON("name");
		Texture texture = new Texture((String) json.getJSON("texture"));
		this.model = new Model(Human.WIDTH, Human.HEIGHT, texture);
	}
	
	public void render() {
		
	}
	
	public void prepare() {
		
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

}

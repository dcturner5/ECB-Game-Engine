package com.gammarush.engine.entities.interactives.vehicles;

import java.util.ArrayList;

import com.gammarush.engine.Game;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.graphics.model.Texture;
import com.gammarush.engine.graphics.model.TextureArray;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.utils.json.JSON;

public class Mercury extends Vehicle {
	
	public static final JSON JSON = VehicleLoader.load("res/entities/interactives/vehicles/mercury/data.json");
	public static final int WIDTH = (int) (JSON.getJSON("width")) * Renderer.SCALE;
	public static final int HEIGHT = (int) (JSON.getJSON("height")) * Renderer.SCALE;
	public static final Model EXTERIOR = new Model(WIDTH, HEIGHT, new TextureArray((String) JSON.getJSON("textures.exterior"), 8));
	public static final Model INTERIOR = new Model(WIDTH, HEIGHT, new TextureArray((String) JSON.getJSON("textures.interior"), 4));
	public static final Model WHEEL = new Model((int) (JSON.getJSON("wheel.width")) * Renderer.SCALE, (int) (JSON.getJSON("wheel.height")) * Renderer.SCALE,
			new Texture((String) JSON.getJSON("textures.wheel")));

	public Mercury(Vector3f position, int direction, Game game) {
		super(position, WIDTH, HEIGHT, EXTERIOR, INTERIOR, WHEEL, game);
		this.direction = direction;
		
		@SuppressWarnings("unchecked")
		ArrayList<JSON> mobPositions = (ArrayList<JSON>) JSON.getJSON("mobPositions");
		for(JSON json : mobPositions) {
			if(json == null) {
				this.mobPositions.add(null);
			}
			else {
				this.mobPositions.add(new Vector2f((int) json.getJSON("x"), (int) json.getJSON("y")));
			}
		}
		
		this.occupancy = this.mobPositions.size() / 4;
		
		System.out.println(JSON);
	}

}

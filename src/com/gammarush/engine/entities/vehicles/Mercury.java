package com.gammarush.engine.entities.vehicles;

import com.gammarush.engine.Game;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.graphics.model.TextureArray;
import com.gammarush.engine.math.vector.Vector3f;

public class Mercury extends Vehicle {
	
	public static final int WIDTH = 48 * Renderer.SCALE;
	public static final int HEIGHT = 32 * Renderer.SCALE;
	public static final Model MODEL = new Model(WIDTH, HEIGHT, new TextureArray("res/entities/vehicles/mercury/exterior.png", 8));
	public static final Model INTERIOR = new Model(WIDTH, HEIGHT, new TextureArray("res/entities/vehicles/mercury/interior.png", 4));

	public Mercury(Vector3f position, int direction, Game game) {
		super(position, WIDTH, HEIGHT, MODEL, INTERIOR, game);
		this.direction = direction;
	}

}

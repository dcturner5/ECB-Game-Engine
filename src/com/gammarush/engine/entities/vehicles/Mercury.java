package com.gammarush.engine.entities.vehicles;

import java.util.ArrayList;
import java.util.Arrays;

import com.gammarush.engine.Game;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.graphics.model.TextureArray;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.tiles.Tile;

public class Mercury extends Vehicle {
	
	public static final int WIDTH = 48 * Renderer.SCALE;
	public static final int HEIGHT = 32 * Renderer.SCALE;
	public static final Model MODEL = new Model(WIDTH, HEIGHT, new TextureArray("res/entities/mercury32.png", 4));
	public static final Model INTERIOR = new Model(WIDTH, HEIGHT, new TextureArray("res/entities/mercury32int.png", 4));
	public ArrayList<Vector2f> mobPositions = new ArrayList<Vector2f>(Arrays.asList(
			null, new Vector2f(16, 11),
			new Vector2f(17, 6), null,
			new Vector2f(20, 8), new Vector2f(31, 8),
			new Vector2f(16, 8), new Vector2f(3, 8)
			));
	public int occupancy = 2;

	public Mercury(Vector3f position, int direction, Game game) {
		super(position, WIDTH, HEIGHT, MODEL, INTERIOR, game);
		this.direction = direction;
	}

}

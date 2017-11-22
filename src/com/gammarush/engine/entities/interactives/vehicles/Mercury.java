package com.gammarush.engine.entities.interactives.vehicles;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.graphics.model.TextureArray;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.tiles.Tile;

public class Mercury extends Vehicle {
	
	public static final int WIDTH = (int) (Tile.WIDTH * 1.5);
	public static final int HEIGHT = Tile.HEIGHT;
	
	public static final Model MODEL = new Model(WIDTH, HEIGHT, new TextureArray("res/entities/mercury32.png", 4));

	public Mercury(Vector3f position, Game game) {
		super(position, WIDTH, HEIGHT, MODEL, game);
	}
	
	@Override
	public void activate(Mob entity) {
		
	}

}

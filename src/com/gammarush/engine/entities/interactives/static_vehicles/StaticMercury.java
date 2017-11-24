package com.gammarush.engine.entities.interactives.static_vehicles;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.graphics.model.TextureArray;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.tiles.Tile;

public class StaticMercury extends StaticVehicle {
	
	public static final int WIDTH = Tile.WIDTH * 3;
	public static final int HEIGHT = Tile.HEIGHT * 2;
	
	public static final Model MODEL = new Model(WIDTH, HEIGHT, new TextureArray("res/entities/mercury32.png", 4));

	public StaticMercury(Vector3f position, Game game) {
		super(position, WIDTH, HEIGHT, MODEL, game);
		direction = DIRECTION_LEFT;
	}
	
	@Override
	public void activate(Mob entity) {
		System.out.println("MERCURY ACTIVATED");
	}

}

package com.gammarush.engine.entities.interactives;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.graphics.model.Texture;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.tiles.Tile;

public class Door extends Interactive {
	
	public static final int WIDTH = Tile.WIDTH;
	public static final int HEIGHT = Tile.HEIGHT;
	public static final Model MODEL = new Model(WIDTH, HEIGHT, new Texture("res/entities/door.png"));
	
	private Door destination = null;
	
	public Door(Vector3f position, Game game) {
		super(position, WIDTH, HEIGHT, MODEL, game);
	}
	
	@Override
	public void activate(Mob entity) {
		if(destination == null) return;
		entity.position.x = destination.position.x;
		entity.position.y = destination.position.y;
	}
	
	public void setDestination(Door door) {
		destination = door;
	}

}

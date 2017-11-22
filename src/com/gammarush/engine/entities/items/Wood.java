package com.gammarush.engine.entities.items;

import com.gammarush.engine.Game;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.graphics.model.Texture;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.tiles.Tile;

public class Wood extends Item {
	
	public static final int WIDTH = Tile.WIDTH / 2;
	public static final int HEIGHT = Tile.HEIGHT / 2;
	public static final Model MODEL = new Model(WIDTH, HEIGHT, new Texture("res/entities/wood.png"));
	
	public Wood(Vector3f position, Game game) {
		super(Item.WOOD, position, WIDTH, HEIGHT, MODEL, game);
	}

}

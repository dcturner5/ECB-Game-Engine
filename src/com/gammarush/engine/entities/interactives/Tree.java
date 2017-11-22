package com.gammarush.engine.entities.interactives;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.graphics.model.Texture;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.physics.AABB;
import com.gammarush.engine.tiles.Tile;

public class Tree extends Interactive {
	
	public static final int WIDTH = Tile.WIDTH * 7;
	public static final int HEIGHT = Tile.HEIGHT * 10;
	
	public static final Model MODEL = new Model(WIDTH, HEIGHT, new Texture("res/entities/tree.png"), new Texture("res/entities/tree_normal.png"));

	public Tree(Vector3f position, Game game) {
		super(position, WIDTH, HEIGHT, MODEL, game);
	}
	
	@Override
	public void activate(Mob entity) {
		game.world.trees.remove(this);
	}
	
	@Override
	public AABB getAABB() {
		int treeWidth = WIDTH / Tile.WIDTH;
		int treeHeight = HEIGHT / Tile.HEIGHT;
		int xOffset = (treeWidth / 2) * Tile.WIDTH;
		int yOffset = (treeHeight - 1) * Tile.HEIGHT;
		return new AABB(position.x + xOffset, position.y + yOffset, Tile.WIDTH, Tile.HEIGHT);
	}

}

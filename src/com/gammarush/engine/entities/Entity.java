package com.gammarush.engine.entities;

import com.gammarush.engine.Game;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.math.matrix.Matrix4f;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector2i;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.physics.AABB;
import com.gammarush.engine.physics.Physics;
import com.gammarush.engine.tiles.Tile;
import com.gammarush.engine.world.World;

public class Entity {
	protected Game game;
	
	public Vector3f position;
	public int width;
	public int height;
	public Model model;
	
	public float rotation = 0.0f;
	
	public Physics physics;
	public Vector2f velocity = new Vector2f();
	
	public static final int TEXTURE_LOCATION = 0;
	public static final int NORMAL_MAP_LOCATION = 1;
	
	public Entity(Vector3f position, int width, int height, Model model, Game game) {
		this.game = game;
		
		this.position = position;
		this.width = width;
		this.height = height;
		this.model = model;
	}
	
	public void update(double delta) {
		
	}
	
	public void render() {
		model.getMesh().bind();
		model.getTexture().bind(TEXTURE_LOCATION);
		model.draw();
		model.getMesh().unbind();
		model.getTexture().unbind(TEXTURE_LOCATION);
	}
	
	public void prepare() {
		Renderer.DEFAULT.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(rotation).add(new Vector3f(width / 2, height / 2, 0)))
				.multiply(Matrix4f.scale(new Vector3f(width / model.WIDTH, height / model.HEIGHT, 0))));
	}
	
	public World getWorld() {
		return game.world;
	}
	
	public Vector2f getPosition() {
		return new Vector2f(position.x, position.y);
	}
	
	public Vector2i getTilePosition() {
		return new Vector2i((int)(position.x / Tile.WIDTH), (int)(position.y) / Tile.HEIGHT);
	}
	
	public AABB getAABB() {
		return new AABB(position.x, position.y, width, height);
	}

}

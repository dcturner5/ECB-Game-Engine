package com.gammarush.engine.entities.interactives.static_vehicles;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.interactives.Interactive;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.math.matrix.Matrix4f;
import com.gammarush.engine.math.vector.Vector3f;

public class StaticVehicle extends Interactive {
	
	public int direction = 0;
	
	public static final int DIRECTION_UP = 0;
	public static final int DIRECTION_DOWN = 1;
	public static final int DIRECTION_LEFT = 2;
	public static final int DIRECTION_RIGHT = 3;

	public StaticVehicle(Vector3f position, int width, int height, Model model, Game game) {
		super(position, width, height, model, game);
	}
	
	@Override
	public void prepare() {
		Renderer.MOB.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(rotation).add(new Vector3f(width / 2, height / 2, 0)))
				.multiply(Matrix4f.scale(new Vector3f(width / model.WIDTH, height / model.HEIGHT, 0))));
		Renderer.MOB.setUniform1i("sprite_index", direction);
	}

}

package com.gammarush.engine.entities.interactives.vehicles;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.interactives.Interactive;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.math.vector.Vector3f;

public class Vehicle extends Interactive {

	public Vehicle(Vector3f position, int width, int height, Model model, Game game) {
		super(position, width, height, model, game);
	}

}

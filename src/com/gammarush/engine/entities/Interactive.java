package com.gammarush.engine.entities;

import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.math.vector.Vector2f;

public class Interactive extends Entity {

	public Interactive(Vector2f position, int width, int height, Model model) {
		super(position, width, height, model);
	}

	public void activate(Mob entity) {
		
	}
	
}

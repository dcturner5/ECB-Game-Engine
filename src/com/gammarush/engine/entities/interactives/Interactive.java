package com.gammarush.engine.entities.interactives;

import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.math.vector.Vector3f;

public class Interactive extends Entity {

	public Interactive(Vector3f position, int width, int height, Model model) {
		super(position, width, height, model);
	}

	public void activate(Mob entity) {
		
	}
	
}

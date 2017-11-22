package com.gammarush.engine.entities.interactives;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.structures.Structure;

public class Interactive extends Entity {
	
	protected Structure structure = null;
	protected boolean onStructure = false;

	public Interactive(Vector3f position, int width, int height, Model model, Game game) {
		super(position, width, height, model, game);
	}

	public void activate(Mob entity) {
		
	}
	
}

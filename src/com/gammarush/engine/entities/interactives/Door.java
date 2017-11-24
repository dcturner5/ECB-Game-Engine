package com.gammarush.engine.entities.interactives;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.graphics.model.Texture;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.structures.Structure;
import com.gammarush.engine.tiles.Tile;

public class Door extends Interactive {
	
	public static final int WIDTH = Tile.WIDTH;
	public static final int HEIGHT = Tile.HEIGHT;
	public static final Model MODEL = new Model(WIDTH, HEIGHT, new Texture("res/entities/door.png"));
	
	private Vector2f placement;
	private Door destination = null;
	
	public Door(Vector3f position, Vector2f placement, boolean onStructure, Structure structure, Game game) {
		super(structure.position.add(position), WIDTH, HEIGHT, MODEL, game);
		
		this.structure = structure;
		this.placement = placement;
		this.onStructure = onStructure;
		if(onStructure) rotation = 90f;
	}
	
	@Override
	public void activate(Mob entity) {
		if(destination == null) return;
		
		if(onStructure) {
			entity.position.x = destination.position.x + destination.placement.x;
			entity.position.y = destination.position.y + destination.placement.y;
			
			//entity.setStructure(null);
		}
		else {
			entity.position.x = destination.position.x + destination.placement.x;
			entity.position.y = destination.position.y + destination.placement.y + destination.structure.layer * Tile.HEIGHT;
			
			//entity.setStructure(destination.structure);
		}
	}
	
	public void setDestination(Door door) {
		destination = door;
	}
	
	public Structure getStructure() {
		return structure;
	}
	
	public int getStructureLayer() {
		if(structure != null && onStructure) return structure.layer;
		return 0;
	}

}

package com.gammarush.engine.entities.mobs.behaviors.subbehaviors;

import java.util.ArrayList;

import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.mobs.behaviors.Behavior;
import com.gammarush.engine.math.vector.Vector2i;
import com.gammarush.engine.tiles.Tile;

public class TravelSubBehavior extends SubBehavior {
	
	private Vector2i destination;
	private ArrayList<Vector2i> path = new ArrayList<Vector2i>();

	public TravelSubBehavior(Vector2i destination, Behavior behavior) {
		super(behavior);
		this.destination = destination;
		findPath();
	}
	
	@Override
	public void update() {
		if(!path.isEmpty()) {
			Vector2i waypoint = path.get(0);
			//ADJUST LATER SO YOU CAN REFIND PATH ON TOP OF STRUCTURES
			if(entity.getWorld().checkSolid(waypoint.x, waypoint.y)) {
				findPath();
				//successful = false;
				//complete = true;
			}
			
			waypoint = waypoint.mult(Tile.WIDTH, Tile.HEIGHT);
			if(entity.position.y > waypoint.y) {
				entity.velocity.y -= entity.speed;
				entity.direction = Mob.DIRECTION_UP;
			}
			if(entity.position.y < waypoint.y) {
				entity.velocity.y += entity.speed;
				entity.direction = Mob.DIRECTION_DOWN;
			}
			if(entity.position.x > waypoint.x) {
				entity.velocity.x -= entity.speed;
				entity.direction = Mob.DIRECTION_LEFT;
			}
			if(entity.position.x < waypoint.x) {
				entity.velocity.x += entity.speed;
				entity.direction = Mob.DIRECTION_RIGHT;
			}
			if(entity.position.x == waypoint.x && entity.position.y == waypoint.y && path.size() > 0) path.remove(0);
		}
		else {
			complete = true;
		}
	}
	
	private void findPath() {
		path = entity.astar.findPath(entity.getTilePosition(), destination);
	}

}

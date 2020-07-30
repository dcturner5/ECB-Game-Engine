package com.gammarush.engine.entities.mobs.behaviors.subbehaviors;

import java.util.ArrayList;

import com.gammarush.engine.entities.components.PhysicsComponent;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.mobs.behaviors.Behavior;
import com.gammarush.engine.math.vector.Vector2f;
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
	public void update(double delta) {
		Mob e = getMob();
		if(!path.isEmpty()) {
			PhysicsComponent pc = (PhysicsComponent) e.getComponent("physics");
			Vector2f position = e.getPosition();
			Vector2i waypoint = path.get(0).mult(Tile.WIDTH, Tile.HEIGHT);
			
			//fix this at some point
			if(getMob().getWorld().getSolid(waypoint.x / Tile.WIDTH, waypoint.y / Tile.HEIGHT)) {
				System.out.println("SOLID");
				findPath();
				//successful = false;
				//complete = true;
				//return;
			}
			
			Vector2f velocity = new Vector2f();
			if(position.y > waypoint.y) {
				velocity.y = Math.max(velocity.y - pc.acceleration, waypoint.y - position.y);
				e.setDirection(Mob.DIRECTION_UP);
			}
			else if(position.y < waypoint.y) {
				velocity.y = Math.min(velocity.y + pc.acceleration, waypoint.y - position.y);
				e.setDirection(Mob.DIRECTION_DOWN);
			}
			if(position.x > waypoint.x) {
				velocity.x = Math.max(velocity.x - pc.acceleration, waypoint.x - position.x);
				e.setDirection(Mob.DIRECTION_LEFT);
			}
			else if(position.x < waypoint.x) {
				velocity.x = Math.min(velocity.x + pc.acceleration, waypoint.x - position.x);
				e.setDirection(Mob.DIRECTION_RIGHT);
			}
			
			e.moving = !velocity.isEmpty();
			e.position = e.position.add(velocity);
			
			if(e.position.x == waypoint.x && e.position.y == waypoint.y) {
				if(path.size() > 0) {
					path.remove(0);
				}
				else {
					e.moving = false;
					complete = true;
				}
			}
		}
		else {
			e.moving = false;
			complete = true;
		}
	}
	
	private void findPath() {
		path = getAIComponent().getAStar().findPath(getMob().getTilePosition(), destination);
	}

}

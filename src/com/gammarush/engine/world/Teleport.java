package com.gammarush.engine.world;

import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.physics.AABB;
import com.gammarush.engine.tiles.Tile;

public class Teleport {
	
	private Vector2f position;
	private int direction;
	private String worldName;
	private String markerName;
	private int destDirection;
	
	public Teleport(Vector2f position, int direction, String worldName, String markerName, int destDirection) {
		this.position = position;
		this.direction = direction;
		
		this.worldName = worldName;
		this.markerName = markerName;
		this.destDirection = destDirection;
	}
	
	public void activate(Mob mob) {
		mob.getUIManager().fade.open();
		
		World world = mob.getWorldManager().getWorld(worldName);
		Vector2f position = world.getMarker(markerName);
		
		mob.setPosition(position);
		mob.setDirection(destDirection);
		
		if(!mob.getWorld().equals(world)) {
			if(mob.isPlayer()) {
				mob.getWorldManager().setWorld(worldName);
			}
			
			mob.getWorld().removeMob(mob);
			mob.setLastChunkPosition(mob.getChunkPosition());
			world.addMob(mob);
		}
		else {
			world.syncEntity(mob);
		}
	}
	
	public AABB getAABB() {
		return new AABB(
				position.x + (direction == Mob.DIRECTION_RIGHT ? Tile.WIDTH * .75f : 0), 
				position.y + (direction == Mob.DIRECTION_DOWN ? Tile.HEIGHT * .75f : 0), 
				direction == Mob.DIRECTION_UP || direction == Mob.DIRECTION_DOWN ? Tile.WIDTH : Tile.WIDTH * .25f, 
				direction == Mob.DIRECTION_LEFT || direction == Mob.DIRECTION_RIGHT ? Tile.HEIGHT : Tile.HEIGHT * .25f
			);
	}
	
	public int getDirection() {
		return direction;
	}

}

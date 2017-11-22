package com.gammarush.engine.entities.mobs.behaviors;

import java.util.ArrayList;

import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.mobs.behaviors.subbehaviors.SubBehavior;
import com.gammarush.engine.entities.mobs.behaviors.subbehaviors.TravelSubBehavior;
import com.gammarush.engine.entities.mobs.behaviors.subbehaviors.WaitSubBehavior;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector2i;
import com.gammarush.engine.tiles.Tile;
import com.gammarush.engine.utils.Timer;

public class IdleBehavior extends Behavior {
	
	public static final int PRIORITY = 0;
	
	private Vector2f origin;
	private int range = 4;
	private int waitTime = 240;
	
	public IdleBehavior(Mob entity) {
		super(entity, PRIORITY);
		origin = entity.getPosition();
	}
	
	public IdleBehavior(int range, int waitTime, Mob entity) {
		super(entity, PRIORITY);
		origin = entity.getPosition();
		this.range = range;
		this.waitTime = waitTime;
	}
	
	@Override
	public void update() {
		if(!queue.isEmpty()) {
			SubBehavior b = queue.get(0);
			if(!b.complete) b.update();
			else queue.remove(b);
		}
		else {
			queue.add(new TravelSubBehavior(getRandomPosition(), this));
			queue.add(new WaitSubBehavior(waitTime, this));
		}
	}
	
	public void setOrigin(Vector2f origin) {
		this.origin = origin;
	}
	
	private Vector2i getRandomPosition() {
		ArrayList<Vector2i> tiles = entity.getWorld().getNonSolidTiles((int)(origin.x / Tile.WIDTH), (int)(origin.y / Tile.HEIGHT), range);
		return tiles.get((int)(Math.random() * tiles.size()));
	}
	
}

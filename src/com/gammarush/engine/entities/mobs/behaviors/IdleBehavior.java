package com.gammarush.engine.entities.mobs.behaviors;

import java.util.ArrayList;

import com.gammarush.engine.entities.mobs.behaviors.subbehaviors.SubBehavior;
import com.gammarush.engine.entities.mobs.behaviors.subbehaviors.TravelSubBehavior;
import com.gammarush.engine.entities.mobs.behaviors.subbehaviors.WaitSubBehavior;
import com.gammarush.engine.entities.mobs.components.AIComponent;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector2i;
import com.gammarush.engine.tiles.Tile;

public class IdleBehavior extends Behavior {
	
	public static final int PRIORITY = 0;
	
	private Vector2f origin;
	private int range = 25;
	private int waitTime = 240;
	
	public IdleBehavior(AIComponent aiComponent) {
		super(PRIORITY, aiComponent);
		origin = getMob().getPosition();
	}
	
	public IdleBehavior(AIComponent aiComponent, int range, int waitTime) {
		super(PRIORITY, aiComponent);
		origin = getMob().getPosition();
		this.range = range;
		this.waitTime = waitTime;
	}
	
	@Override
	public void update(double delta) {
		if(!queue.isEmpty()) {
			SubBehavior b = queue.get(0);
			if(!b.getComplete()) b.update(delta);
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
		ArrayList<Vector2i> tiles = getMob().getWorld().getNonSolidTiles((int)(origin.x / Tile.WIDTH), (int)(origin.y / Tile.HEIGHT), range);
		if(tiles.size() == 0) return new Vector2i();
		return tiles.get((int)(Math.random() * tiles.size()));
	}
	
}

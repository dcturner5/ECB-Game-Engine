package com.gammarush.engine.entities.mobs.human;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.mobs.behaviors.Behavior;
import com.gammarush.engine.entities.mobs.behaviors.IdleBehavior;
import com.gammarush.engine.entities.mobs.behaviors.TravelBehavior;
import com.gammarush.engine.entities.mobs.human.clothing.Clothing;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.graphics.model.TextureArray;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector2i;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.tiles.Tile;

public class Human extends Mob {
	
	public static final int WIDTH = 16 * Renderer.SCALE;
	public static final int HEIGHT = 16 * Renderer.SCALE;
	public static final Model MODEL = new Model(WIDTH, HEIGHT, new TextureArray("res/entities/mobs/human/human.png", 16));
	
	protected Behavior travel, lumber;

	public Human(Vector3f position, Game game) {
		super(position, WIDTH, HEIGHT, MODEL, game);
		
		idle = new IdleBehavior(this);
		behaviors.add(idle);
		
		for(int i = 0; i < 2; i++) {
			Clothing c = Game.clothing.getRandom();
			outfit.add(c);
		}
	}
	
	public void update(double delta) {
		if(!isRidingVehicle()) {
			super.update(delta);
			Vector2f initial = new Vector2f(velocity);
			
			updateBehaviors();
			
			if(velocity.x != 0 || velocity.y != 0) moving = true;
			else moving = false;
			
			Vector2f position2D = new Vector2f(position.x, position.y);
			position2D = position2D.add(velocity);
			
			Vector2f translation = physics.collision(position2D);
			position.z = Renderer.ENTITY_LAYER + (position.y / Tile.HEIGHT) / game.world.height;
			
			position2D = position2D.add(translation);
			
			position.x = position2D.x;
			position.y = position2D.y;
			
			velocity = initial;
		}
	}
	
	public void idle() {
		behaviors.remove(lumber);
	}
	
	public void travel(int x, int y) {
		idle.queue.clear();
		
		behaviors.remove(travel);
		travel = new TravelBehavior(new Vector2i(x, y), this);
		behaviors.add(travel);
	}

}

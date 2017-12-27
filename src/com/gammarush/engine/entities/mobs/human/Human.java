package com.gammarush.engine.entities.mobs.human;

import java.util.ArrayList;
import java.util.Arrays;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.items.Item;
import com.gammarush.engine.entities.items.clothing.ClothingTemplate;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.mobs.behaviors.Behavior;
import com.gammarush.engine.entities.mobs.behaviors.IdleBehavior;
import com.gammarush.engine.entities.mobs.behaviors.TravelBehavior;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.graphics.model.TextureArray;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector2i;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.math.vector.Vector4f;
import com.gammarush.engine.physics.Physics;
import com.gammarush.engine.tiles.Tile;

public class Human extends Mob {
	
	public static final int WIDTH = 16 * Renderer.SCALE;
	public static final int HEIGHT = 16 * Renderer.SCALE;
	public static final Model MODEL = new Model(new TextureArray("res/entities/mobs/human/human.png", 16));
	
	public static final Vector4f[] BODY_COLOR_BLACK = new Vector4f[] {new Vector4f(.31f, .14f, .03f, 1), new Vector4f(.26f, .12f, .02f, 1)};
	public static final Vector4f[] BODY_COLOR_BROWN = new Vector4f[] {new Vector4f(.65f, .40f, .13f, 1), new Vector4f(.61f, .32f, .12f, 1)};
	public static final Vector4f[] BODY_COLOR_WHITE = new Vector4f[] {new Vector4f(1, .67f, .38f, 1), new Vector4f(.91f, .60f, .35f, 1)};
	public static final ArrayList<Vector4f[]> BODY_COLORS = new ArrayList<Vector4f[]>(Arrays.asList(BODY_COLOR_BLACK, BODY_COLOR_BROWN, BODY_COLOR_WHITE, BODY_COLOR_WHITE, BODY_COLOR_WHITE, BODY_COLOR_WHITE));
	
	public static final Vector4f[] HAIR_COLOR_BLACK = new Vector4f[] {new Vector4f(.09f, .09f, .09f, 1), new Vector4f(.05f, .05f, .05f, 1)};
	public static final Vector4f[] HAIR_COLOR_BLONDE = new Vector4f[] {new Vector4f(.61f, .54f, .19f, 1), new Vector4f(.55f, .49f, .17f, 1)};
	public static final Vector4f[] HAIR_COLOR_BROWN = new Vector4f[] {new Vector4f(.22f, .09f, 0, 1), new Vector4f(.17f, .07f, 0, 1)};
	public static final Vector4f[] HAIR_COLOR_RED = new Vector4f[] {new Vector4f(.83f, .46f, .22f, 1), new Vector4f(.79f, .44f, .21f, 1)};
	public static final ArrayList<Vector4f[]> HAIR_COLORS = new ArrayList<Vector4f[]>(Arrays.asList(HAIR_COLOR_BLACK, HAIR_COLOR_BLONDE, HAIR_COLOR_BROWN, HAIR_COLOR_RED));
	
	protected Behavior travel, lumber;

	public Human(Vector3f position, Game game) {
		super(position, WIDTH, HEIGHT, MODEL, game);
		
		idle = new IdleBehavior(this);
		behaviors.add(idle);
		
		//race and hair
		color = BODY_COLORS.get((int) (Math.random() * BODY_COLORS.size()));
		if(color.equals(BODY_COLOR_WHITE)) hairColor = HAIR_COLORS.get((int) (Math.random() * HAIR_COLORS.size()));
		else hairColor = HAIR_COLOR_BLACK;
		
		//hair
		//outfit.add(Game.clothings.getRandomByType(ClothingTemplate.TYPE_HAIR));
		//head
		//if(Math.random() < .25) outfit.add(Game.clothings.getRandomByType(ClothingTemplate.TYPE_HEAD));
		//body
		//if(Math.random() < .75) outfit.add(Game.clothings.getRandomByType(ClothingTemplate.TYPE_BODY));
		
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
			
			ArrayList<Item> removeQueue = new ArrayList<Item>();
			for(Item e : getWorld().items) {
				if(Physics.getCollision(getAABB(), e.getAABB())) {
					ClothingTemplate t = Game.clothings.get(e.getTemplate().getName());
					if(t != null) outfit.add(t);
					removeQueue.add(e);
				}
			}
			for(Item e : removeQueue) {
				getWorld().items.remove(e);
			}
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

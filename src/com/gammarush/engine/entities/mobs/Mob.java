package com.gammarush.engine.entities.mobs;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.gammarush.engine.Game;
import com.gammarush.engine.astar.AStar;
import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.entities.interactives.Interactive;
import com.gammarush.engine.entities.mobs.behaviors.Behavior;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.input.KeyCallback;
import com.gammarush.engine.math.matrix.Matrix4f;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.physics.AABB;
import com.gammarush.engine.physics.Physics;
import com.gammarush.engine.tiles.Tile;

public class Mob extends Entity {
	
	protected ArrayList<Behavior> behaviors = new ArrayList<Behavior>();
	private Comparator<Behavior> behaviorSorter = new Comparator<Behavior>() {
		@Override
		public int compare(Behavior b1, Behavior b2) {
			if(b1.priority > b2.priority) return -1;
			if(b1.priority < b2.priority) return 1;
			return 0;
		}
	};
	
	public Physics physics;
	public float speed = 4;
	public int direction = 2;
	
	public boolean moving = true;
	
	public AStar astar;
	
	public Behavior idle;
	
	protected int animationIndex = 0;
	protected int animationFrame = 0;
	protected int animationMaxFrame = 8;
	protected int animationWidth = 4;
	
	public static final int DIRECTION_UP = 0;
	public static final int DIRECTION_DOWN = 1;
	public static final int DIRECTION_LEFT = 2;
	public static final int DIRECTION_RIGHT = 3;

	public Mob(Vector3f position, int width, int height, Model model, Game game) {
		super(position, width, height, model, game);
		
		physics = new Physics(width, height, game.world);
		astar = new AStar(game.world);
	}
	
	@Override
	public void update(double delta) {
		updateAnimation();
	}
	
	@Override
	public void prepare() {
		Renderer.MOB.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(rotation).add(new Vector3f(width / 2, height / 2, 0)))
				.multiply(Matrix4f.scale(new Vector3f(width / model.WIDTH, height / model.HEIGHT, 0))));
		Renderer.MOB.setUniform1i("sprite_index", animationIndex + direction * animationWidth);
	}
	
	public void control() {
		Vector2f initial = new Vector2f(velocity);
		
		float alteredSpeed = speed;
		if((KeyCallback.isKeyDown(GLFW_KEY_W) || KeyCallback.isKeyDown(GLFW_KEY_S)) && 
				(KeyCallback.isKeyDown(GLFW_KEY_A) || KeyCallback.isKeyDown(GLFW_KEY_D))) alteredSpeed = alteredSpeed * 1.4f / 2f;
		
		if(KeyCallback.isKeyDown(GLFW_KEY_W)) {
			velocity.y -= alteredSpeed;
			direction = Mob.DIRECTION_UP;
		}
		if(KeyCallback.isKeyDown(GLFW_KEY_S)) {
			velocity.y += alteredSpeed;
			direction = DIRECTION_DOWN;
		}
		if(KeyCallback.isKeyDown(GLFW_KEY_A)) {
			velocity.x -= alteredSpeed;
			direction = DIRECTION_LEFT;
		}
		if(KeyCallback.isKeyDown(GLFW_KEY_D)) {
			velocity.x += alteredSpeed;
			direction = DIRECTION_RIGHT;
		}
		if(KeyCallback.isKeyDown(GLFW_KEY_SPACE)) {
			Interactive e = getInteractive();
			if(e != null) e.activate(null);
		}
		
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
	
	public void updateBehaviors() {
		if(!behaviors.isEmpty()) {
			Collections.sort(behaviors, behaviorSorter);
			Behavior b = behaviors.get(0);
			if(!b.complete) b.update();
			else behaviors.remove(b);
		}
	}
	
	public void updateAnimation() {
		if(moving) {
			if(animationFrame < animationMaxFrame) {
                animationFrame += 1;
            } else {
                animationFrame = 0;
                if(animationIndex < animationWidth - 1) {
                    animationIndex += 1;
                } else {
                    animationIndex = 0;
                }
            }
		} else {
			animationFrame = 0;
            animationIndex = 0;
		}
	}
	
	public Interactive getInteractive() {
		int padding = 4;
		
		AABB box = new AABB(position.x, position.y, width, height);
		Interactive interactive = null;
		for(Interactive e : game.world.interactives) {
			AABB interactiveBox = e.getAABB();
			interactiveBox.x -= padding;
			interactiveBox.y -= padding;
			interactiveBox.width += padding * 2;
			interactiveBox.height += padding * 2;
			if(Physics.getCollision(box, interactiveBox)) interactive = e;
		}
		
		return interactive;
	}

}

package com.gammarush.engine.entities.player;

import static org.lwjgl.glfw.GLFW.*;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.entities.interactives.Interactive;
import com.gammarush.engine.entities.mobs.Human;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.graphics.model.TextureArray;
import com.gammarush.engine.input.KeyCallback;
import com.gammarush.engine.math.matrix.Matrix4f;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.physics.AABB;
import com.gammarush.engine.physics.Physics;
import com.gammarush.engine.tiles.Tile;

public class Player extends Entity {
	
	public static final int WIDTH = Tile.WIDTH;
	public static final int HEIGHT = Tile.HEIGHT;
	public static final Model MODEL = new Model(WIDTH, HEIGHT, new TextureArray("res/entities/human.png", 16));
	
	public Physics physics;
	public float speed = 4;
	public int direction = 2;
			
	public boolean moving = true;
	
	protected int animationIndex = 0;
	protected int animationFrame = 0;
	protected int animationMaxFrame = 8;
	protected int animationWidth = 4;
	
	public static final int DIRECTION_UP = 0;
	public static final int DIRECTION_DOWN = 1;
	public static final int DIRECTION_LEFT = 2;
	public static final int DIRECTION_RIGHT = 3;
	
	public Player(Vector3f position, Game game) {
		super(position, WIDTH, HEIGHT, MODEL, game);
		physics = new Physics(width, height, game.world);
	}
	
	@Override
	public void update(double delta) {
		updateAnimation();
		Vector2f initial = new Vector2f(velocity);
		
		if(KeyCallback.isKeyDown(GLFW_KEY_W)) {
			velocity.y -= speed;
			direction = DIRECTION_UP;
		}
		if(KeyCallback.isKeyDown(GLFW_KEY_S)) {
			velocity.y += speed;
			direction = DIRECTION_DOWN;
		}
		if(KeyCallback.isKeyDown(GLFW_KEY_A)) {
			velocity.x -= speed;
			direction = DIRECTION_LEFT;
		}
		if(KeyCallback.isKeyDown(GLFW_KEY_D)) {
			velocity.x += speed;
			direction = DIRECTION_RIGHT;
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
		
		//game.renderer.camera.follow(this);
	}

	public void render(Renderer renderer) {
		Renderer.MOB.enable();
		Human.MODEL.getMesh().bind();
		Human.MODEL.getTexture().bind(Human.TEXTURE_LOCATION);
		prepare();
		Human.MODEL.draw();
		Human.MODEL.getMesh().unbind();
		Human.MODEL.getTexture().unbind(Human.TEXTURE_LOCATION);
		Renderer.MOB.disable();
	}
	
	public void prepare() {
		Renderer.MOB.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(rotation).add(new Vector3f(width / 2, height / 2, 0)))
				.multiply(Matrix4f.scale(new Vector3f(width / model.WIDTH, height / model.HEIGHT, 0))));
		Renderer.MOB.setUniform1i("sprite_index", animationIndex + direction * animationWidth);
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

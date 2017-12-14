package com.gammarush.engine.entities.interactives.vehicles;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.Arrays;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.interactives.Interactive;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.input.KeyCallback;
import com.gammarush.engine.math.matrix.Matrix4f;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.physics.AABB;
import com.gammarush.engine.tiles.Tile;

public class Vehicle extends Interactive {
	
	public Model interiorModel;
	protected ArrayList<Mob> mobs = new ArrayList<Mob>();
	public ArrayList<Vector2f> mobPositions = new ArrayList<Vector2f>(Arrays.asList(
			null, new Vector2f(0, 1),
			new Vector2f(0, -3), null,
			new Vector2f(1, 0), new Vector2f(14, 0),
			new Vector2f(-1, 0), new Vector2f(-14, 0)
			));
	protected int occupancy = 2;
	
	public float acceleration = .2f;
	public int direction = 2;
	
	public boolean moving = true;
	public boolean braking = false;
	
	protected int animationIndex = 0;
	protected int animationFrame = 0;
	protected int animationMaxFrame = 8;
	protected int animationWidth = 2;

	public Vehicle(Vector3f position, int width, int height, Model model, Model interiorModel, Game game) {
		super(position, width, height, model, game);
		this.interiorModel = interiorModel;
		setSolid(true);
		setCollisionBox(new AABB(0, 0, width, height));
	}
	
	@Override
	public void update(double delta) {
		if(velocity.x != 0 || velocity.y != 0) moving = true;
		else moving = false;
		
		updateAnimation();
		
		float deceleration = acceleration / 2f;
		if(braking) deceleration += .4f;
		
		if(velocity.x < 0) {
			direction = DIRECTION_LEFT;
			velocity.x = Math.min(velocity.x + deceleration, 0);
		}
		if(velocity.x > 0) {
			direction = DIRECTION_RIGHT;
			velocity.x = Math.max(velocity.x - deceleration, 0);
		}
		if(velocity.y < 0) {
			direction = DIRECTION_UP;
			velocity.y = Math.min(velocity.y + deceleration, 0);
		}
		if(velocity.y > 0) {
			direction = DIRECTION_DOWN;
			velocity.y = Math.max(velocity.y - deceleration, 0);
		}
		
		//temporary
		//make wheels independent sprite in future and rotate them in opengl
		float speed = velocity.magnitude();
		if(speed != 0) {
			animationMaxFrame = (int) ((1f / speed) * 4);
		}
		
		Vector2f position2D = new Vector2f(position.x, position.y);
		position2D = position2D.add(velocity);
		
		Vector2f translation = physics.collision(position2D);
		position.z = Renderer.ENTITY_LAYER + (position.y / Tile.HEIGHT) / game.world.height;
		
		position2D = position2D.add(translation);
		
		position.x = position2D.x;
		position.y = position2D.y;
	}
	
	@Override
	public void render(Renderer renderer) {
		//CAR
		model.getMesh().bind();
		model.getTexture().bind(TEXTURE_LOCATION);
		model.draw();
		model.getMesh().unbind();
		model.getTexture().unbind(TEXTURE_LOCATION);
		//INTERIOR
		prepareInterior();
		interiorModel.getMesh().bind();
		interiorModel.getTexture().bind(TEXTURE_LOCATION);
		interiorModel.draw();
		interiorModel.getMesh().unbind();
		interiorModel.getTexture().unbind(TEXTURE_LOCATION);
		//MOBS
		for(int i = 0; i < mobs.size(); i++) {
			Mob e = mobs.get(i);
			Vector2f position = mobPositions.get(i + direction * occupancy);
			if(position == null) continue;
			
			prepareMob(e, position);
			e.model.getMesh().bind();
			e.model.getTexture().bind(TEXTURE_LOCATION);
			e.model.draw();
			e.model.getMesh().unbind();
			e.model.getTexture().unbind(TEXTURE_LOCATION);
		}
	}
	
	@Override
	public void prepare() {
		Renderer.VEHICLE.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(rotation).add(new Vector3f(width / 2, height / 2, 0)))
				.multiply(Matrix4f.scale(new Vector3f(width / model.WIDTH, height / model.HEIGHT, 0))));
		Renderer.VEHICLE.setUniform1i("sprite_index", animationIndex + direction * animationWidth);
	}
	
	private void prepareInterior() {
		Renderer.VEHICLE.setUniformMat4f("ml_matrix", Matrix4f.translate(position.add(0, 0, -.0002f)).multiply(Matrix4f.rotate(rotation).add(new Vector3f(width / 2, height / 2, 0)))
				.multiply(Matrix4f.scale(new Vector3f(width / model.WIDTH, height / model.HEIGHT, 0))));
		Renderer.VEHICLE.setUniform1i("sprite_index", direction);
	}
	
	private void prepareMob(Mob mob, Vector2f offset) {
		Renderer.VEHICLE.setUniformMat4f("ml_matrix", 
				Matrix4f.translate(position.add(offset.x * Renderer.SCALE, offset.y * Renderer.SCALE, -.0001f))
				.multiply(Matrix4f.rotate(rotation).add(new Vector3f(width / 2, height / 2, 0)))
				.multiply(Matrix4f.scale(new Vector3f(mob.width / mob.model.WIDTH, mob.height / mob.model.HEIGHT, 0))));
		Renderer.VEHICLE.setUniform1i("sprite_index", direction * mob.animation.width);
	}
	
	@Override
	public void activate(Mob mob) {
		if(isRiding(mob)) {
			removeMob(mob);
		}
		else {
			addMob(mob);
		}
	}
	
	public void control(Mob mob) {
		braking = false;
		if(KeyCallback.isKeyDown(GLFW_KEY_W)) {
			if(velocity.x == 0) velocity.y -= acceleration;
			else braking = true;
		}
		if(KeyCallback.isKeyDown(GLFW_KEY_S)) {
			if(velocity.x == 0) velocity.y += acceleration;
			else braking = true;
		}
		if(KeyCallback.isKeyDown(GLFW_KEY_A)) {
			if(velocity.y == 0) velocity.x -= acceleration;
			else braking = true;
		}
		if(KeyCallback.isKeyDown(GLFW_KEY_D)) {
			if(velocity.y == 0) velocity.x += acceleration;
			else braking = true;
		}
		if(KeyCallback.isKeyDown(GLFW_KEY_Q)) {
			activate(mob);
		}
	}
	
	public boolean addMob(Mob mob) {
		if(mobs.size() < occupancy && mobs.add(mob)) {
			mob.setVehicle(this);
			//if(mobs.size() < 2) mobs.add(new Human(position, game));
			return true;
		}
		return false;
	}
	
	public boolean removeMob(Mob mob) {
		if(mobs.remove(mob)) {
			mob.setVehicle(null);
			if(mobs.size() == 0) {
				//stop car or something
			}
		}
		return false;
	}
	
	public boolean isDriving(Mob mob) {
		if(mobs.size() == 0) return false;
		return mobs.get(0).equals(mob);
	}
	
	public boolean isRiding(Mob mob) {
		return mobs.contains(mob);
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

}

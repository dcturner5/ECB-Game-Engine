package com.gammarush.engine.entities.interactives.vehicles;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.interactives.Interactive;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.mobs.animations.AnimationData;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.input.KeyCallback;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.physics.AABB;
import com.gammarush.engine.tiles.Tile;

public class Vehicle extends Interactive {
	
	private VehicleTemplate template;
	private WheelTemplate wheelTemplate;
	
	private float acceleration;
	private int occupancy;
	
	private ArrayList<Mob> mobs = new ArrayList<Mob>();
	private ArrayList<Vector2f> mobPositions = new ArrayList<Vector2f>();
	
	public int direction;
	public boolean moving = true;
	public boolean braking = false;
	
	public AnimationData animation = new AnimationData(2, 8);
	public float wheelRotation = 0f;

	public Vehicle(VehicleTemplate template, Vector3f position, int direction, Game game) {
		super(position, template.getWidth(), template.getHeight(), template.getModel(), game);
		this.template = template;
		this.wheelTemplate = Game.vehicles.getRandomWheel();
		this.acceleration = template.getAcceleration();
		this.occupancy = template.getOccupancy();
		this.direction = direction;
		this.mobPositions = template.getMobPositions();
		
		setSolid(true);
		setCollisionBox(new AABB(0, 0, width, height));
	}
	
	@Override
	public void update(double delta) {
		if(velocity.x != 0 || velocity.y != 0) moving = true;
		else moving = false;
		
		animation.update(moving);
		animation.setDirection(direction);
		
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
		
		float speed = velocity.magnitude();
		if(speed != 0) {
			animation.setMaxFrame((int) ((1f / speed) * 4));
			wheelRotation += velocity.x;
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
	public void render() {
		//getWorld().vehicleBatchManager.add(this);
		//MOBS
		for(int i = 0; i < mobs.size(); i++) {
			//Mob e = mobs.get(i);
			
			Vector2f position = mobPositions.get(i + direction * occupancy);
			if(position == null) continue;
			
			/*prepareMob(e, position);
			e.model.getMesh().bind();
			e.model.getTexture().bind(TEXTURE_LOCATION);
			e.model.draw();
			e.model.getMesh().unbind();
			e.model.getTexture().unbind(TEXTURE_LOCATION);
			e.outfit.render(renderer);*/
		}
	}
	
	/*private void prepareMob(Mob mob, Vector2f offset) {
		Renderer.MOB.setUniformMat4f("ml_matrix", 
				Matrix4f.translate(position.add(offset.x * Renderer.SCALE, offset.y * Renderer.SCALE, -.0001f))
				.multiply(Matrix4f.rotate(rotation).add(new Vector3f(width / 2, height / 2, 0)))
				.multiply(Matrix4f.scale(new Vector3f(mob.width / mob.model.WIDTH, mob.height / mob.model.HEIGHT, 0))));
		Renderer.MOB.setUniform1i("sprite_index", direction * mob.animation.width);
		Renderer.MOB.setUniform4f("primary_color", mob.color[0]);
		Renderer.MOB.setUniform4f("secondary_color", mob.color[1]);
	}*/
	
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
	
	public VehicleTemplate getTemplate() {
		return template;
	}
	
	public WheelTemplate getWheelTemplate() {
		return wheelTemplate;
	}

}

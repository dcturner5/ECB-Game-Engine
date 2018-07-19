package com.gammarush.engine.entities.mobs.components;

import static org.lwjgl.glfw.GLFW.*;

import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.entities.Interactive;
import com.gammarush.engine.entities.components.PhysicsComponent;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.vehicles.Vehicle;
import com.gammarush.engine.input.KeyCallback;
import com.gammarush.engine.math.vector.Vector2f;

public class ControllableComponent extends MobComponent {
	
	public static final String NAME = "controllable";
	public static final String[] DEPENDENCIES = new String[]{"physics"};
	public static final int PRIORITY = 0;

	public ControllableComponent(Entity entity) {
		super(NAME, DEPENDENCIES, PRIORITY, entity);
	}

	@Override
	public void update(double delta) {
		Mob e = getMob();
		
		if(!e.isRidingVehicle()) {
			controlMob();
		}
		if(e.isDrivingVehicle()) {
			controlVehicle();
		}
	}
	
	private void controlMob() {
		Mob e = getMob();
		PhysicsComponent pc = (PhysicsComponent) e.getComponent("physics");
		
		float acceleration = pc.acceleration;
		if((KeyCallback.isKeyDown(GLFW_KEY_W) || KeyCallback.isKeyDown(GLFW_KEY_S)) && 
				(KeyCallback.isKeyDown(GLFW_KEY_A) || KeyCallback.isKeyDown(GLFW_KEY_D))) {
			acceleration = acceleration * 1.4f / 2f;
		}
		
		Vector2f velocity = new Vector2f();
		
		if(!e.isInteractingWithMob()) {
			if(KeyCallback.isKeyDown(GLFW_KEY_W)) {
				velocity.y -= acceleration;
				e.direction = Entity.DIRECTION_UP;
			}
			if(KeyCallback.isKeyDown(GLFW_KEY_S)) {
				velocity.y += acceleration;
				e.direction = Entity.DIRECTION_DOWN;
			}
			if(KeyCallback.isKeyDown(GLFW_KEY_A)) {
				velocity.x -= acceleration;
				e.direction = Entity.DIRECTION_LEFT;
			}
			if(KeyCallback.isKeyDown(GLFW_KEY_D)) {
				velocity.x += acceleration;
				e.direction = Entity.DIRECTION_RIGHT;
			}
			if(KeyCallback.isKeyDown(GLFW_KEY_E)) {
				Interactive e1 = e.getInteractive();
				if(e1 != null) e1.activate(e);
			}
		}
		
		e.moving = !velocity.isEmpty();
		e.position = e.position.add(velocity);
	}
	
	private void controlVehicle() {
		Mob e = getMob();
		Vehicle v = e.getVehicle();
		PhysicsComponent pc = (PhysicsComponent) v.getComponent("physics");
		
		v.braking = false;
		if(KeyCallback.isKeyDown(GLFW_KEY_W)) {
			if(pc.velocity.x == 0) pc.velocity.y -= pc.acceleration;
			else v.braking = true;
		}
		if(KeyCallback.isKeyDown(GLFW_KEY_S)) {
			if(pc.velocity.x == 0) pc.velocity.y += pc.acceleration;
			else v.braking = true;
		}
		if(KeyCallback.isKeyDown(GLFW_KEY_A)) {
			if(pc.velocity.y == 0) pc.velocity.x -= pc.acceleration;
			else v.braking = true;
		}
		if(KeyCallback.isKeyDown(GLFW_KEY_D)) {
			if(pc.velocity.y == 0) pc.velocity.x += pc.acceleration;
			else v.braking = true;
		}
		if(KeyCallback.isKeyDown(GLFW_KEY_Q)) {
			v.activate(e);
		}
	}

}

package com.gammarush.engine.entities.vehicles;

import java.util.ArrayList;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.Interactive;
import com.gammarush.engine.entities.components.AnimationComponent;
import com.gammarush.engine.entities.components.PhysicsComponent;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.math.vector.Vector2f;

public class Vehicle extends Interactive {
	
	private VehicleTemplate template;
	private WheelTemplate wheelTemplate;
	private int occupancy;
	private ArrayList<Mob> mobs = new ArrayList<Mob>();
	private ArrayList<Vector2f> mobPositions = new ArrayList<Vector2f>();
	
	public int direction;
	public boolean moving = true;
	public boolean braking = false;
	
	public float wheelRotation = 0f;
	
	public Vehicle(VehicleTemplate template, Vector2f position, int direction) {
		super(position, template.getWidth(), template.getHeight(), template.getModel());
		this.template = template;
		this.wheelTemplate = Game.vehicles.getRandomWheel();
		this.occupancy = template.getOccupancy();
		this.direction = direction;
		this.mobPositions = template.getMobPositions();
		
		setSolid(true);
		addComponent(new PhysicsComponent(this, template.getAcceleration()));
		addComponent(new AnimationComponent(this, template.getAnimationHashMap()));
		((AnimationComponent) getComponent("animation")).start("run");
	}
	
	@Override
	public void update(double delta) {
		super.update(delta);
		
		PhysicsComponent pc = (PhysicsComponent) getComponent("physics");
		
		if(pc.velocity.x != 0 || pc.velocity.y != 0) moving = true;
		else moving = false;
		
		float deceleration = pc.acceleration / 2f;
		if(braking) deceleration += .4f;
		
		if(pc.velocity.x < 0) {
			direction = DIRECTION_LEFT;
			pc.velocity.x = Math.min(pc.velocity.x + deceleration, 0);
		}
		if(pc.velocity.x > 0) {
			direction = DIRECTION_RIGHT;
			pc.velocity.x = Math.max(pc.velocity.x - deceleration, 0);
		}
		if(pc.velocity.y < 0) {
			direction = DIRECTION_UP;
			pc.velocity.y = Math.min(pc.velocity.y + deceleration, 0);
		}
		if(pc.velocity.y > 0) {
			direction = DIRECTION_DOWN;
			pc.velocity.y = Math.max(pc.velocity.y - deceleration, 0);
		}
		
		float speed = pc.velocity.magnitude();
		if(speed != 0) {
			AnimationComponent ac = (AnimationComponent) getComponent("animation");
			ac.getAnimation().setMaxFrame((int) ((1f / speed) * 4));
			wheelRotation += pc.velocity.x;
		}
		
		for(int i = 0; i < mobs.size(); i++) {
			Mob e = mobs.get(i);
			Vector2f position = mobPositions.get(i + direction * occupancy);
			if(position != null) {
				e.position.x = this.position.x + position.x;
				e.position.y = this.position.y + position.y;
			}
		}
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

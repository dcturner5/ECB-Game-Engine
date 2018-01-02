package com.gammarush.engine.entities.interactives.vehicles;

import java.util.ArrayList;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.components.AnimationComponent;
import com.gammarush.engine.entities.components.PhysicsComponent;
import com.gammarush.engine.entities.interactives.Interactive;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.animations.Animation;
import com.gammarush.engine.entities.animations.AnimationHashMap;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector3f;

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

	public Vehicle(VehicleTemplate template, Vector3f position, int direction, Game game) {
		super(position, template.getWidth(), template.getHeight(), template.getModel(), game);
		this.template = template;
		this.wheelTemplate = Game.vehicles.getRandomWheel();
		this.occupancy = template.getOccupancy();
		this.direction = direction;
		this.mobPositions = template.getMobPositions();
		
		setSolid(true);
		addComponent(new PhysicsComponent(this, template.getAcceleration()));
		addComponent(new AnimationComponent(this, new AnimationHashMap(new Animation("run", true, 0, 8, 2))));
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

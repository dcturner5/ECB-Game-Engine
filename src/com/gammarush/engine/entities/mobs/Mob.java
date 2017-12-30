package com.gammarush.engine.entities.mobs;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.entities.components.PhysicsComponent;
import com.gammarush.engine.entities.interactives.Interactive;
import com.gammarush.engine.entities.mobs.animations.AnimationData;
import com.gammarush.engine.entities.mobs.components.AIComponent;
import com.gammarush.engine.entities.mobs.components.ClothingComponent;
import com.gammarush.engine.entities.interactives.vehicles.Vehicle;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.math.matrix.Matrix4f;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.math.vector.Vector4f;
import com.gammarush.engine.physics.AABB;
import com.gammarush.engine.physics.Physics;

public class Mob extends Entity {
	
	private AIComponent aiComponent;
	
	public int speed = 4;
	public int direction = 2;
	public boolean moving = true;

	public Vehicle vehicle = null;
	public AnimationData animation;
	
	public Vector4f[] color = new Vector4f[] {new Vector4f(), new Vector4f()};
	public Vector4f[] hairColor = new Vector4f[] {new Vector4f(), new Vector4f()};

	public Mob(Vector3f position, int width, int height, Model model, Game game) {
		super(position, width, height, model, game);
		setSolid(false);
		setPhysicsComponent(new PhysicsComponent(this, 4));
		setAIComponent(new AIComponent(this));
		addComponent(new ClothingComponent(this));
		
		animation = new AnimationData(4, 8);
	}
	
	@Override
	public void update(double delta) {
		super.update(delta);
		
		if(!isRidingVehicle()) {
			animation.update(moving);
			animation.setDirection(direction);
		}
	}
	
	@Override
	public void render() {
		if(!isRidingVehicle()) {
			super.render();
		}
	}
	
	@Override
	public void prepare() {
		if(!isRidingVehicle()) {
			Renderer.MOB.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(rotation).add(new Vector3f(width / 2, height / 2, 0)))
					.multiply(Matrix4f.scale(new Vector3f(width, height, 0))));
			Renderer.MOB.setUniform1i("sprite_index", animation.getIndex());
			Renderer.MOB.setUniform4f("primary_color", color[0]);
			Renderer.MOB.setUniform4f("secondary_color", color[1]);
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
	
	public boolean isRidingVehicle() {
		return vehicle != null && vehicle.isRiding(this);
	}
	
	public boolean isDrivingVehicle() {
		return vehicle != null && vehicle.isDriving(this);
	}
	
	public Vehicle getVehicle() {
		return vehicle;
	}
	
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	
	public AIComponent getAIComponent() {
		return aiComponent;
	}
	
	public void setAIComponent(AIComponent aiComponent) {
		this.aiComponent = aiComponent;
		addComponent(aiComponent);
	}

}

package com.gammarush.engine.entities.mobs;

import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.entities.components.AnimationComponent;
import com.gammarush.engine.entities.components.InventoryComponent;
import com.gammarush.engine.entities.components.PhysicsComponent;
import com.gammarush.engine.entities.interactives.Interactive;
import com.gammarush.engine.entities.mobs.components.AIComponent;
import com.gammarush.engine.entities.mobs.components.ClothingComponent;
import com.gammarush.engine.entities.mobs.components.ControllableComponent;
import com.gammarush.engine.entities.interactives.vehicles.Vehicle;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.input.KeyCallback;
import com.gammarush.engine.math.matrix.Matrix4f;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.math.vector.Vector4f;
import com.gammarush.engine.physics.AABB;
import com.gammarush.engine.physics.Physics;
import com.gammarush.engine.utils.json.JSON;

public class Mob extends Entity {
	
	private MobTemplate template;
	
	public boolean moving = false;
	public int direction = 2;
	public Vehicle vehicle = null;
	
	public Vector4f[] color = new Vector4f[] {new Vector4f(), new Vector4f()};
	public Vector4f[] hairColor = new Vector4f[] {new Vector4f(), new Vector4f()};

	public Mob(MobTemplate template, Vector3f position) {
		super(position, template.getWidth(), template.getHeight(), template.getModel());
		this.template = template;
		setSolid(false);
		
		//race and hair
		color = template.colors.get((int) (Math.random() * template.colors.size()));
		hairColor = template.hairColors.get((int) (Math.random() * template.hairColors.size()));
		
		for(JSON c : template.getComponents()) {
			String name = c.getString("name");
			if(name.equals("ai")) {
				addComponent(new AIComponent(this));
			}
			if(name.equals("animation")) {
				addComponent(new AnimationComponent(this, c.getAnimationHashMap("animations")));
			}
			if(name.equals("clothing")) {
				addComponent(new ClothingComponent(this));
			}
			if(name.equals("controllable")) {
				addComponent(new ControllableComponent(this));
			}
			if(name.equals("inventory")) {
				addComponent(new InventoryComponent(this, c.getInteger("size")));
			}
			if(name.equals("physics")) {
				addComponent(new PhysicsComponent(this, c.getFloat("acceleration")));
			}
		}
	}
	
	@Override
	public void update(double delta) {
		super.update(delta);
		
		AnimationComponent ac = ((AnimationComponent) getComponent("animation"));
		if(moving) ac.start("run");
		else ac.stop("run");

		if(!ac.isRunning()) ac.start("idle");
		
		if(KeyCallback.isKeyDown(org.lwjgl.glfw.GLFW.GLFW_KEY_T)) {
			((AnimationComponent) getComponent("animation")).start("stab");
		}
	}

	@Override
	public void render() {
		super.render();
	}
	
	@Override
	public void prepare() {
			Renderer.MOB.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(rotation).add(new Vector3f(width / 2, height / 2, 0)))
					.multiply(Matrix4f.scale(new Vector3f(width, height, 0))));
			Renderer.MOB.setUniform1i("sprite_index", ((AnimationComponent) getComponent("animation")).getIndex());
			Renderer.MOB.setUniform4f("primary_color", color[0]);
			Renderer.MOB.setUniform4f("secondary_color", color[1]);
	}
	
	public Interactive getInteractive() {
		int padding = 4;
		
		AABB box = new AABB(position.x, position.y, width, height);
		Interactive interactive = null;
		for(Interactive e : getWorld().getInteractives()) {
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
	
	public MobTemplate getTemplate() {
		return template;
	}

}

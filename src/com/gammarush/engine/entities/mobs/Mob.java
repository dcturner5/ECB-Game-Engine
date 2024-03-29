package com.gammarush.engine.entities.mobs;

import com.gammarush.engine.entities.Color;
import com.gammarush.engine.entities.Interactive;
import com.gammarush.engine.entities.components.AnimationComponent;
import com.gammarush.engine.entities.components.FrictionComponent;
import com.gammarush.engine.entities.components.InventoryComponent;
import com.gammarush.engine.entities.components.PhysicsComponent;
import com.gammarush.engine.entities.mobs.actors.Actor;
import com.gammarush.engine.entities.mobs.components.AIComponent;
import com.gammarush.engine.entities.mobs.components.AttackComponent;
import com.gammarush.engine.entities.mobs.components.ClothingComponent;
import com.gammarush.engine.entities.mobs.components.ControllableComponent;
import com.gammarush.engine.entities.mobs.components.ProjectileComponent;
import com.gammarush.engine.entities.mobs.components.StatsComponent;
import com.gammarush.engine.entities.vehicles.Vehicle;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.math.matrix.Matrix4f;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.physics.AABB;
import com.gammarush.engine.physics.Physics;
import com.gammarush.engine.utils.json.JSON;
import com.gammarush.engine.world.Teleport;

public class Mob extends Interactive {
	
	private MobTemplate template;
	
	public boolean moving = false;
	private int direction = 2;
	private Vehicle vehicle = null;
	
	public Color color = new Color();
	public Color hairColor = new Color();
	
	private Mob interactingMob;

	public Mob(MobTemplate template, Vector2f position) {
		super(position, template.getWidth(), template.getHeight(), template.getModel());
		this.template = template;
		setSolid(false);
		setCollisionBox(template.getCollisionBox());
		
		if(template.colors.size() > 0) {
			color = template.colors.get((int) (Math.random() * template.colors.size()));
		}
		if(template.hairColors.size() > 0) {
			hairColor = template.hairColors.get((int) (Math.random() * template.hairColors.size()));
		}
		
		for(JSON c : template.getComponents()) {
			String name = c.getString("name");
			if(name.equals("ai")) {
				addComponent(new AIComponent(this));
			}
			if(name.equals("animation")) {
				addComponent(new AnimationComponent(this, c.getAnimationHashMap("animations")));
			}
			if(name.equals("attack")) {
				addComponent(new AttackComponent(this));
			}
			if(name.equals("clothing")) {
				addComponent(new ClothingComponent(this));
			}
			if(name.equals("controllable")) {
				addComponent(new ControllableComponent(this));
			}
			if(name.equals("friction")) {
				addComponent(new FrictionComponent(this));
			}
			if(name.equals("inventory")) {
				addComponent(new InventoryComponent(this, c.getInteger("size")));
			}
			if(name.equals("physics")) {
				addComponent(new PhysicsComponent(this, c.getFloat("acceleration")));
			}
			if(name.equals("projectile")) {
				addComponent(new ProjectileComponent(this));
			}
			if(name.equals("stats")) {
				addComponent(new StatsComponent(this, new MobStats(c)));
			}
		}
	}
	
	@Override
	public void update(double delta) {
		super.update(delta);
		position.z = Renderer.ENTITY_LAYER + getNormalizedWorldPosition().y;
		
		AnimationComponent ac = ((AnimationComponent) getComponent("animation"));
		if(moving) ac.start("run");
		else ac.stop("run");

		if(!ac.isRunning()) ac.start("idle");
		
		//Teleport check
		if(isPlayer()) {
			for(Teleport t : getWorld().getTeleports()) {
				if(Physics.getCollision(getAABB(), t.getAABB()) && getDirection() == t.getDirection()) {
					t.activate(this);
				}
			}
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
			Renderer.MOB.setUniform4f("primary_color", color.getPrimary());
			Renderer.MOB.setUniform4f("secondary_color", color.getSecondary());
	}
	
	@Override
	public void activate(Mob e) {
		//System.out.println("ACTIVATED");
	}
	
	public Interactive getInteractive() {
		int padding = 4;
		
		AABB box = new AABB(position.x, position.y, width, height);
		Interactive interactive = null;
		for(Interactive e : getWorld().getInteractives()) {
			if(e.equals(this)) continue;
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
	
	public int getDirection() {
		return direction;
	}
	
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	public MobTemplate getTemplate() {
		return template;
	}
	
	public AIComponent getAIComponent() {
		return (AIComponent) getComponent("ai");
	}
	
	public Mob getInteractingMob() {
		return interactingMob;
	}
	
	public void setInteractingMob(Mob mob) {
		interactingMob = mob;
	}
	
	public boolean isInteractingWithMob() {
		return interactingMob != null;
	}
	
	public boolean isActor() {
		return this instanceof Actor;
	}
	
	public boolean isPlayer() {
		return getPlayerManager().getMob().equals(this);
	}

}

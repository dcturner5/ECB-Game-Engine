package com.gammarush.engine.entities.mobs.components;

import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.entities.components.PhysicsComponent;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.physics.AABB;
import com.gammarush.engine.physics.Physics;
import com.gammarush.engine.world.World;

public class ProjectileComponent extends MobComponent {
	
	public static final String NAME = "projectile";
	public static final String[] DEPENDENCIES = new String[]{"physics"};
	public static final int PRIORITY = 4;

	public ProjectileComponent(Entity entity) {
		super(NAME, DEPENDENCIES, PRIORITY, entity);
	}
	
	@Override
	public void update(double delta) {
		World world = getMob().getWorld();
		AABB box = getMob().getAABB();
		for(Mob e : world.getMobs()) {
			if(Physics.getCollision(box, e.getAABB())) {
				//world.removeMob(getMob());
			}
		}
	}
	
	public void setVelocity(Vector2f velocity) {
		((PhysicsComponent) getMob().getComponent("physics")).velocity = velocity;
	}

}

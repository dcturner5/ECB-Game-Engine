package com.gammarush.engine.entities.mobs.components;

import com.gammarush.engine.entities.components.AnimationComponent;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.physics.AABB;
import com.gammarush.engine.physics.Physics;

public class AttackComponent extends MobComponent {
	
	public static final String NAME = "attack";
	public static final String[] DEPENDENCIES = new String[]{"animation"};
	public static final int PRIORITY = 1;
	
	private int damage = 10;
	private int range = 32;
	private int cooldown = 16;
	
	private int cooldownIndex = 0;

	public AttackComponent(Mob mob) {
		super(NAME, DEPENDENCIES, PRIORITY, mob);
	}

	@Override
	public void update(double delta) {
		if(cooldownIndex > 0) cooldownIndex--;
	}
	
	public boolean attack() {
		if(cooldownIndex > 0) return false;
		
		Mob e = getMob();
		AABB cb = new AABB(e.position.x, e.position.y, e.width, e.height);
		
		Vector2f offset = new Vector2f();
		if(e.direction == Mob.DIRECTION_UP) {
			offset.y -= range;
		}
		if(e.direction == Mob.DIRECTION_DOWN) {
			offset.y += range;
		}
		if(e.direction == Mob.DIRECTION_LEFT) {
			offset.x -= range;
		}
		if(e.direction == Mob.DIRECTION_RIGHT) {
			offset.x += range;
		}
		
		cb.x += offset.x;
		cb.y += offset.y;
		
		Mob mob = null;
		for(Mob e1 : e.getWorld().getMobs()) {
			if(e1.equals(e)) continue;
			AABB mobBox = e1.getAABB();
			if(Physics.getCollision(cb, mobBox)) mob = e1;
		}
		
		if(mob != null) {
			StatsComponent sc = (StatsComponent) mob.getComponent("stats");
			if(sc != null) {
				sc.damageHealth(damage);
				((AnimationComponent) e.getComponent("animation")).start("stab");
				cooldownIndex = cooldown;
				return true;
			}
		}
		System.out.println("ATTACK FAILED");
		return false;
	}

}

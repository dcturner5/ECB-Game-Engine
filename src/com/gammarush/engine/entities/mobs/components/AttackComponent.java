package com.gammarush.engine.entities.mobs.components;

import com.gammarush.engine.entities.components.AnimationComponent;
import com.gammarush.engine.entities.components.PhysicsComponent;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.physics.AABB;
import com.gammarush.engine.physics.Physics;

public class AttackComponent extends MobComponent {
	
	public static final String NAME = "attack";
	public static final String[] DEPENDENCIES = new String[]{"animation", "stats"};
	public static final int PRIORITY = 1;
	
	private int attack;
	private int range;
	private int cooldown;
	
	private int cooldownIndex = 0;

	public AttackComponent(Mob mob) {
		super(NAME, DEPENDENCIES, PRIORITY, mob);
		
		StatsComponent sc = (StatsComponent) mob.getComponent("stats");
		attack = sc.getStat("attack.damage");
		range = sc.getStat("attack.range");
		cooldown = sc.getStat("attack.cooldown");
	}

	@Override
	public void update(double delta) {
		if(getMob().isInteractingWithMob()) cooldownIndex = cooldown;
		else if(cooldownIndex > 0) cooldownIndex--;
	}
	
	public boolean attack() {
		Mob e = getMob();
		if(cooldownIndex > 0) return false;
		
		AABB cb = new AABB(e.position.x, e.position.y, e.width, e.height);
		
		Vector2f offset = new Vector2f();
		if(e.direction == Mob.DIRECTION_UP) {
			offset.y = -1;
		}
		if(e.direction == Mob.DIRECTION_DOWN) {
			offset.y = 1;
		}
		if(e.direction == Mob.DIRECTION_LEFT) {
			offset.x = -1;
		}
		if(e.direction == Mob.DIRECTION_RIGHT) {
			offset.x = 1;
		}
		
		cb.x += offset.x * range;
		cb.y += offset.y * range;
		
		Mob mob = null;
		for(Mob e1 : e.getWorld().getMobs()) {
			if(e1.equals(e)) continue;
			AABB mobBox = e1.getAABB();
			if(Physics.getCollision(cb, mobBox)) mob = e1;
		}
		
		cooldownIndex = cooldown;
		activateClothing("attack", e);
		
		((AnimationComponent) e.getComponent("animation")).start("attack");
		if(mob != null) {
			PhysicsComponent pc = (PhysicsComponent) mob.getComponent("physics");
			StatsComponent sc = (StatsComponent) mob.getComponent("stats");
			if(pc != null && sc != null) {
				int damage =  attack + getClothingStat("attack", e) - getClothingStat("defense", mob);
				sc.alterHealth(-damage, e);
				pc.velocity = offset.mult(damage);
				cooldownIndex = cooldown; //? defense cooldown
				
				activateClothing("defense", mob);
				return true;
			}
		}
		
		return false;
	}
	
	public int getRange() {
		return range + getClothingStat("range", getMob());
	}
	
	public void activateClothing(String name, Mob e) {
		ClothingComponent cc = (ClothingComponent) e.getComponent("clothing");
		if(cc != null) cc.activate(name);
	}
	
	public int getClothingStat(String name, Mob e) {
		if(e.getComponent("clothing") == null) return 0;
		return ((ClothingComponent) e.getComponent("clothing")).getStat(name);
	}

}

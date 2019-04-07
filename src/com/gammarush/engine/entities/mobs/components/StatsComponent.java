package com.gammarush.engine.entities.mobs.components;

import com.gammarush.engine.GameManager;
import com.gammarush.engine.entities.components.AnimationComponent;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.mobs.MobStats;
import com.gammarush.engine.entities.mobs.behaviors.AttackBehavior;
import com.gammarush.engine.events.DeathEvent;

public class StatsComponent extends MobComponent {
	
	public static final String NAME = "stats";
	public static final String[] DEPENDENCIES = new String[]{};
	public static final int PRIORITY = 1;
	
	private MobStats stats;
	private int health;
	
	public StatsComponent(Mob mob, MobStats stats) {
		super(NAME, DEPENDENCIES, PRIORITY, mob);
		this.stats = stats;
		health = stats.getStat("health");
	}
	
	@Override
	public void update(double delta) {
		
	}
	
	public void restoreAllHealth() {
		health = stats.getStat("health");
	}
	
	public void alterHealth(int delta, Mob e) {
		if(getMob().isDisabled()) return;
		
		//Hostility check
		if(delta < 0) {
			AIComponent ac = getMob().getAIComponent();
			if(ac != null) {
				ac.addBehavior(new AttackBehavior(e));
			}
		}
		
		health += delta;
		
		if(health < stats.getStat("health") / 2) {
			ClothingComponent cc = (ClothingComponent) getMob().getComponent("clothing");
			cc.outfit.add(GameManager.getClothing("bruise_01"));
		}
		if(health < 0) {
			AnimationComponent ac = (AnimationComponent) getMob().getComponent("animation");
			ac.start("dead");
			
			getMob().moving = false;
			getMob().disable();
			
			getMob().getEventManager().add(new DeathEvent(getMob(), e));
		}
	}
	
	public int getHealth() {
		return health;
	}
	
	public void setHealth(int value) {
		health = value;
	}
	
	public int getStat(String name) {
		return stats.getStat(name);
	}

}

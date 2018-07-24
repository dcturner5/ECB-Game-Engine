package com.gammarush.engine.entities.mobs.components;

import com.gammarush.engine.GameManager;
import com.gammarush.engine.entities.mobs.Mob;

public class StatsComponent extends MobComponent {
	
	public static final String NAME = "stats";
	public static final String[] DEPENDENCIES = new String[]{};
	public static final int PRIORITY = 1;
	
	private int health;
	private int maxHealth;
	
	public StatsComponent(Mob mob, int maxHealth) {
		super(NAME, DEPENDENCIES, PRIORITY, mob);
		this.health = maxHealth;
		this.maxHealth = maxHealth;
	}
	
	@Override
	public void update(double delta) {
		if(health < maxHealth / 2) {
			ClothingComponent cc = (ClothingComponent) getMob().getComponent("clothing");
			cc.outfit.add(GameManager.getClothing("bruise_01"));
		}
	}
	
	public void damageHealth(int value) {
		health -= value;
	}
	
	public void restoreHealth(int value) {
		health += value;
	}
	
	public void restoreAllHealth() {
		health = maxHealth;
	}
	
	public int getHealth() {
		return health;
	}
	
	public void setHealth(int value) {
		health = value;
	}

}

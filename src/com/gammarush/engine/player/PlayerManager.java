package com.gammarush.engine.player;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.mobs.components.ControllableComponent;
import com.gammarush.engine.world.WorldManager;

public class PlayerManager {
	
	private WorldManager worldManager;
	private Mob mob;
	
	public PlayerManager(WorldManager worldManager) {
		this.worldManager = worldManager;
		this.worldManager.setPlayerManager(this);
		
		setMob(Game.actors.get("Martín"));
		this.worldManager.getWorld().addMob(mob);
	}
	
	public void update(double delta) {
		worldManager.getRenderer().getCamera().follow(mob);
	}

	public void render() {
		mob.prepare();
		mob.render();
	}
	
	public void prepare() {
		mob.prepare();
	}

	public Mob getMob() {
		return mob;
	}

	public void setMob(Mob mob) {
		this.mob = mob;
		this.mob.addComponent(new ControllableComponent(this.mob));
		this.mob.removeComponent("ai");
	}

}

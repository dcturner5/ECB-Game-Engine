package com.gammarush.engine.player;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.mobs.components.ControllableComponent;
import com.gammarush.engine.math.vector.Vector3f;

public class Player {
	
	private Game game;
	private Mob mob;
	
	public Player(Vector3f position, Game game) {
		this.game = game;
		setMob(new Mob(Game.mobs.get("human"), position, game));
	}
	
	public void update(double delta) {
		mob.update(delta);
		game.renderer.camera.follow(mob);
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

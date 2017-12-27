package com.gammarush.engine.player;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.mobs.human.Human;
import com.gammarush.engine.math.vector.Vector3f;

public class Player {
	
	private Game game;
	private Mob mob;
	
	public Player(Vector3f position, Game game) {
		this.game = game;
		setMob(new Human(position, game));
		mob.setSolid(true);
	}
	
	public void update(double delta) {
		mob.animation.update(mob.moving);
		mob.animation.setDirection(mob.direction);
		mob.control();
		game.renderer.camera.follow(mob);
	}

	public void render() {
		mob.prepare();
		mob.render();
	}
	
	public void prepare() {
		getMob().prepare();
	}

	public Mob getMob() {
		return mob;
	}

	public void setMob(Mob mob) {
		this.mob = mob;
	}

}

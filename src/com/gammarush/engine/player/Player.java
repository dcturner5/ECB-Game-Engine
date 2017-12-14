package com.gammarush.engine.player;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.mobs.human.Human;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.math.vector.Vector3f;

public class Player {
	
	private Game game;
	private Mob mob;
	
	public Player(Vector3f position, Game game) {
		this.game = game;
		setMob(new Human(position, game));
	}
	
	public void update(double delta) {
		mob.updateAnimation();
		mob.control();
		game.renderer.camera.follow(mob);
	}

	public void render(Renderer renderer) {
		Renderer.MOB.enable();
		mob.prepare();
		mob.render(renderer);
		Renderer.MOB.disable();
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

package com.gammarush.engine.player;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.mobs.Human;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.math.vector.Vector3f;

public class Player {
	
	private Game game;
	private Mob mob;
	
	public Player(Vector3f position, Game game) {
		this.game = game;
		mob = new Human(position, game);
	}
	
	public void update(double delta) {
		mob.updateAnimation();
		mob.control();
		game.renderer.camera.follow(mob);
	}

	public void render() {
		Renderer.MOB.enable();
		mob.prepare();
		mob.render();
		Renderer.MOB.disable();
	}
	
	public void prepare() {
		mob.prepare();
	}

}

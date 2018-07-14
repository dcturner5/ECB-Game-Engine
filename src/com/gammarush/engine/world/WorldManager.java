package com.gammarush.engine.world;

import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.player.Player;

public class WorldManager {
	
	private World world;
	private WorldHashMap worlds = new WorldHashMap();
	
	private Player player;
	private Renderer renderer;
	
	public WorldManager() {
		worlds = WorldLoader.load("res/worlds/data.json", this);
		
		world = worlds.get("overworld");
	}
	
	public void update(double delta) {
		getWorld().update(delta);
	}
	
	public void render() {
		getWorld().render();
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public Renderer getRenderer() {
		return renderer;
	}
	
	public World getWorld() {
		return world;
	}
	
	public void setPlayer(Player player) {
		this.player = player;
	}
	
	public void setRenderer(Renderer renderer) {
		this.renderer = renderer;
	}

}

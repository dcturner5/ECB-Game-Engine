package com.gammarush.engine.world;

import java.util.UUID;

import com.gammarush.engine.GameManager;
import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.events.EventManager;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.input.InputManager;
import com.gammarush.engine.player.PlayerManager;
import com.gammarush.engine.quests.QuestManager;
import com.gammarush.engine.scripts.ScriptManager;
import com.gammarush.engine.ui.UIManager;

public class WorldManager {
	
	private GameManager gameManager;
	
	private World world;
	private WorldHashMap worlds = new WorldHashMap();
	
	public WorldManager(GameManager gameManager) {
		this.gameManager = gameManager;
		WorldLoader.load("res/worlds/data.json", this);
	}
	
	public void update(double delta) {
		getWorld().update(delta);
	}
	
	public void render() {
		getWorld().render();
	}
	
	public World getWorld() {
		return world;
	}
	
	public World getWorld(String name) {
		return worlds.get(name);
	}
	
	public void setWorld(String name) {
		if(worlds.get(name) != null) {
			world = worlds.get(name);
		}
	}
	
	public void setWorldHashMap(WorldHashMap worlds) {
		this.worlds = worlds;
	}
	
	public Entity getEntity(UUID uuid) {
		return worlds.getEntity(uuid);
	}
	
	public World getEntityWorld(UUID uuid) {
		return worlds.getEntityWorld(uuid);
	}
	
	public EventManager getEventManager() {
		return gameManager.getEventManager();
	}
	
	public InputManager getInputManager() {
		return gameManager.getInputManager();
	}
	
	public QuestManager getQuestManager() {
		return gameManager.getQuestManager();
	}
	
	public Renderer getRenderer() {
		return gameManager.getRenderer();
	}
	
	public PlayerManager getPlayerManager() {
		return gameManager.getPlayerManager();
	}
	
	public ScriptManager getScriptManager() {
		return gameManager.getScriptManager();
	}
	
	public UIManager getUIManager() {
		return gameManager.getUIManager();
	}

}

package com.gammarush.engine.events;

import com.gammarush.engine.GameManager;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.input.InputManager;
import com.gammarush.engine.player.PlayerManager;
import com.gammarush.engine.quests.QuestManager;
import com.gammarush.engine.scripts.ScriptManager;
import com.gammarush.engine.ui.UIManager;
import com.gammarush.engine.world.WorldManager;

public class EventManager {
	
	private GameManager gameManager;
	
	public EventManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}
	
	public void add(Event event) {
		event.execute(this);
	}
	
	public void execute(String name, Object... parameters) {
		getScriptManager().callMethod("event_" + name, parameters);
	}
	
	public InputManager getInputManager() {
		return gameManager.getInputManager();
	}
	
	public PlayerManager getPlayerManager() {
		return gameManager.getPlayerManager();
	}
	
	public QuestManager getQuestManager() {
		return gameManager.getQuestManager();
	}
	
	public Renderer getRenderer() {
		return gameManager.getRenderer();
	}
	
	public ScriptManager getScriptManager() {
		return gameManager.getScriptManager();
	}
	
	public UIManager getUIManager() {
		return gameManager.getUIManager();
	}
	
	public WorldManager getWorldManager() {
		return gameManager.getWorldManager();
	}

}

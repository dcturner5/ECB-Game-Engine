package com.gammarush.engine;

import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.input.InputManager;
import com.gammarush.engine.player.PlayerManager;
import com.gammarush.engine.quests.QuestManager;
import com.gammarush.engine.scripts.ScriptManager;
import com.gammarush.engine.ui.UIManager;
import com.gammarush.engine.world.WorldManager;

public class GameManager {
	
	private InputManager inputManager;
	private PlayerManager playerManager;
	private QuestManager questManager;
	private Renderer renderer;
	private ScriptManager scriptManager;
	private UIManager uiManager;
	private WorldManager worldManager;
	
	public GameManager(Game game) {
		worldManager = new WorldManager(this);
		scriptManager = new ScriptManager(this);
		renderer = new Renderer(game.width, game.height, game.scale, this);
		uiManager = new UIManager(this);
		inputManager = new InputManager(game.window, this);
		playerManager = new PlayerManager(this);
		questManager = new QuestManager(this);
	}
	
	public void update(double delta) {
		playerManager.update(delta);
		uiManager.update(delta);
		worldManager.update(delta);
	}
	
	public void render() {
		renderer.clear();
		renderer.render();
	}
	
	public InputManager getInputManager() {
		return inputManager;
	}
	
	public PlayerManager getPlayerManager() {
		return playerManager;
	}
	
	public QuestManager getQuestManager() {
		return questManager;
	}
	
	public Renderer getRenderer() {
		return renderer;
	}
	
	public ScriptManager getScriptManager() {
		return scriptManager;
	}
	
	public UIManager getUIManager() {
		return uiManager;
	}
	
	public WorldManager getWorldManager() {
		return worldManager;
	}

}

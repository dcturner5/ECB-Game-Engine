package com.gammarush.engine.player;

import com.gammarush.engine.GameManager;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.mobs.components.ControllableComponent;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.input.InputManager;
import com.gammarush.engine.quests.QuestManager;
import com.gammarush.engine.scripts.ScriptManager;
import com.gammarush.engine.ui.UIManager;
import com.gammarush.engine.world.WorldManager;

public class PlayerManager {
	
	private GameManager gameManager;
	private Mob mob;
	
	public PlayerManager(GameManager gameManager) {
		this.gameManager = gameManager;
		
		setMob(GameManager.getActor("Martín"));
	}
	
	public void update(double delta) {
		getRenderer().getCamera().follow(mob);
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
	
	public InputManager getInputManager() {
		return gameManager.getInputManager();
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

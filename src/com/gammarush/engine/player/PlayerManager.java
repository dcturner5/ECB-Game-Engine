package com.gammarush.engine.player;

import com.gammarush.engine.GameManager;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.mobs.components.AIComponent;
import com.gammarush.engine.entities.mobs.components.ControllableComponent;
import com.gammarush.engine.events.EventManager;
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
		
		setMob(GameManager.getActor("player"));
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
		if(mob != null) {
			//TODO disable components instead of removing and adding
			if(this.mob != null) {
				this.mob.addComponent(new AIComponent(this.mob));
				this.mob.removeComponent("controllable");
			}
			
			this.mob = mob;
			this.mob.addComponent(new ControllableComponent(this.mob));
			this.mob.removeComponent("ai");
		}
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

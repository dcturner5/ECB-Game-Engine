package com.gammarush.engine.quests;

import com.gammarush.axil.memory.AxilMemory;
import com.gammarush.engine.GameManager;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.input.InputManager;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.player.PlayerManager;
import com.gammarush.engine.scripts.ScriptManager;
import com.gammarush.engine.tiles.Tile;
import com.gammarush.engine.ui.UIManager;
import com.gammarush.engine.world.WorldManager;

public class QuestManager {
	
	private GameManager gameManager;
	
	private QuestHashMap quests = new QuestHashMap();
	private DialogueHashMap dialogues = new DialogueHashMap();
	
	public QuestManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}
	
	public void addDialogue(Dialogue dialogue) {
		dialogues.put(dialogue);
	}
	
	public Quest getQuest(String name) {
		return quests.get(name);
	}
	
	public Dialogue getDialogue(String name) {
		return dialogues.get(name);
	}
	
	public void loadScripts() {
		getScriptManager().addMethod("spawn", 4, (int[] args, AxilMemory memory) -> {
			String type = memory.getString(args[0]);
			int x = memory.getInt(args[1]);
			int y = memory.getInt(args[2]);
			Mob e = new Mob(GameManager.getMob(type), new Vector2f(x, y));
			e.setWorld(getWorldManager().getWorld());
			getWorldManager().getWorld().addMob(e);
			return -1;
		});
		
		getScriptManager().addMethod("getX", 1, (int[] args, AxilMemory memory) -> {
			int address = args[0];
			int value = (int) Math.floor(getPlayerManager().getMob().position.x / Tile.WIDTH) * Tile.WIDTH;
			memory.setInt(address, value);
			return -1;
		});
		
		getScriptManager().addMethod("getY", 1, (int[] args, AxilMemory memory) -> {
			int address = args[0];
			int value = (int) Math.floor(getPlayerManager().getMob().position.y / Tile.HEIGHT) * Tile.HEIGHT;
			memory.setInt(address, value);
			return -1;
		});
		
		quests = QuestLoader.load("res/quests/data.json", this);
		for(Quest q : quests.getArray()) {
			getScriptManager().getQueue().add(q.getScriptPath());
		}
		getScriptManager().compile();
	}
	
	public InputManager getInputManager() {
		return gameManager.getInputManager();
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
	
	public WorldManager getWorldManager() {
		return gameManager.getWorldManager();
	}

}

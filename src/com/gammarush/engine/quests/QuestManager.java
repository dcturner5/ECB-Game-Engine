package com.gammarush.engine.quests;

import com.gammarush.axil.memory.AxilMemory;
import com.gammarush.engine.GameManager;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.mobs.components.ClothingComponent;
import com.gammarush.engine.events.EventManager;
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
		getScriptManager().addMethod("console_print", 2, (int[] args, AxilMemory memory) -> {
			String value = memory.getString(args[0]);
			getUIManager().console.print(value);
			return -1;
		});
		
		getScriptManager().addMethod("setPlayer", 2, (int[] args, AxilMemory memory) -> {
			String name = memory.getString(args[0]);
			getPlayerManager().setMob(GameManager.getActor(name));
			return -1;
		});
		
		getScriptManager().addMethod("getWorld", 1, (int[] args, AxilMemory memory) -> {
			int address = args[0];
			String value = getWorldManager().getWorld().getName();
			memory.setString(address, value);
			return -1;
		});
		
		getScriptManager().addMethod("setWorld", 2, (int[] args, AxilMemory memory) -> {
			String name = memory.getString(args[0]);
			getWorldManager().getWorld().removeMob(getPlayerManager().getMob());
			getWorldManager().setWorld(name);
			getWorldManager().getWorld().addMob(getPlayerManager().getMob());
			getPlayerManager().getMob().setWorld(getWorldManager().getWorld());
			return -1;
		});
		
		getScriptManager().addMethod("spawn", 4, (int[] args, AxilMemory memory) -> {
			String type = memory.getString(args[0]);
			int x = memory.getInt(args[1]);
			int y = memory.getInt(args[2]);
			Mob e = new Mob(GameManager.getMob(type), new Vector2f(x, y));
			e.setWorld(getWorldManager().getWorld());
			getWorldManager().getWorld().addMob(e);
			return -1;
		});
		
		getScriptManager().addMethod("printPosition", 1, (int[] args, AxilMemory memory) -> {
			int x = (int) Math.floor(getPlayerManager().getMob().position.x / Tile.WIDTH) * Tile.WIDTH;
			int y = (int) Math.floor(getPlayerManager().getMob().position.y / Tile.HEIGHT) * Tile.HEIGHT;
			getUIManager().console.print(x + ", " + y);
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
		
		getScriptManager().addMethod("addClothing", 3, (int[] args, AxilMemory memory) -> {
			String actorName = memory.getString(args[0]);
			String itemName = memory.getString(args[1]);
			ClothingComponent cc = (ClothingComponent) GameManager.actors.get(actorName).getComponent("clothing");
			cc.outfit.add(GameManager.clothings.get(itemName));
			return -1;
		});
		
		getScriptManager().addMethod("removeClothing", 3, (int[] args, AxilMemory memory) -> {
			String actorName = memory.getString(args[0]);
			String itemName = memory.getString(args[1]);
			ClothingComponent cc = (ClothingComponent) GameManager.actors.get(actorName).getComponent("clothing");
			cc.outfit.remove(GameManager.clothings.get(itemName));
			return -1;
		});
		
		quests = QuestLoader.load("res/quests/data.json", this);
		for(Quest q : quests.getArray()) {
			getScriptManager().getQueue().add(q.getScriptPath());
		}
		getScriptManager().compile();
		
		DialogueLoader.load("res/dialogues/data.json", this);
	}
	
	public EventManager getEventManager() {
		return gameManager.getEventManager();
	}
	
	public InputManager getInputManager() {
		return gameManager.getInputManager();
	}
	
	public PlayerManager getPlayerManager() {
		return gameManager.getPlayerManager();
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

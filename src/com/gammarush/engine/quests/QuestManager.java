package com.gammarush.engine.quests;

import com.gammarush.axil.memory.AxilMemory;
import com.gammarush.engine.Game;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.player.PlayerManager;
import com.gammarush.engine.scripts.ScriptManager;
import com.gammarush.engine.tiles.Tile;
import com.gammarush.engine.world.WorldManager;

public class QuestManager {
	
	private PlayerManager playerManager;
	private ScriptManager scriptManager;
	private WorldManager worldManager;
	
	private QuestHashMap quests = new QuestHashMap();
	private DialogueHashMap dialogues = new DialogueHashMap();
	
	public QuestManager(PlayerManager playerManager, ScriptManager scriptManager, WorldManager worldManager) {
		this.playerManager = playerManager;
		this.scriptManager = scriptManager;
		this.worldManager = worldManager;
		
		getScriptManager().addMethod("spawn", 4, (int[] args, AxilMemory memory) -> {
			String type = memory.getString(args[0]);
			int x = memory.getInt(args[1]);
			int y = memory.getInt(args[2]);
			Mob e = new Mob(Game.mobs.get(type), new Vector2f(x, y));
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
		
		getQuest("main").start();
		//getScriptManager().getUIManager().dialogue.set(getDialogue("main_001"));
		//getScriptManager().getUIManager().dialogue.open();
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
	
	public PlayerManager getPlayerManager() {
		return playerManager;
	}
	
	public ScriptManager getScriptManager() {
		return scriptManager;
	}
	
	public WorldManager getWorldManager() {
		return worldManager;
	}

}

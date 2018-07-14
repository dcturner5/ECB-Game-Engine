package com.gammarush.engine.quests;

import com.gammarush.axil.memory.AxilMemory;
import com.gammarush.engine.Game;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.scripts.ScriptManager;
import com.gammarush.engine.world.WorldManager;

public class QuestManager {
	
	private ScriptManager scriptManager;
	
	private QuestHashMap quests = new QuestHashMap();
	private DialogueHashMap dialogues = new DialogueHashMap();
	
	public QuestManager(ScriptManager scriptManager, WorldManager worldManager) {
		this.scriptManager = scriptManager;
		
		getScriptManager().addMethod("spawn", 4, (int[] args, AxilMemory memory) -> {
			String type = memory.getString(args[0]);
			int x = memory.getInt(args[1]);
			int y = memory.getInt(args[2]);
			Mob e = new Mob(Game.mobs.get(type), new Vector3f(x, y, Renderer.ENTITY_LAYER));
			e.setWorld(worldManager.getWorld());
			worldManager.getWorld().addMob(e);
			return -1;
		});
		
		quests = QuestLoader.load("res/quests.json", this);
		for(Quest q : quests.getArray()) {
			getScriptManager().getLoadQueue().add(q.getScriptPath());
		}
		getScriptManager().load();
		
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
	
	public ScriptManager getScriptManager() {
		return scriptManager;
	}

}

package com.gammarush.engine.quests;

import com.gammarush.engine.scripts.ScriptManager;

public class QuestManager {
	
	private QuestHashMap quests;
	
	private ScriptManager scriptManager;
	
	public QuestManager(ScriptManager scriptManager) {
		this.scriptManager = scriptManager;
		
		quests = QuestLoader.load("res/quests.json", this);
		for(Quest q : quests.getArray()) {
			getScriptManager().getCompileQueue().add(q.getScriptPath());
		}
		getScriptManager().compile();
		
		getScriptManager().getUIManager().dialogue.set(getQuest("main").getDialogues().getRandom());
	}
	
	public Quest getQuest(String name) {
		return quests.get(name);
	}
	
	public ScriptManager getScriptManager() {
		return scriptManager;
	}

}

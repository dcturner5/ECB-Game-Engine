package com.gammarush.engine.quests;

import java.util.ArrayList;

import com.gammarush.axil.AxilScript;
import com.gammarush.engine.utils.json.JSON;

public class Quest {
	
	private int id;
	private String name;
	private String scriptPath;
	private DialogueHashMap dialogues = new DialogueHashMap();
	
	private QuestManager questManager;
	
	public Quest(int id, JSON json, QuestManager questManager) {
		this.id = id;
		this.name = json.getString("name");
		this.scriptPath = json.getString("script");
		this.questManager = questManager;
		
		ArrayList<JSON> dialogueArray = json.getArray("dialogues");
		for(int i = 0; i < dialogueArray.size(); i++) {
			JSON dialogueJson = dialogueArray.get(i);
			dialogues.put(new Dialogue(i, dialogueJson));
		}
	}
	
	public void start() {
		getScript().run();
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public DialogueHashMap getDialogues() {
		return dialogues;
	}
	
	public AxilScript getScript() {
		return questManager.getScriptManager().getScript(getScriptPath() + ".axil");
	}
	
	public String getScriptPath() {
		return scriptPath;
	}
	
	public void setScript() {
		
	}
	
}

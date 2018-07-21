package com.gammarush.engine.quests;

import com.gammarush.engine.utils.json.JSON;

public class DialogueOption {
	
	public enum OptionType {
		DEFAULT, PROGRESS, EXIT
	}
	
	private QuestManager questManager;
	
	private String name;
	private String text;
	private OptionType type;
	private String link;
	
	public DialogueOption(JSON json, QuestManager questManager) {
		type = OptionType.DEFAULT;
		String typeJson = json.getString("type");
		if(typeJson != null) {
			if(typeJson.equals("exit")) {
				type = OptionType.EXIT;
			}
		}
		
		name = json.getString("name");
		text = json.getString("text");
		link = json.getString("link");
		if(link == null) link = "";
			
		this.questManager = questManager;
	}
	
	public String getName() {
		return name;
	}
	
	public String getText() {
		return text;
	}
	
	public OptionType getType() {
		return type;
	}
	
	public Dialogue getLink() {
		if(link.equals("")) return null;
		return questManager.getDialogue(link);
	}

	public QuestManager getQuestManager() {
		return questManager;
	}
	
}


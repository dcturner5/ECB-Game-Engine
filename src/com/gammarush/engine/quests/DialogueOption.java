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
		OptionType type = OptionType.DEFAULT;
		switch(json.getString("type")) {
		case "progress":
			type = OptionType.PROGRESS;
			break;
		case "exit":
			type = OptionType.EXIT;
			break;
		}
		
		this.name = json.getString("name");
		this.text = json.getString("text");
		this.type = type;
		this.link = json.getString("link");
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


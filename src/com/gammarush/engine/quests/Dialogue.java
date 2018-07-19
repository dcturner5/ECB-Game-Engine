package com.gammarush.engine.quests;

import java.util.ArrayList;

import com.gammarush.engine.utils.json.JSON;

public class Dialogue {
	
	private QuestManager questManager;
	
	private int id;
	private String name;
	private String text;
	private ArrayList<DialogueOption> options = new ArrayList<DialogueOption>();
	
	public Dialogue(int id, JSON json, QuestManager questManager) {
		this.id = id;
		this.questManager = questManager;
		name = json.getString("name");
		text = json.getString("text");
		ArrayList<JSON> optionArray = json.getArray("options");
		for(JSON optionJson : optionArray) {
			options.add(new DialogueOption(optionJson, questManager));
		}
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public String getText() {
		return text;
	}
	
	public ArrayList<DialogueOption> getOptions() {
		return options;
	}
	
	public QuestManager getQuestManager() {
		return questManager;
	}
	
}

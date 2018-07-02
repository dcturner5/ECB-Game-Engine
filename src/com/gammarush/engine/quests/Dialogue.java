package com.gammarush.engine.quests;

import java.util.ArrayList;

import com.gammarush.engine.quests.DialogueOption.OptionType;
import com.gammarush.engine.utils.json.JSON;

public class Dialogue {
	
	private int id;
	private String name;
	private String text;
	private ArrayList<DialogueOption> options = new ArrayList<DialogueOption>();
	
	public Dialogue(int id, JSON json) {
		name = json.getString("name");
		text = json.getString("text");
		ArrayList<JSON> optionArray = json.getArray("options");
		for(JSON o : optionArray) {
			OptionType optionType = OptionType.DEFAULT;
			switch(o.getString("type")) {
			case "progress":
				optionType = OptionType.PROGRESS;
				break;
			case "exit":
				optionType = OptionType.EXIT;
				break;
			}
			options.add(new DialogueOption(o.getString("text"), optionType, o.getString("link")));
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
	
}

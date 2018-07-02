package com.gammarush.engine.quests;

public class DialogueOption {
	
	public enum OptionType {
		DEFAULT, PROGRESS, EXIT
	}
	
	private String text;
	private OptionType type;
	private String link;
	
	public DialogueOption(String text, OptionType type, String link) {
		this.text = text;
		this.type = type;
		this.link = link;
	}
	
	public String getText() {
		return text;
	}
	
	public OptionType getType() {
		return type;
	}
	
	public String getLink() {
		return link;
	}
	
}


package com.gammarush.engine.entities.mobs.actors;

import java.util.ArrayList;

import com.gammarush.engine.GameManager;
import com.gammarush.engine.entities.items.clothing.ClothingTemplate;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.mobs.actors.components.NameTagComponent;
import com.gammarush.engine.entities.mobs.behaviors.InteractWithMobBehavior;
import com.gammarush.engine.entities.mobs.components.ClothingComponent;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.quests.Dialogue;
import com.gammarush.engine.quests.QuestManager;
import com.gammarush.engine.ui.containers.UIDialogue;
import com.gammarush.engine.utils.json.JSON;

public class Actor extends Mob {
	
	private int id;
	private String name;
	private String displayName;
	private String dialogueLink;
	
	public Actor(int id, JSON json, QuestManager questManager) {
		super(GameManager.getMob(json.getString("type")), new Vector2f());
		
		this.id = id;
		this.name = json.getString("name");
		
		if(json.exists("displayName")) {
			displayName = json.getString("displayName");
			addComponent(new NameTagComponent(this));
		}
		
		
		if(json.getArray("clothes") != null) {
			for(String clothesName : json.getStringArray("clothes")) {
				ClothingTemplate t = GameManager.getClothing(clothesName);
				((ClothingComponent) getComponent("clothing")).outfit.add(t);
			}
		}
		
		if(json.getJSON("colors") != null) {
			String colorName = json.getString("colors.color");
			if(colorName != null) this.color = getTemplate().getColor(colorName);
			String hairColorName = json.getString("colors.hairColor");
			if(hairColorName != null) this.hairColor = getTemplate().getHairColor(hairColorName);
		}
		
		if(json.getString("dialogue") != null) {
			dialogueLink = json.getString("dialogue");
		}
		
		ArrayList<JSON> dialogueArray = json.getArray("dialogues");
		if(dialogueArray != null) {
			for(int i = 0; i < dialogueArray.size(); i++) {
				JSON dialogueJson = dialogueArray.get(i);
				questManager.addDialogue(new Dialogue(i, dialogueJson, questManager));
			}
		}
	}
	
	@Override
	public void activate(Mob e) {
		Dialogue dialogue = getQuestManager().getDialogue(dialogueLink);
		if(dialogue != null) {
			getAIComponent().addBehavior(new InteractWithMobBehavior(e));
			UIDialogue dialogueUI = getUIManager().dialogue;
			dialogueUI.set(dialogue);
			dialogueUI.open();
		}
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDisplayName() {
		return displayName;
	}

}

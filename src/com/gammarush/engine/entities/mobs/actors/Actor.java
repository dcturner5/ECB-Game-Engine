package com.gammarush.engine.entities.mobs.actors;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.items.clothing.ClothingTemplate;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.mobs.components.ClothingComponent;
import com.gammarush.engine.entities.mobs.components.NameTagComponent;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.tiles.Tile;
import com.gammarush.engine.ui.containers.UIDialogue;
import com.gammarush.engine.utils.json.JSON;

public class Actor extends Mob {
	
	private int id;
	private String name;
	
	public Actor(int id, JSON json) {
		super(Game.mobs.get(json.getString("type")), new Vector2f(0, 3 * Tile.HEIGHT));
		
		this.id = id;
		this.name = json.getString("name");
		
		if(json.getArray("clothes") != null) {
			for(String clothesName : json.getStringArray("clothes")) {
				ClothingTemplate t = Game.clothings.get(clothesName);
				((ClothingComponent) getComponent("clothing")).outfit.add(t);
			}
		}
		
		if(json.getJSON("colors") != null) {
			String colorName = json.getString("colors.color");
			if(colorName != null) this.color = getTemplate().getColor(colorName);
			String hairColorName = json.getString("colors.hairColor");
			if(hairColorName != null) this.hairColor = getTemplate().getHairColor(hairColorName);
		}
		
		addComponent(new NameTagComponent(this));
	}
	
	@Override
	public void activate(Mob e) {
		UIDialogue dialogue = getUIManager().dialogue;
		dialogue.set(getQuestManager().getDialogue("main_001"));
		dialogue.open();
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

}

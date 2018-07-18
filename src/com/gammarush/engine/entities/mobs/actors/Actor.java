package com.gammarush.engine.entities.mobs.actors;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.items.clothing.ClothingTemplate;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.mobs.components.ClothingComponent;
import com.gammarush.engine.entities.mobs.components.NameTagComponent;
import com.gammarush.engine.graphics.Color;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.math.vector.Vector4f;
import com.gammarush.engine.tiles.Tile;
import com.gammarush.engine.ui.fonts.Font;
import com.gammarush.engine.utils.json.JSON;

public class Actor extends Mob {
	
	private int id;
	private String name;
	private String type;
	
	public Actor(int id, JSON json) {
		super(Game.mobs.get(json.getString("type")), new Vector2f(0, 3 * Tile.HEIGHT));
		
		this.id = id;
		this.name = json.getString("name");
		this.type = json.getString("type");
		
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
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}

}

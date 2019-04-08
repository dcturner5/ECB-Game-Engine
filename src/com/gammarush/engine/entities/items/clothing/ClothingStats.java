package com.gammarush.engine.entities.items.clothing;

import com.gammarush.engine.entities.EntityStats;
import com.gammarush.engine.utils.json.JSON;

public class ClothingStats extends EntityStats {
	
	public static final String[] NAMES = {"attack", "defense", "range"};
	
	public ClothingStats() {
		super();
		for(String name : NAMES) {
			setStat(name, 0);
		}
	}
	
	public ClothingStats(JSON json) {
		super();
		for(String name : NAMES) {
			setStat(name, json.exists("clothing.stats." + name) ? json.getInteger("clothing.stats." + name) : 0);
		}
	}

}

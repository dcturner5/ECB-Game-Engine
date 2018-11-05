package com.gammarush.engine.entities.mobs;

import com.gammarush.engine.entities.EntityStats;
import com.gammarush.engine.utils.json.JSON;

public class MobStats extends EntityStats {
	
	public static final String[] NAMES = {"health", "attack.damage", "attack.range", "attack.cooldown", "defense"};

	public MobStats() {
		super();
		for(String name : NAMES) {
			setStat(name, 0);
		}
	}
	
	public MobStats(JSON json) {
		super();
		for(String name : NAMES) {
			setStat(name, json.exists(name) ? json.getInteger(name) : 0);
		}
	}
}

package com.gammarush.engine.entities.items.clothing;

import java.util.HashMap;

import com.gammarush.axil.AxilScript;
import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.scripts.ScriptManager;
import com.gammarush.engine.utils.json.JSON;

public class ClothingScripts {
	
	public static final String[] NAMES = {"attack", "defense"};
	
	private ScriptManager scriptManager;
	private HashMap<String, String> scripts = new HashMap<String, String>();
	
	public ClothingScripts() {
		for(String name : NAMES) {
			scripts.put(name, null);
		}
	}
	
	public ClothingScripts(JSON json, ScriptManager scriptManager) {
		this.scriptManager = scriptManager;
		for(String name : NAMES) {
			if(json.exists("clothing.scripts." + name)) {
				String scriptName = json.getString("clothing.scripts." + name);
				scripts.put(name, scriptName);
				scriptManager.getQueue().add(scriptName);
			}
			else {
				scripts.put(name, null);
			}
		}
	}
	
	public void activate(String name, Entity e) {
		String scriptName = scripts.get(name);
		if(scriptName != null) {
			AxilScript script = scriptManager.getScript(scriptName + ".axil");
			if(script != null) {
				script.call("main", e.getUUID().toString());
			}
		}
	}
	
}

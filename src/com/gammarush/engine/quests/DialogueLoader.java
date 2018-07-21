package com.gammarush.engine.quests;

import java.util.ArrayList;

import com.gammarush.engine.utils.json.JSON;
import com.gammarush.engine.utils.json.JSONLoader;

public class DialogueLoader {
	
	public static void load(String path, QuestManager questManager) {
		JSON json = JSONLoader.load(path);
		ArrayList<JSON> array = json.getArray("dialogues");
		for(int i = 0; i < array.size(); i++) {
			JSON element = array.get(i);
			questManager.addDialogue(new Dialogue(i, element, questManager));
		}
	}
	
}

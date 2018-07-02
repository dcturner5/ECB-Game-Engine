package com.gammarush.engine.quests;

import java.util.ArrayList;

import com.gammarush.engine.utils.json.JSON;
import com.gammarush.engine.utils.json.JSONLoader;

public class QuestLoader {
	
	public static QuestHashMap load(String path, QuestManager questManager) {
		QuestHashMap result = new QuestHashMap();
		JSON json = JSONLoader.load(path);
		
		ArrayList<JSON> array = json.getArray("quests");
		for(int i = 0; i < array.size(); i++) {
			JSON element = array.get(i);
			result.put(new Quest(i, element, questManager));
		}
		
		return result;
	}

}

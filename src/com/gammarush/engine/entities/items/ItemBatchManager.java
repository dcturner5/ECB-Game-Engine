package com.gammarush.engine.entities.items;

import java.util.ArrayList;

import com.gammarush.engine.Game;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector3f;

public class ItemBatchManager {
	
	public ArrayList<ItemBatch> batches = new ArrayList<ItemBatch>();
	
	public void add(ItemTemplate template, Vector3f position, Vector2f offset) {
		ItemBatch batch = null;
		boolean exists = false;
		for(ItemBatch b : batches) {
			if(b.id == template.getId()) {
				batch = b;
				exists = true;
			}
		}
		if(!exists) {
			batch = new ItemBatch(template.getId());
			batches.add(batch);
		}
		
		batch.add(position, offset);
	}
	
	public void render() {
		for(ItemBatch b : batches) {
			ItemTemplate t = Game.items.get(b.id);
			t.render(b);
		}
		batches.clear();
	}

}

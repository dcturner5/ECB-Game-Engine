package com.gammarush.engine.entities.items;

import java.util.ArrayList;

import com.gammarush.engine.GameManager;

public class ItemBatchManager {
	
	public ArrayList<ItemBatch> batches = new ArrayList<ItemBatch>();
	
	public void add(Item item) {
		ItemBatch batch = null;
		boolean exists = false;
		for(ItemBatch b : batches) {
			if(b.getId() == item.getTemplate().getId()) {
				batch = b;
				exists = true;
			}
		}
		if(!exists) {
			batch = new ItemBatch(item.getTemplate().getId());
			batches.add(batch);
		}
		
		batch.add(item.position, item.getOffset());
	}
	
	public void process(ArrayList<Item> items) {
		for(Item e : items) {
			if(!e.getScreenPresence()) continue;
			add(e);
		}
	}
	
	public void render() {
		for(ItemBatch b : batches) {
			ItemTemplate t = GameManager.getItem(b.getId());
			t.render(b);
		}
		batches.clear();
	}

}

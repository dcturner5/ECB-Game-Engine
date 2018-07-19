package com.gammarush.engine.entities.statics;

import java.util.ArrayList;

import com.gammarush.engine.GameManager;

public class StaticBatchManager {
	
	private ArrayList<StaticBatch> batches = new ArrayList<StaticBatch>();
	
	public void add(Static e) {
		StaticBatch batch = null;
		boolean exists = false;
		for(StaticBatch b : batches) {
			if(b.getId() == e.getTemplate().getId()) {
				batch = b;
				exists = true;
			}
		}
		if(!exists) {
			batch = new StaticBatch(e.getTemplate().getId());
			batches.add(batch);
		}
		batch.add(e.position, e.getColor());
	}
	
	public void process(ArrayList<Static> statics) {
		for(Static e : statics) {
			if(!e.getScreenPresence()) continue;
			e.render();
			e.renderComponents();
			add(e);
		}
	}
	
	public void render() {
		for(StaticBatch b : batches) {
			StaticTemplate t = GameManager.getStatic(b.getId());
			t.render(b);
		}
		batches.clear();
	}

}

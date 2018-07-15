package com.gammarush.engine.entities.mobs;

import java.util.ArrayList;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.components.AnimationComponent;

public class MobBatchManager {
	
	public ArrayList<MobBatch> batches = new ArrayList<MobBatch>();
	
	public void add(Mob e) {
		MobBatch batch = null;
		boolean exists = false;
		for(MobBatch b : batches) {
			if(b.getId() == e.getTemplate().getId()) {
				batch = b;
				exists = true;
			}
		}
		if(!exists) {
			batch = new MobBatch(e.getTemplate().getId());
			batches.add(batch);
		}
		
		AnimationComponent ac = (AnimationComponent) e.getComponent("animation");
		batch.add(e.position, ac.getAnimation(), e.color);
	}
	
	public void process(ArrayList<Mob> mobs) {
		for(Mob e : mobs) {
			if(!e.getScreenPresence()) continue;
			e.render();
			e.renderComponents();
			add(e);
		}
	}
	
	public void render() {
		for(MobBatch b : batches) {
			MobTemplate t = Game.mobs.get(b.getId());
			t.render(b);
		}
		batches.clear();
	}

}

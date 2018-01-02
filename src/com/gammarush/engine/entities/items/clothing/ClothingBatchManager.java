package com.gammarush.engine.entities.items.clothing;

import java.util.ArrayList;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.animations.Animation;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.math.vector.Vector4f;

public class ClothingBatchManager {
	
	public ArrayList<ClothingBatch> batches = new ArrayList<ClothingBatch>();
	
	public void add(ClothingTemplate template, Vector3f position, Animation animation, Vector4f[] color) {
		ClothingBatch batch = null;
		boolean exists = false;
		for(ClothingBatch b : batches) {
			if(b.id == template.getId()) {
				batch = b;
				exists = true;
			}
		}
		if(!exists) {
			batch = new ClothingBatch(template.getId(), template.getLayer());
			batches.add(batch);
		}
		
		batch.add(position, animation, color);
	}
	
	public void render() {
		for(ClothingBatch b : batches) {
			ClothingTemplate t = Game.clothings.get(b.id);
			t.render(b);
		}
		batches.clear();
	}

}

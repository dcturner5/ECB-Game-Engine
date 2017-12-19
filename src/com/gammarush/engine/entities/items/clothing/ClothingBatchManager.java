package com.gammarush.engine.entities.items.clothing;

import java.util.ArrayList;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.mobs.animations.AnimationData;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.math.vector.Vector4f;

public class ClothingBatchManager {
	
	public ArrayList<ClothingBatch> batches = new ArrayList<ClothingBatch>();
	
	public void add(Clothing clothing, Vector3f position, AnimationData animation, Vector4f[] color) {
		ClothingBatch batch = null;
		boolean exists = false;
		for(ClothingBatch b : batches) {
			if(b.id == clothing.getId()) {
				batch = b;
				exists = true;
			}
		}
		if(!exists) {
			batch = new ClothingBatch(clothing.getId(), clothing.getLayer());
			batches.add(batch);
		}
		
		batch.add(position, animation, color);
	}
	
	public void render() {
		for(ClothingBatch b : batches) {
			Clothing c = Game.clothings.get(b.id);
			c.render(b);
		}
		batches.clear();
	}

}
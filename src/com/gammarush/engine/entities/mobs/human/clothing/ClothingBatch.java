package com.gammarush.engine.entities.mobs.human.clothing;

import java.util.ArrayList;

import com.gammarush.engine.entities.mobs.animations.AnimationData;
import com.gammarush.engine.math.vector.Vector3f;

public class ClothingBatch {
	
	public int id;
	public int layer;
	
	public ArrayList<Vector3f> positions = new ArrayList<Vector3f>();
	public ArrayList<AnimationData> animations = new ArrayList<AnimationData>();
	
	public ClothingBatch(int id, int layer) {
		this.id = id;
		this.layer = layer;
	}

	public void add(Vector3f position, AnimationData animation) {
		positions.add(position);
		animations.add(animation);
	}
	
}

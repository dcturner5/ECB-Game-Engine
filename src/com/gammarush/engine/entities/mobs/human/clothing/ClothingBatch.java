package com.gammarush.engine.entities.mobs.human.clothing;

import java.util.ArrayList;

import com.gammarush.engine.entities.mobs.animations.AnimationData;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.math.vector.Vector4f;

public class ClothingBatch {
	
	public int id;
	public int layer;
	
	public ArrayList<Vector3f> positions = new ArrayList<Vector3f>();
	public ArrayList<AnimationData> animations = new ArrayList<AnimationData>();
	public ArrayList<Vector4f[]> colors = new ArrayList<Vector4f[]>();
	
	public ClothingBatch(int id, int layer) {
		this.id = id;
		this.layer = layer;
	}

	public void add(Vector3f position, AnimationData animation, Vector4f[] color) {
		positions.add(position);
		animations.add(animation);
		colors.add(color);
	}
	
}

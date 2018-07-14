package com.gammarush.engine.entities.items.clothing;

import java.util.ArrayList;

import com.gammarush.engine.entities.animations.Animation;
import com.gammarush.engine.entities.Color;
import com.gammarush.engine.math.vector.Vector3f;

public class ClothingBatch {
	
	public int id;
	public int layer;
	
	public ArrayList<Vector3f> positions = new ArrayList<Vector3f>();
	public ArrayList<Animation> animations = new ArrayList<Animation>();
	public ArrayList<Color> colors = new ArrayList<Color>();
	
	public ClothingBatch(int id, int layer) {
		this.id = id;
		this.layer = layer;
	}

	public void add(Vector3f position, Animation animation, Color color) {
		positions.add(position);
		animations.add(animation);
		colors.add(color);
	}
	
}

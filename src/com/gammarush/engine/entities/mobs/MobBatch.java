package com.gammarush.engine.entities.mobs;

import java.util.ArrayList;

import com.gammarush.engine.entities.Color;
import com.gammarush.engine.entities.animations.Animation;
import com.gammarush.engine.graphics.Batch;
import com.gammarush.engine.math.vector.Vector3f;

public class MobBatch extends Batch {

	public ArrayList<Vector3f> positions = new ArrayList<Vector3f>();
	public ArrayList<Animation> animations = new ArrayList<Animation>();
	public ArrayList<Color> colors = new ArrayList<Color>();
	
	public MobBatch(int id) {
		super(id);
	}
	
	public void add(Vector3f position, Animation animation, Color color) {
		positions.add(position);
		animations.add(animation);
		colors.add(color);
	}

}

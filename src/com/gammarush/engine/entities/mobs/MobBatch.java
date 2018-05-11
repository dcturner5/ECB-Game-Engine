package com.gammarush.engine.entities.mobs;

import java.util.ArrayList;

import com.gammarush.engine.entities.animations.Animation;
import com.gammarush.engine.graphics.Batch;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.math.vector.Vector4f;

public class MobBatch extends Batch {

	public ArrayList<Vector3f> positions = new ArrayList<Vector3f>();
	public ArrayList<Animation> animations = new ArrayList<Animation>();
	public ArrayList<Vector4f[]> colors = new ArrayList<Vector4f[]>();
	
	public MobBatch(int id) {
		super(id);
	}
	
	public void add(Vector3f position, Animation animation, Vector4f[] color) {
		positions.add(position);
		animations.add(animation);
		colors.add(color);
	}

}

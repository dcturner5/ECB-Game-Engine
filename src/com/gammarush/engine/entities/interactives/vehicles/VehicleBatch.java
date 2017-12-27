package com.gammarush.engine.entities.interactives.vehicles;

import java.util.ArrayList;

import com.gammarush.engine.entities.mobs.animations.AnimationData;
import com.gammarush.engine.graphics.Batch;
import com.gammarush.engine.math.vector.Vector3f;

public class VehicleBatch extends Batch {
	
	public VehicleBatch(int id) {
		super(id);
	}
	
	public ArrayList<Vector3f> positions = new ArrayList<Vector3f>();
	public ArrayList<AnimationData> animations = new ArrayList<AnimationData>();
	
	public void add(Vector3f position, AnimationData animation) {
		positions.add(position);
		animations.add(animation);
	}

}

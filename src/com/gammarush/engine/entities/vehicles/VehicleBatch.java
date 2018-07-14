package com.gammarush.engine.entities.vehicles;

import java.util.ArrayList;

import com.gammarush.engine.entities.animations.Animation;
import com.gammarush.engine.graphics.Batch;
import com.gammarush.engine.math.vector.Vector3f;

public class VehicleBatch extends Batch {
	
	public VehicleBatch(int id) {
		super(id);
	}
	
	public ArrayList<Vector3f> positions = new ArrayList<Vector3f>();
	public ArrayList<Animation> animations = new ArrayList<Animation>();
	
	public void add(Vector3f position, Animation animation) {
		positions.add(position);
		animations.add(animation);
	}

}

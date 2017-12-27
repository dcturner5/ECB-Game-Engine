package com.gammarush.engine.entities.interactives.vehicles;

import java.util.ArrayList;

import com.gammarush.engine.graphics.Batch;
import com.gammarush.engine.math.vector.Vector3f;

public class WheelBatch extends Batch {
	
	public int id;
	
	public ArrayList<Vector3f> positions = new ArrayList<Vector3f>();
	public ArrayList<Integer> sizes = new ArrayList<Integer>();
	public ArrayList<Float> rotations = new ArrayList<Float>();
	
	public WheelBatch(int id) {
		super(id);
	}
	
	public void add(Vector3f position, int size, float rotation) {
		positions.add(position);
		sizes.add(size);
		rotations.add(rotation);
	}

}

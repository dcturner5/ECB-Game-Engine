package com.gammarush.engine.entities.statics;

import java.util.ArrayList;

import com.gammarush.engine.entities.Color;
import com.gammarush.engine.graphics.Batch;
import com.gammarush.engine.math.vector.Vector3f;

public class StaticBatch extends Batch {
	
	public ArrayList<Vector3f> positions = new ArrayList<Vector3f>();
	public ArrayList<Color> colors = new ArrayList<Color>();
	
	public StaticBatch(int id) {
		super(id);
	}
	
	public void add(Vector3f position, Color color) {
		positions.add(position);
		colors.add(color);
	}

}

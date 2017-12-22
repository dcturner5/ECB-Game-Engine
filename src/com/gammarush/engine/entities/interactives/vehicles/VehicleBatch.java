package com.gammarush.engine.entities.interactives.vehicles;

import java.util.ArrayList;

import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.math.vector.Vector2f;

public class VehicleBatch {
	
	public Model exteriorModel;
	public Model interiorModel;
	public Model wheelModel;
	
	public ArrayList<Vector2f> positions = new ArrayList<Vector2f>();
	
	public void add(Vector2f position) {
		positions.add(position);
	}

}

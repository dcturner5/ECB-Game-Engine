package com.gammarush.engine.entities.interactives.vehicles;

import java.util.ArrayList;

import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.math.vector.Vector2f;

public class WheelBatch {
	
	public int id;
	public Model model;
	
	public ArrayList<Vector2f> positions = new ArrayList<Vector2f>();
	public ArrayList<Vector2f> dimensions = new ArrayList<Vector2f>();
	public ArrayList<Float> angles = new ArrayList<Float>();
	
	public WheelBatch(Model model) {
		
	}
	
	public void add(Vector2f positions, int width, int height, float angle) {
		
	}

}

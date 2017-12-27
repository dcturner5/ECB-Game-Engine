package com.gammarush.engine.entities.items;

import java.util.ArrayList;

import com.gammarush.engine.graphics.Batch;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector3f;

public class ItemBatch extends Batch {
	
	public ArrayList<Vector3f> positions = new ArrayList<Vector3f>();
	public ArrayList<Vector2f> offsets = new ArrayList<Vector2f>();
	
	public ItemBatch(int id) {
		super(id);
	}
	
	public void add(Vector3f position, Vector2f offset) {
		positions.add(position);
		offsets.add(offset);
	}
	
}

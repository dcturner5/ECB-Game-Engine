package com.gammarush.engine.tiles;

import java.util.ArrayList;

import com.gammarush.engine.graphics.Batch;
import com.gammarush.engine.math.vector.Vector3f;

public class TileBatch extends Batch {
	
	private boolean blend;
	public ArrayList<Vector3f> positions = new ArrayList<Vector3f>();
	public ArrayList<BlendData> blendDatas = new ArrayList<BlendData>();
	
	public TileBatch(int id, boolean blend) {
		super(id);
		this.blend = blend;
	}
	
	public void add(Vector3f position, BlendData blendData) {
		positions.add(position);
		if(blend) blendDatas.add(blendData);
	}
	
	public boolean getBlend() {
		return blend;
	}

}

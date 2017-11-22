package com.gammarush.engine.tiles;

import java.util.ArrayList;

import com.gammarush.engine.math.vector.Vector3f;

public class TileBatch {
	
	public int id;
	public boolean blend;
	public ArrayList<Vector3f> positions = new ArrayList<Vector3f>();
	public ArrayList<BlendData> blendDatas = new ArrayList<BlendData>();
	
	public TileBatch(int id, boolean blend) {
		this.id = id;
		this.blend = blend;
	}

}

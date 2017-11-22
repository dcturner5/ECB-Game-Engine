package com.gammarush.engine.tiles;

public class BlendData {
	
	public Tile tile;
	public int[] indices = new int[8];
	
	public BlendData(Tile tile, int[] indices) {
		this.tile = tile;
		this.indices = indices;
	}

}

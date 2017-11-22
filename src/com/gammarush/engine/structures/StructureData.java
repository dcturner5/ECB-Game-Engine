package com.gammarush.engine.structures;

import com.gammarush.engine.math.vector.Vector2i;
import com.gammarush.engine.tiles.Tile;

public class StructureData {
	
	public Structure structure;
	
	public String name;
	public int width;
	public int height;
	public int layer;
	public int baseHeight;
	public int baseOffset;
	public int[] baseCollisionArray;
	public int[] topCollisionArray;
	public Vector2i placement;
	
	public int tileWidth;
	public int tileHeight;
	
	public StructureData(Structure structure, String name, int width, int height, int layer, int baseHeight, int baseOffset, int[] baseCollisionArray, int[] topCollisionArray, Vector2i placement) {
		this.structure = structure;
		this.name = name;
		this.width = width;
		this.height = height;
		this.layer = layer;
		this.baseHeight = baseHeight;
		this.baseOffset = baseOffset;
		this.baseCollisionArray = baseCollisionArray;
		this.topCollisionArray = topCollisionArray;
		this.placement = placement;
		
		this.tileWidth = width / Tile.WIDTH;
		this.tileHeight = height / Tile.HEIGHT;
	}

}

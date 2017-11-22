package com.gammarush.engine.world;

import com.gammarush.engine.tiles.Tile;

public class TerrainEditor {
	
	private World world;
	
	public int layer = 0;
	public int radius = 1;
	
	public TerrainEditor(World world) {
		this.world = world;
	}
	
	public void setElevation(int x, int y) {
		for(int i = -radius; i <= radius; i++) {
			int x1 = x + i;
			for(int j = -radius; j <= radius; j++) {
				int y1 = y + j;
				world.setElevation(World.LAYERS[Math.max(Math.min(layer, World.LAYERS.length - 1), 0)] + .0001f, x1, y1);
			}
		}
		
		int octaves = 5;
		for(int i = 0; i < octaves; i++) {
			for(int x1 = 0; x1 < world.width; x1++) {
				for(int y1 = 0; y1 < world.height; y1++) {
					float value = world.getElevation(x1, y1);
					float up = world.getElevation(x1, y1 - 1);
					float down = world.getElevation(x1, y1 + 1);
					float left = world.getElevation(x1 - 1, y1);
					float right = world.getElevation(x1 + 1, y1);
					float[] neighbors = new float[] {up, down, left, right};
					
					for(int j = 0; j < World.LAYERS.length; j++) {
						float layer = World.LAYERS[j];
						if(value < layer) continue;
						
						int sum = 0;
						for(int k = 0; k < neighbors.length; k++) {
							if(neighbors[k] > layer) sum++;
						}
						
						if(sum < 2) world.setElevation(layer - .0001f, x1, y1);
						else {
							if(up < layer && down < layer && left > layer && right > layer) world.setElevation(layer - .0001f, x1, y1);
							if(up > layer && down > layer && left < layer && right < layer) world.setElevation(layer - .0001f, x1, y1);
						}
					}
				}
			}
		}
		
		for(int x2 = 0; x2 < this.world.width; x2++) {
			for(int y2 = 0; y2 < this.world.height; y2++) {
				float elevation = this.world.getElevation(x2, y2);
				
				int biomeId = this.world.getBiome(x2, y2);
				
				if(biomeId == Biome.PLAINS.id) this.world.setTile(Tile.PLAINS_GROUND, x2, y2);
				if(biomeId == Biome.FOREST.id) this.world.setTile(Tile.FOREST_GROUND, x2, y2);
				if(biomeId == Biome.DESERT.id) this.world.setTile(Tile.DESERT_GROUND, x2, y2);
				
				this.world.generateCliff(x2, y2, World.LAYERS);
				
				if(elevation < World.WATER_LEVEL) this.world.setTile(Tile.WATER, x2, y2);
			}
		}
		
	}
}

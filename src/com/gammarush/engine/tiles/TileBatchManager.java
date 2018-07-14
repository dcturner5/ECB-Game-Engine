package com.gammarush.engine.tiles;

import java.util.ArrayList;

import com.gammarush.engine.Game;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.math.vector.Vector2i;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.world.Chunk;

public class TileBatchManager {
	
	ArrayList<TileBatch> batches = new ArrayList<TileBatch>();
	
	public void process(Chunk chunk) {
		Renderer renderer = chunk.getWorld().getRenderer();
		Vector2i cp = chunk.getWorldPosition();
		int wx = (int) Math.floor(-renderer.getCamera().position.x / Tile.WIDTH);
		int wy = (int) Math.floor(-renderer.getCamera().position.y / Tile.HEIGHT);
		
		int fx = Math.max(wx - cp.x, 0);
		int lx = (int) Math.min(wx + (renderer.getWidth() / renderer.getCamera().getZoom() / Tile.WIDTH) - cp.x + 1, Chunk.WIDTH);
		int fy = Math.max(wy - cp.y, 0);
		int ly = (int) Math.min(wy + (renderer.getHeight() / renderer.getCamera().getZoom() / Tile.HEIGHT) - cp.y + 2, Chunk.HEIGHT);
	
		if(lx <= 0 || ly <= 0) {
			return;
		}
		
		for(int x = fx; x < lx; x++) {
			for(int y = fy; y < ly; y++) {
				int id = chunk.getTile(x, y);
				Tile tile = Game.tiles.get(id);
				if(tile == null) continue;
				
				Tile tilef = null;
				boolean blend = false;
				int[] blendIndices = new int[8];
				
				if(tile.getBlendWeight() == null) {
					int[] ids = new int[8];
					int[] idCount = new int[8];
					
					ids[0] = chunk.getTile(x - 1, y);
					ids[1] = chunk.getTile(x, y - 1);
					ids[2] = chunk.getTile(x + 1, y);
					ids[3] = chunk.getTile(x, y + 1);
					ids[4] = chunk.getTile(x - 1, y - 1);
					ids[5] = chunk.getTile(x + 1, y - 1);
					ids[6] = chunk.getTile(x + 1, y + 1);
					ids[7] = chunk.getTile(x - 1, y + 1);
					
					ArrayList<Tile> tiles = new ArrayList<Tile>(8);
					for(int i = 0; i < ids.length; i++) {
						Tile neighborTile = Game.tiles.get(ids[i]);
						if(neighborTile != null) tiles.add(neighborTile);
						
					}
					
					if(tile.getBlendType() == Tile.BLEND_TYPE_RECESSIVE) {
						for(int i = 0; i < tiles.size(); i++) {
							if(tiles.get(i).getBlendType() == Tile.BLEND_TYPE_DOMINANT) {
								idCount[i] += 1;
								blendIndices[i] = 1;
							}
						}
					}
					
					int highestCountIndex = 0;
					for(int i = 0; i < idCount.length; i++) {
						if(idCount[i] >= idCount[highestCountIndex]) highestCountIndex = i;
					}
					
					
					if(idCount[highestCountIndex] > 0) {
						blend = true;
						tilef = tiles.get(highestCountIndex);
					}
				}
				else {
					if(tile.getBlendType() == Tile.BLEND_TYPE_RECESSIVE) {
						blend = true;
						blendIndices[0] = 1;
						tilef = Game.tiles.get(chunk.getTile(x + tile.getBlendWeight().x, y + tile.getBlendWeight().y));
					}
				}
				
				TileBatch batch = null;
				boolean exists = false;
				for(TileBatch b : batches) {
					if(b.getId() == id && b.getBlend() == blend) {
						batch = b;
						exists = true;
					}
				}
				if(!exists) {
					batch = new TileBatch(id, blend);
					batches.add(batch);
				}
				
				batch.add(new Vector3f(
							x * Tile.WIDTH + chunk.getPosition().x * Chunk.WIDTH * Tile.WIDTH,
							y * Tile.HEIGHT + chunk.getPosition().y * Chunk.HEIGHT * Tile.HEIGHT,
							Renderer.TILE_LAYER
						),
						new BlendData(tilef, blendIndices));
			}
		}
	}
	
	public void render() {
		for(TileBatch b : batches) {
			Tile tile = Game.tiles.get(b.getId());
			if(b.getBlend()) tile.render(b.positions, b.blendDatas);
			else tile.render(b.positions);
		}
		batches.clear();
	}
	
}

package com.gammarush.engine.structures;

import com.gammarush.engine.Game;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.graphics.model.Texture;
import com.gammarush.engine.graphics.model.TextureArray;
import com.gammarush.engine.math.matrix.Matrix4f;
import com.gammarush.engine.math.vector.Vector2i;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.tiles.Tile;

public class StoneStair extends Structure {
	
	public static final String NAME = "Stone Stair";
	public static final int WIDTH = Tile.WIDTH * 3;
	public static final int HEIGHT = Tile.HEIGHT * 7;
	public static final int LAYER = 3;
	
	public static final Vector2i PLACEMENT = new Vector2i(1, 6);
	public static final int BASE_HEIGHT = 3;
	public static final int BASE_OFFSET = HEIGHT / Tile.HEIGHT - BASE_HEIGHT;
	public static final int[] BASE_COLLISION_ARRAY = new int[] {
		1,1,1,
		1,1,1,
		1,1,1
	};
	public static final int[] TOP_COLLISION_ARRAY = new int[] {
		1,1,1,
		1,1,1,
		1,1,1
	};
	
	public static final Model MODEL = new Model(WIDTH, HEIGHT, new TextureArray("res/structures/stair.png", 5), new TextureArray("res/structures/stair_normal.png", 5));
	public static final Model PREVIEW_MODEL = new Model(WIDTH, HEIGHT, new Texture("res/structures/gate_preview.png"));
	//public static final Model OVERLAY_MODEL = new Model(WIDTH, HEIGHT, new Texture("res/structures/tower_overlay.png"), new Texture("res/structures/tower_overlay_normal.png"));
	
	private int[] sideIndices = new int[] {1, 1, 1, 1};

	public StoneStair(Vector3f position, Game game) {
		super(Structure.STONE_STAIR, position, WIDTH, HEIGHT, LAYER, MODEL, game);
		//setOverlay(OVERLAY_MODEL);
	}
	
	@Override
	public void update() {
		int tileWidth = WIDTH / Tile.WIDTH;
		int tileHeight = HEIGHT / Tile.HEIGHT;
		int tileX = (int) (position.x / Tile.WIDTH);
		//FIX LATER
		int tileY = (int) (position.y / Tile.HEIGHT) + BASE_OFFSET;
		
		sideIndices[0] = 0;
		if(game.world.getElevation(tileX, tileY - 1) > game.world.getElevation(tileX, tileY + 1)) sideIndices[0] = 1;
		
		sideIndices[1] = 0;
		if(game.world.getElevation(tileX, tileY + 1) > game.world.getElevation(tileX, tileY - 1)) sideIndices[1] = 1;
		
		sideIndices[2] = 0;
		if(game.world.getElevation(tileX - 1, tileY) > game.world.getElevation(tileX + 1, tileY)) sideIndices[2] = 1;
		
		sideIndices[3] = 0;
		if(game.world.getElevation(tileX + 1, tileY) > game.world.getElevation(tileX - 1, tileY)) sideIndices[3] = 1;
	}
	
	@Override
	public void render() {
		model.getMesh().bind();
		model.getTexture().bind(TEXTURE_LOCATION);
		model.getNormalMap().bind(NORMAL_MAP_LOCATION);
		model.draw();
		model.getTexture().unbind(TEXTURE_LOCATION);
		model.getNormalMap().unbind(NORMAL_MAP_LOCATION);
		model.getMesh().unbind();
		
		/*for(StructureLayer l : layers) {
			l.prepare();
			l.render();
		}*/
	}
	
	@Override
	public void prepare() {
		Renderer.STRUCTURE.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.scale(new Vector3f(width / model.WIDTH, height / model.HEIGHT, 0)))
				.add(new Vector3f(width / 2, height / 2, 0)));
		Renderer.STRUCTURE.setUniform1iv("side_indices", sideIndices);
	}
	
	@Override
	public boolean checkPlacement(int worldX, int worldY, int localX, int localY) {
		if(localX == 0 && localY == 0) {
			int tile01 = game.world.getTile(worldX, worldY + 1);
			int tile10 = game.world.getTile(worldX + 1, worldY);
			int tile11 = game.world.getTile(worldX + 1, worldY + 1);
			int tile12 = game.world.getTile(worldX + 1, worldY + 2);
			int tile21 = game.world.getTile(worldX + 2, worldY + 1);
			
			if(((tile10 > Tile.CLIFF && tile10 <= Tile.CLIFF_DOWNRIGHT_IN && tile11 > Tile.CLIFF && tile11 <= Tile.CLIFF_DOWNRIGHT_IN && tile12 > Tile.CLIFF && tile12 <= Tile.CLIFF_DOWNRIGHT_IN) ||
					(tile01 > Tile.CLIFF && tile01 <= Tile.CLIFF_DOWNRIGHT_IN && tile11 > Tile.CLIFF && tile11 <= Tile.CLIFF_DOWNRIGHT_IN && tile21 > Tile.CLIFF && tile21 <= Tile.CLIFF_DOWNRIGHT_IN)) &&
					(!game.world.checkSolid(worldX, worldY) && !game.world.checkSolid(worldX + 2, worldY) && !game.world.checkSolid(worldX, worldY + 2) && !game.world.checkSolid(worldX + 2, worldY + 2))) return true;
			return false;
		}
		return true;
	}
	
	@Override
	public boolean usingOverlay() {
		//if(sideIndices[3] == 1) return true;
		return false;
	}
	
}

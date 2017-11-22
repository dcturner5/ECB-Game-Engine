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

public class StoneStairFront extends Structure {
	
	public static final String NAME = "Stone Stair Front";
	public static final int WIDTH = Tile.WIDTH * 3;
	public static final int HEIGHT = Tile.HEIGHT * 5;
	public static final int LAYER = 3;
	
	public static final Vector2i PLACEMENT = new Vector2i(1, 5);
	public static final int BASE_HEIGHT = 1;
	public static final int BASE_OFFSET = HEIGHT / Tile.HEIGHT - BASE_HEIGHT;
	public static final int[] BASE_COLLISION_ARRAY = new int[] {
		1,1,1
	};
	public static final int[] TOP_COLLISION_ARRAY = new int[] {
		1,1,1
	};
	
	public static final Model MODEL = new Model(WIDTH, HEIGHT, new TextureArray("res/structures/stair_front.png", 5), new TextureArray("res/structures/stair_front_normal.png", 5));
	public static final Model PREVIEW_MODEL = new Model(WIDTH, HEIGHT, new Texture("res/structures/stair_front_preview.png"));
	//public static final Model OVERLAY_MODEL = new Model(WIDTH, HEIGHT, new Texture("res/structures/tower_overlay.png"), new Texture("res/structures/tower_overlay_normal.png"));
	
	private int[] sideIndices = new int[] {0, 0, 0, 0};

	public StoneStairFront(Vector3f position, Game game) {
		super(Structure.STONE_STAIR_FRONT, position, WIDTH, HEIGHT, LAYER, MODEL, game);
		//setOverlay(OVERLAY_MODEL);
	}
	
	@Override
	public void update() {
		int tileX = (int) (position.x / Tile.WIDTH);
		int tileY = (int) (position.y / Tile.HEIGHT) + BASE_OFFSET;
		
		sideIndices[0] = 0;
		if(game.world.getElevation(tileX + 1, tileY - 1) > game.world.getElevation(tileX + 1, tileY + 1)) sideIndices[0] = 1;
		
		sideIndices[1] = 0;
		if(game.world.getElevation(tileX + 1, tileY - 1) < game.world.getElevation(tileX + 1, tileY + 1)) sideIndices[1] = 1;
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
			int tile00 = game.world.getTile(worldX, worldY);
			int tile10 = game.world.getTile(worldX + 1, worldY);
			int tile20 = game.world.getTile(worldX + 2, worldY);
			
			if((tile00 > Tile.CLIFF && tile00 <= Tile.CLIFF_DOWNRIGHT_IN && tile10 > Tile.CLIFF && tile10 <= Tile.CLIFF_DOWNRIGHT_IN &&
					tile20 > Tile.CLIFF && tile20 <= Tile.CLIFF_DOWNRIGHT_IN)) return true;
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
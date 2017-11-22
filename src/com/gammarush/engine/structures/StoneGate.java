package com.gammarush.engine.structures;

import com.gammarush.engine.Game;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.graphics.model.Texture;
import com.gammarush.engine.graphics.model.TextureArray;
import com.gammarush.engine.math.matrix.Matrix4f;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector2i;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.tiles.Tile;

public class StoneGate extends Structure {
	
	public static final String NAME = "Stone Gate";
	public static final int WIDTH = Tile.WIDTH * 3;
	public static final int HEIGHT = Tile.HEIGHT * 6;
	public static final int LAYER = 3;
	
	public static final Vector2i PLACEMENT = new Vector2i(1, 5);
	public static final int BASE_HEIGHT = 3;
	public static final int BASE_OFFSET = HEIGHT / Tile.HEIGHT - BASE_HEIGHT;
	public static final int[] BASE_COLLISION_ARRAY = new int[] {
		1,0,1,
		1,0,1,
		1,0,1
	};
	public static final int[] TOP_COLLISION_ARRAY = new int[] {
		1,1,1,
		1,1,1,
		1,1,1
	};
	
	public static final Model MODEL = new Model(WIDTH, HEIGHT, new TextureArray("res/structures/gate.png", 5), new TextureArray("res/structures/tower_normal.png", 5));
	public static final Model PREVIEW_MODEL = new Model(WIDTH, HEIGHT, new Texture("res/structures/gate_preview.png"));
	public static final Model OVERLAY_MODEL = new Model(WIDTH, HEIGHT, new Texture("res/structures/tower_overlay.png"), new Texture("res/structures/tower_overlay_normal.png"));
	
	private int[] sideIndices = new int[] {1, 1, 1, 1};

	public StoneGate(Vector3f position, Game game) {
		super(Structure.STONE_GATE, position, WIDTH, HEIGHT, LAYER, MODEL, game);
		setOverlay(OVERLAY_MODEL);
	}
	
	@Override
	public void update() {
		int tileWidth = WIDTH / Tile.WIDTH;
		int tileHeight = HEIGHT / Tile.HEIGHT;
		int tileX = (int) (position.x / Tile.WIDTH);
		int tileY = (int) (position.y / Tile.HEIGHT) + (tileHeight - tileWidth);
		
		sideIndices[0] = 0;
		for(int i = 0; i < tileWidth; i++) {
			if(game.world.getStructureTop(tileX + i, tileY - 1) != layer) sideIndices[0] = 1;
		}
		
		sideIndices[1] = 0;
		for(int i = 0; i < tileWidth; i++) {
			if(game.world.getStructureTop(tileX - 1, tileY + i) != layer) sideIndices[1] = 1;
		}
		
		sideIndices[2] = 0;
		for(int i = 0; i < tileWidth; i++) {
			if(game.world.getStructureTop(tileX + tileWidth, tileY + i) != layer) sideIndices[2] = 1;
		}
		
		sideIndices[3] = 0;
		for(int i = 0; i < tileWidth; i++) {
			if(game.world.getStructureTop(tileX + i, tileY + tileWidth) != layer) sideIndices[3] = 1;
		}
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
	public boolean usingOverlay() {
		if(sideIndices[3] == 1) return true;
		return false;
	}
}

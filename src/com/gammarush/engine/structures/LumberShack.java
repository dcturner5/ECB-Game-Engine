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

public class LumberShack extends Structure {
	
	public static final String NAME = "Lumber Shack";
	public static final int WIDTH = Tile.WIDTH * 5;
	public static final int HEIGHT = Tile.HEIGHT * 4;
	public static final int LAYER = 2;
	
	public static final Vector2i PLACEMENT = new Vector2i(2, 3);
	public static final int BASE_HEIGHT = 2;
	public static final int BASE_OFFSET = HEIGHT / Tile.HEIGHT - BASE_HEIGHT;
	public static final int[] BASE_COLLISION_ARRAY = new int[] {
		1,1,1,1,1,
		1,1,1,1,1
	};
	public static final int[] TOP_COLLISION_ARRAY = BASE_COLLISION_ARRAY;
	
	public static final Model MODEL = new Model(WIDTH, HEIGHT, new TextureArray("res/structures/lumbershack.png", 1), new TextureArray("res/structures/lumbershack_normal.png", 1));
	public static final Model PREVIEW_MODEL = new Model(WIDTH, HEIGHT, new Texture("res/structures/lumbershack_preview.png"));
	
	private int[] sideIndices = new int[] {1, 1, 1, 1};

	public LumberShack(Vector3f position, Game game) {
		super(Structure.LUMBER_SHACK, position, WIDTH, HEIGHT, LAYER, MODEL, game);
	}
	
	@Override
	public void update() {
		
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

}

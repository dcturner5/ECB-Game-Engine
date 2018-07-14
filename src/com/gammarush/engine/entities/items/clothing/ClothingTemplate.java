package com.gammarush.engine.entities.items.clothing;

import com.gammarush.engine.entities.items.ItemTemplate;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.Color;
import com.gammarush.engine.entities.animations.Animation;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.graphics.model.Texture;
import com.gammarush.engine.graphics.model.TextureArray;
import com.gammarush.engine.math.matrix.Matrix4f;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.utils.json.JSON;

public class ClothingTemplate extends ItemTemplate {
	
	public static final int TYPE_HAIR = 0;
	public static final int TYPE_HEAD = 1;
	public static final int TYPE_BODY = 2;
	
	public static final int WIDTH = 16 * Renderer.SCALE;
	public static final int HEIGHT = 16 * Renderer.SCALE;
	
	private int type;
	private int layer;
	
	public Model model;
	
	public ClothingTemplate(int id, JSON json) {
		super(id, json);
		
		String type = json.getString("clothing.type");
		if(type.equals("hair")) {
			this.type = TYPE_HAIR;
			layer = 0;
		}
		else if(type.equals("head")) {
			this.type = TYPE_HEAD;
			layer = 1;
		}
		else if(type.equals("body")) {
			this.type = TYPE_BODY;
			layer = 2;
		}
		
		Texture texture = new TextureArray("res/entities/items/clothings/" + json.getString("name") + ".png", 64);
		this.model = new Model(texture);
	}
	
	public void render(ClothingBatch batch) {
		model.getMesh().bind();
		model.getTexture().bind(Mob.TEXTURE_LOCATION);
		for(int i = 0; i < batch.positions.size(); i++) {
			prepare(batch.positions.get(i), batch.animations.get(i), batch.colors.get(i));
			model.draw();
		}
		model.getMesh().unbind();
		model.getTexture().unbind(Mob.TEXTURE_LOCATION);
	}
	
	public void prepare(Vector3f position, Animation animation, Color color) {
		Renderer.MOB.setUniformMat4f("ml_matrix", Matrix4f.translate(position.add(0, 0, layer * .0001f + .0001f)).multiply(Matrix4f.rotate(0).add(new Vector3f(WIDTH / 2, HEIGHT / 2, 0)))
				.multiply(Matrix4f.scale(new Vector3f(WIDTH, HEIGHT, 0))));
		Renderer.MOB.setUniform1i("sprite_index", animation != null ? animation.getIndex() : 0);
		Renderer.MOB.setUniform4f("primary_color", color.getPrimary());
		Renderer.MOB.setUniform4f("secondary_color", color.getSecondary());
	}
	
	public int getLayer() {
		return layer;
	}
	
	public int getType() {
		return type;
	}

}

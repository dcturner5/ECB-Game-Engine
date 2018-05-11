package com.gammarush.engine.entities.mobs;

import java.util.ArrayList;

import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.entities.EntityTemplate;
import com.gammarush.engine.entities.animations.Animation;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.graphics.model.TextureArray;
import com.gammarush.engine.math.matrix.Matrix4f;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.math.vector.Vector4f;
import com.gammarush.engine.utils.json.JSON;

public class MobTemplate extends EntityTemplate {
	
	private int width;
	private int height;
	
	private Model model;
	
	private ArrayList<JSON> components;
	
	public ArrayList<Vector4f[]> colors;
	public ArrayList<Vector4f[]> hairColors;

	public MobTemplate(int id, JSON json) {
		super(id, json);
		
		this.width = json.getInteger("width") * Renderer.SCALE;
		this.height = json.getInteger("height") * Renderer.SCALE;
		
		this.model = new Model(new TextureArray("res/entities/mobs/" + json.getString("name") + ".png", 64));
		
		this.components = json.getArray("components");
		
		colors = new ArrayList<Vector4f[]>();
		ArrayList<JSON> colorsJSON = json.getArray("colors");
		for(JSON color : colorsJSON) {
			colors.add(new Vector4f[] {color.getColor("primary"), color.getColor("secondary")});
		}
		
		hairColors = new ArrayList<Vector4f[]>();
		ArrayList<JSON> hairColorsJSON = json.getArray("hairColors");
		for(JSON hairColor : hairColorsJSON) {
			hairColors.add(new Vector4f[] {hairColor.getColor("primary"), hairColor.getColor("secondary")});
		}
		
	}
	
	public void render(MobBatch batch) {
		model.getMesh().bind();
		model.getTexture().bind(Entity.TEXTURE_LOCATION);
		for(int i = 0; i < batch.positions.size(); i++) {
			prepare(batch.positions.get(i), batch.animations.get(i), batch.colors.get(i));
			model.draw();
		}
		model.getMesh().unbind();
		model.getTexture().unbind(Entity.TEXTURE_LOCATION);
	}
	
	public void prepare(Vector3f position, Animation animation, Vector4f[] color) {
		Renderer.MOB.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(0).add(new Vector3f(width / 2, height / 2, 0)))
				.multiply(Matrix4f.scale(new Vector3f(width, height, 0))));
		Renderer.MOB.setUniform1i("sprite_index", animation.getIndex());
		Renderer.MOB.setUniform4f("primary_color", color[0]);
		Renderer.MOB.setUniform4f("secondary_color", color[1]);
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public Model getModel() {
		return model;
	}
	
	public ArrayList<JSON> getComponents() {
		return components;
	}

}

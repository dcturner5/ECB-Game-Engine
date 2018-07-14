package com.gammarush.engine.entities.mobs;

import java.util.ArrayList;

import com.gammarush.engine.entities.Color;
import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.entities.EntityTemplate;
import com.gammarush.engine.entities.animations.Animation;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.graphics.model.TextureArray;
import com.gammarush.engine.math.matrix.Matrix4f;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.utils.json.JSON;

public class MobTemplate extends EntityTemplate {
	
	private int width;
	private int height;
	
	private Model model;
	
	private ArrayList<JSON> components;
	
	public ArrayList<Color> colors;
	public ArrayList<Color> hairColors;

	public MobTemplate(int id, JSON json) {
		super(id, json);
		
		this.width = json.getInteger("width") * Renderer.SCALE;
		this.height = json.getInteger("height") * Renderer.SCALE;
		
		this.model = new Model(new TextureArray("res/entities/mobs/" + json.getString("name") + ".png", 64));
		
		this.components = json.getArray("components");
		
		colors = new ArrayList<Color>();
		ArrayList<JSON> colorsJSON = json.getArray("colors");
		for(JSON color : colorsJSON) {
			colors.add(new Color(color));
		}
		
		hairColors = new ArrayList<Color>();
		ArrayList<JSON> hairColorsJSON = json.getArray("hairColors");
		for(JSON hairColor : hairColorsJSON) {
			hairColors.add(new Color(hairColor));
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
	
	public void prepare(Vector3f position, Animation animation, Color color) {
		Renderer.MOB.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(0).add(new Vector3f(width / 2, height / 2, 0)))
				.multiply(Matrix4f.scale(new Vector3f(width, height, 0))));
		//crashes from nullpointerexception sometimes
		Renderer.MOB.setUniform1i("sprite_index", animation != null ? animation.getIndex() : 0);
		Renderer.MOB.setUniform4f("primary_color", color.getPrimary());
		Renderer.MOB.setUniform4f("secondary_color", color.getSecondary());
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
	
	public Color getColor(String name) {
		for(Color c : colors) {
			if(c.getName().equals(name)) return c;
		}
		return null;
	}
	
	public Color getHairColor(String name) {
		for(Color c : hairColors) {
			if(c.getName().equals(name)) return c;
		}
		return null;
	}

}

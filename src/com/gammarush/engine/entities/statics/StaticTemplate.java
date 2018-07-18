package com.gammarush.engine.entities.statics;

import java.util.ArrayList;

import com.gammarush.engine.entities.Color;
import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.entities.EntityTemplate;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.graphics.model.Texture;
import com.gammarush.engine.math.matrix.Matrix4f;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.utils.json.JSON;

public class StaticTemplate extends EntityTemplate {
	
	private int width;
	private int height;
	private boolean solid;
	private Model model;
	private ArrayList<Color> colors;
	
	public StaticTemplate(int id, JSON json) {
		super(id, json);
		
		this.width = json.getInteger("width") * Renderer.SCALE;
		this.height = json.getInteger("height") * Renderer.SCALE;
		this.solid = json.getBoolean("solid");
		
		this.model = new Model(new Texture("res/entities/statics/" + json.getString("name") + ".png"));
	}
	
	public void render(StaticBatch batch) {
		model.getMesh().bind();
		model.getTexture().bind(Entity.TEXTURE_LOCATION);
		for(int i = 0; i < batch.positions.size(); i++) {
			prepare(batch.positions.get(i), batch.colors.get(i));
			model.draw();
		}
		model.getMesh().unbind();
		model.getTexture().unbind(Entity.TEXTURE_LOCATION);
	}
	
	public void prepare(Vector3f position, Color color) {
		Renderer.DEFAULT.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(0).add(new Vector3f(width / 2, height / 2, 0)))
				.multiply(Matrix4f.scale(new Vector3f(width, height, 0))));
		//Renderer.DEFAULT.setUniform4f("primary_color", color.getPrimary());
		//Renderer.DEFAULT.setUniform4f("secondary_color", color.getSecondary());
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public boolean getSolid() {
		return solid;
	}
	
	public Model getModel() {
		return model;
	}
	
	public Color getColor(String name) {
		for(Color c : colors) {
			if(c.getName().equals(name)) return c;
		}
		return null;
	}
	
}

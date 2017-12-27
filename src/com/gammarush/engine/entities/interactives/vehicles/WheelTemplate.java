package com.gammarush.engine.entities.interactives.vehicles;

import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.entities.EntityTemplate;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.graphics.model.Texture;
import com.gammarush.engine.math.matrix.Matrix4f;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.utils.json.JSON;

public class WheelTemplate extends EntityTemplate {
	
	private Model model;
	
	public WheelTemplate(int id, JSON json) {
		super(id, json);
		
		Texture texture = new Texture("res/entities/interactives/vehicles/wheels/" + json.getString("name") + ".png");
		this.model = new Model(texture);
	}

	public void render(WheelBatch batch) {
		model.getMesh().bind();
		model.getTexture().bind(Entity.TEXTURE_LOCATION);
		for(int i = 0; i < batch.positions.size(); i++) {
			prepare(batch.positions.get(i), batch.sizes.get(i), batch.rotations.get(i));
			model.draw();
		}
		model.getMesh().unbind();
		model.getTexture().unbind(Entity.TEXTURE_LOCATION);
	}
	
	public void prepare(Vector3f position, int size, float rotation) {
		Renderer.DEFAULT.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(rotation).add(new Vector3f(size / 2, size / 2, 0)))
				.multiply(Matrix4f.scale(new Vector3f(size, size, 0))));
	}

}

package com.gammarush.engine.entities.mobs;

import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.entities.EntityTemplate;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.graphics.model.Texture;
import com.gammarush.engine.utils.json.JSON;

public class MobTemplate extends EntityTemplate {
	
	private Model model;

	public MobTemplate(int id, JSON json) {
		super(id, json);
		
		Texture texture = new Texture("res/entities/mobs/" + json.getString("name") + ".png");
		this.model = new Model(texture);
	}
	
	public void render(MobBatch batch) {
		model.getMesh().bind();
		model.getTexture().bind(Entity.TEXTURE_LOCATION);
		/*for(int i = 0; i < batch.positions.size(); i++) {
			prepare(batch.positions.get(i));
			model.draw();
		}*/
		model.getMesh().unbind();
		model.getTexture().unbind(Entity.TEXTURE_LOCATION);
	}
	
	public void prepare() {
		
	}

}

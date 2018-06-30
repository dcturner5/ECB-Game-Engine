package com.gammarush.engine.gui.components;

import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.gui.UIManager;
import com.gammarush.engine.math.matrix.Matrix4f;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.math.vector.Vector4f;


public class UIImage extends UIComponent {
	
	public Model model;
	public float alpha = 1f;
	
	public UIImage(Vector2f position, int width, int height, Model model) {
		super(position, width, height, new Vector4f());
		this.model = model;
	}
	
	public UIImage(Vector2f position, int width, int height, float alpha, Model model) {
		super(position, width, height, new Vector4f());
		this.model = model;
		this.alpha = alpha;
	}
	
	@Override
	public void render() {
		model.getMesh().bind();
		model.getTexture().bind(UIManager.TEXTURE_LOCATION);
		model.draw();
		model.getMesh().unbind();
		model.getTexture().unbind();
	}
	
	@Override
	public void prepare() {
		if(container != null) {
			cutoff.x = 0f;
			cutoff.y = 0f;
			cutoff.z = (container.getWidth() - position.x) / width;
			cutoff.w = (container.getHeight() - position.y) / height;
		}
		else cutoff = new Vector4f(0f, 0f, 1f, 1f);
		
		Renderer.GUI.setUniformMat4f("ml_matrix", Matrix4f.translate(position.add(container.getPosition()))
				.multiply(Matrix4f.rotate(rotation).add(new Vector3f(width / 2, height / 2, 0)))
				.multiply(Matrix4f.scale(new Vector3f(width, height, 0))));
		Renderer.GUI.setUniform1i("use_sprite", 1);
		Renderer.GUI.setUniform1f("sprite_alpha", alpha);
		Renderer.GUI.setUniform4f("cutoff", cutoff);
	}

}

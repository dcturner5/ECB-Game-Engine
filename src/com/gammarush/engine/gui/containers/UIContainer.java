package com.gammarush.engine.gui.containers;

import java.util.ArrayList;

import com.gammarush.engine.gui.animation.UIAnimation;
import com.gammarush.engine.gui.components.UIComponent;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.model.quad.Quad;
import com.gammarush.engine.math.matrix.Matrix4f;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.math.vector.Vector4f;

//HOLDS ALL GUI COMPONENTS, ORGANIZES THEM

public class UIContainer {
	
	public static final Quad MODEL = new Quad(1, 1);
	
	public Vector3f position;
	public float rotation = 0.0f;
	public int width;
	public int height;
	public Vector4f color;
	
	public boolean visible = true;
	public boolean solid = true;
	public boolean ready = true;
	
	public UIAnimation open;
	public UIAnimation close;
	
	public ArrayList<UIComponent> components = new ArrayList<UIComponent>();
	
	public UIContainer(Vector3f position, int width, int height, Vector4f color) {
		this.position = position;
		this.width = width;
		this.height = height;
		this.color = color;
	}
	
	public void update() {
		updateAnimations();
	}
	
	public void render() {
		MODEL.bind();
		MODEL.draw();
		MODEL.unbind();
		for(UIComponent c : components) {
			if(c.visible) {
				c.prepare();
				c.render();
			}
		}
	}
	
	public void prepare() {
		Renderer.GUI.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(rotation).add(new Vector3f(width / 2, height / 2, 0)))
				.multiply(Matrix4f.scale(new Vector3f(width, height, 0))));
		Renderer.GUI.setUniform4f("color", color);
		Renderer.GUI.setUniform1i("use_sprite", 0);
		Renderer.GUI.setUniform4f("cutoff", new Vector4f(0f, 0f, 1f, 1f));
	}
	
	public void add(UIComponent component) {
		component.container = this;
		components.add(component);
	}
	
	public void open() {
		if(open != null) {
			if(!open.running && (close == null || !close.running)) open.start();
		}
		else {
			visible = true;
		}
	}
	
	public void close() {
		if(!visible) return;
		if(close != null) {
			if(!close.running && (open == null || !open.running)) {
				close.start();
			}
		}
		else {
			visible = false;
		}
	}
	
	public void toggle() {
		if((open == null || !open.running) && (close == null || !close.running)) {
			if(!visible) {
				open();
			}
			else {
				close();
			}
		}
	}
	
	public void setOpenAnimation(UIAnimation animation) {
		this.open = animation;
	}
	
	public void setCloseAnimation(UIAnimation animation) {
		this.close = animation;
	}
	

	
	private void updateAnimations() {
		if(open != null && open.running) {
			open.update();
		}
		if(close != null && close.running) {
			close.update();
		}
	}
	
	public boolean getCollision(float x, float y) {
		if(x >= position.x + width || y >= position.y + height || x <= position.x || y <= position.y) return false;
		return true;
	}

}
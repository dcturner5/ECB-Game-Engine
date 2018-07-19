package com.gammarush.engine.ui.containers;

import java.util.ArrayList;

import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.model.quad.Quad;
import com.gammarush.engine.math.matrix.Matrix4f;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.math.vector.Vector4f;
import com.gammarush.engine.ui.animation.UIAnimation;
import com.gammarush.engine.ui.components.UIComponent;

//HOLDS ALL GUI COMPONENTS, ORGANIZES THEM

public class UIContainer {
	
	public static final Quad MODEL = new Quad(1, 1);
	
	private Vector3f position;
	private float rotation = 0.0f;
	private int width;
	private int height;
	private Vector4f color;
	
	private boolean visible = true;
	private boolean solid = true;
	//private boolean ready = true;
	
	private UIAnimation openAnimation;
	private UIAnimation closeAnimation;
	
	private ArrayList<UIComponent> components = new ArrayList<UIComponent>();
	
	public UIContainer(Vector3f position, int width, int height) {
		setPosition(position);
		setWidth(width);
		setHeight(height);
		setColor(new Vector4f(0, 0, 0, 0));
	}
	
	public UIContainer(Vector3f position, int width, int height, Vector4f color) {
		setPosition(position);
		setWidth(width);
		setHeight(height);
		setColor(color);
	}
	
	public void update() {
		updateAnimations();
	}
	
	public void render() {
		MODEL.bind();
		MODEL.draw();
		MODEL.unbind();
		for(UIComponent c : getComponents()) {
			if(c.getVisible()) {
				c.prepare();
				c.render();
			}
		}
	}
	
	public void prepare() {
		Renderer.GUI.setUniformMat4f("ml_matrix", Matrix4f.translate(getPosition()).multiply(Matrix4f.rotate(rotation).add(new Vector3f(getWidth() / 2, getHeight() / 2, 0)))
				.multiply(Matrix4f.scale(new Vector3f(getWidth(), getHeight(), 0))));
		Renderer.GUI.setUniform4f("color", getColor());
		Renderer.GUI.setUniform1i("use_sprite", 0);
		Renderer.GUI.setUniform4f("cutoff", new Vector4f(0f, 0f, 1f, 1f));
	}
	
	public void add(UIComponent component) {
		component.container = this;
		getComponents().add(component);
	}
	
	public void open() {
		if(openAnimation != null) {
			if(!openAnimation.running && (closeAnimation == null || !closeAnimation.running)) openAnimation.start();
		}
		else {
			visible = true;
		}
	}
	
	public void close() {
		if(!visible) return;
		if(closeAnimation != null) {
			if(!closeAnimation.running && (openAnimation == null || !openAnimation.running)) {
				closeAnimation.start();
			}
		}
		else {
			visible = false;
		}
	}
	
	public void toggle() {
		if(!getOpenAnimationRunning() && !getCloseAnimationRunning()) {
			if(!getVisible()) open();
			else close();
		}
	}
	
	public void setOpenAnimation(UIAnimation animation) {
		this.openAnimation = animation;
	}
	
	public void setCloseAnimation(UIAnimation animation) {
		this.closeAnimation = animation;
	}
	
	private void updateAnimations() {
		if(getOpenAnimationRunning()) {
			openAnimation.update();
		}
		if(getCloseAnimationRunning()) {
			closeAnimation.update();
		}
	}
	
	public boolean getCollision(float x, float y) {
		if(x >= getPosition().x + getWidth() || y >= getPosition().y + getHeight() || x <= getPosition().x || y <= getPosition().y) return false;
		return true;
	}
	
	public UIAnimation getCloseAnimation() {
		return closeAnimation;
	}
	
	public boolean getCloseAnimationRunning() {
		return closeAnimation != null && closeAnimation.running;
	}
	
	public UIAnimation getOpenAnimation() {
		return openAnimation;
	}
	
	public boolean getOpenAnimationRunning() {
		return openAnimation != null && openAnimation.running;
	}
	
	public boolean getSolid() {
		return solid;
	}
	
	public boolean getVisible() {
		return visible;
	}
	
	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public Vector4f getColor() {
		return color;
	}

	public void setColor(Vector4f color) {
		this.color = color;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public ArrayList<UIComponent> getComponents() {
		return components;
	}

	public void setComponents(ArrayList<UIComponent> components) {
		this.components = components;
	}

	public void setSolid(boolean solid) {
		this.solid = solid;
	}

}
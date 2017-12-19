package com.gammarush.engine.gui.components;

import com.gammarush.engine.gui.containers.UIContainer;
import com.gammarush.engine.gui.event.EventType;
import com.gammarush.engine.gui.event.UIEventHandler;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.model.quad.Quad;
import com.gammarush.engine.math.matrix.Matrix4f;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.math.vector.Vector4f;

//BASE GUI COMPONENT OTHER COMPONENTS EXTEND OFF OF

public class UIComponent {
	
	public static final Quad MODEL = new Quad(1, 1);
	public static final float Z_OFFSET = .00001f;
	
	public Vector3f position;
	public float rotation = 0.0f;
	public int width;
	public int height;
	protected Vector4f color;
	protected Vector4f cutoff = new Vector4f(0f, 0f, 1f, 1f);
	
	public UIContainer container;
	private UIEventHandler eventhandler;
	
	public boolean leftClick = false;
	public boolean rightClick = false;
	public boolean focus = false;
	public boolean hover = false;
	
	public boolean clickable = true;
	public boolean focusable = false;
	public boolean editable = false;
	public boolean resizable = false;
	
	public boolean visible = true;
	
	public UIComponent(Vector2f position, int width, int height, Vector4f color) {
		this.position = new Vector3f(position.x, position.y, Z_OFFSET);
		this.width = width;
		this.height = height;
		this.color = color;
		setEventHandler(new UIEventHandler() {
			@Override
			public void leftClick() {
			}
			@Override
			public void rightClick() {
			}
			@Override
			public void leftRelease() {
			}
			@Override
			public void rightRelease() {
			}
			@Override
			public void hoverEnter() {
			}
			@Override
			public void hoverExit() {
			}
			@Override
			public void keyInput(int key) {
			}
		});
	}
	
	public void render() {
		MODEL.bind();
		MODEL.draw();
		MODEL.unbind();
	}
	
	public void prepare() {
		if(container != null) {
			cutoff.x = Math.max(-position.x / width, 0);
			cutoff.y = Math.max(-position.y / height, 0);
			cutoff.z = (container.width - position.x) / width;
			cutoff.w = (container.height - position.y) / height;
		}
		else cutoff = new Vector4f(0f, 0f, 1f, 1f);
		
		Renderer.GUI.setUniformMat4f("ml_matrix", Matrix4f.translate(position.add(container.position))
				.multiply(Matrix4f.rotate(rotation).add(new Vector3f(width / 2, height / 2, 0)))
				.multiply(Matrix4f.scale(new Vector3f(width, height, 0))));
		Renderer.GUI.setUniform4f("color", color);
		Renderer.GUI.setUniform1i("use_sprite", 0);
		Renderer.GUI.setUniform4f("cutoff", cutoff);
	}
	
	public void activate(EventType type) {
		eventhandler.activate(type.id);
	}
	
	public void activate(int type) {
		eventhandler.activate(type);
	}
	
	public void activate(EventType type, int key) {
		eventhandler.keyInput(key);
	}
	
	public void setColor(Vector4f color) {
		this.color = color;
	}
	
	public void setEventHandler(UIEventHandler eventhandler) {
		this.eventhandler = eventhandler;
	}
	
	public boolean getCollision(float x, float y) {
		if(x >= position.x + container.position.x + width || y >= position.y + container.position.y + height || x <= position.x + container.position.x || y <= position.y + container.position.y) return false;
		return true;
	}

}
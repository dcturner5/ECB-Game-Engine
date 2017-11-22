package com.gammarush.engine.gui.animation;

import com.gammarush.engine.gui.containers.UIContainer;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.math.vector.Vector4f;

//PARENT CLASS FOR GUI ANIMATIONS

public class UIAnimation {
	
	protected UIContainer container;
	protected Vector3f position;
	protected int width;
	protected int height;
	protected Vector4f color;
	
	public boolean running = false;
	public boolean complete = false;
	public int frame;
	public int max;
	
	public UIAnimation(UIContainer container) {
		this.container = container;
		position = new Vector3f(container.position);
		width = container.width;
		height = container.height;
		color = new Vector4f(container.color);
		max = 0;
		frame = 0;
	}
	
	public UIAnimation(UIContainer container, int time) {
		this.container = container;
		position = new Vector3f(container.position);
		width = container.width;
		height = container.height;
		color = new Vector4f(container.color);
		max = time;
		frame = 0;
	}
	
	public void update() {
		
	}
	
	public void start() {
		running = true;
		complete = false;
		frame = 0;
		container.visible = true;
	}
	
	public void stop() {
		running = false;
		complete = true;
		frame = 0;
		container.position = new Vector3f(position);
		container.width = width;
		container.height = height;
		container.color = new Vector4f(color);
	}
	
}
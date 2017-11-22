package com.gammarush.engine.gui.animation;

import com.gammarush.engine.gui.containers.UIContainer;
import com.gammarush.engine.math.vector.Vector2f;

//GUI COMPONENT WILL INCREASE WIDTH UNTIL FULLY VISIBLE

public class UIAnimationSlideOpen extends UIAnimation {
	
	private AnimationType type;
	private Vector2f step;
	
	public UIAnimationSlideOpen(UIContainer container, int time, AnimationType type) {
		super(container, time);
		this.type = type;
		step = new Vector2f();
		if(type == AnimationType.UP || type == AnimationType.DOWN) step.y = height / time;
		if(type == AnimationType.LEFT || type == AnimationType.RIGHT) step.x = width / time;
	}
	
	public void update() {
		if(!running) return;
		if(frame >= max) stop();
		else {
			container.width += step.x;
			container.height += step.y;
			if(type == AnimationType.UP) container.position.y -= step.y;
			if(type == AnimationType.LEFT) container.position.x -= step.x;
			frame++;
		}
	}
	
	public void start() {
		super.start();
		if(type == AnimationType.UP) container.position.y += height;
		if(type == AnimationType.LEFT) container.position.x += width;
		if(type == AnimationType.LEFT || type == AnimationType.RIGHT) container.width = 0;
		if(type == AnimationType.UP || type == AnimationType.DOWN) container.height = 0;
	}

}

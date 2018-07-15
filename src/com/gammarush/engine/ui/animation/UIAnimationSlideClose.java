package com.gammarush.engine.ui.animation;

import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.ui.containers.UIContainer;

//GUI COMPONENT WILL DECREASE WIDTH UNTIL NOT VISIBLE

public class UIAnimationSlideClose extends UIAnimation {
	
	private AnimationType type;
	private Vector2f step;
	
	public UIAnimationSlideClose(UIContainer container, int time, AnimationType type) {
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
			container.setWidth((int) (container.getWidth() - step.x));
			container.setHeight((int) (container.getHeight() - step.y));
			if(type == AnimationType.DOWN) container.getPosition().y += step.y;
			if(type == AnimationType.RIGHT) container.getPosition().x += step.x;
			frame++;
		}
	}
	
	public void stop() {
		super.stop();
		container.setVisible(false);
	}
	
}

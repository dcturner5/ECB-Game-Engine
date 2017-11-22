package com.gammarush.engine.gui.animation;

import com.gammarush.engine.gui.containers.UIContainer;

public class UIAnimationFadeOpen extends UIAnimation {
	
	private float step;
	
	public UIAnimationFadeOpen(UIContainer container, int time) {
		super(container, time);
		step = container.color.w / time;
	}
	
	public void update() {
		if(!running) return;
		if(frame >= max) stop();
		else {
			container.color.w += step;
			frame++;
		}
	}
	
	public void start() {
		super.start();
		container.color.w = 0f;
	}

}

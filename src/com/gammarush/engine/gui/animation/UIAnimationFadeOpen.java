package com.gammarush.engine.gui.animation;

import com.gammarush.engine.gui.containers.UIContainer;

public class UIAnimationFadeOpen extends UIAnimation {
	
	private float step;
	
	public UIAnimationFadeOpen(UIContainer container, int time) {
		super(container, time);
		step = container.getColor().w / time;
	}
	
	public void update() {
		if(!running) return;
		if(frame >= max) stop();
		else {
			container.getColor().w += step;
			frame++;
		}
	}
	
	public void start() {
		super.start();
		container.getColor().w = 0f;
	}

}

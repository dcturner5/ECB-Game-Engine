package com.gammarush.engine.gui.animation;

import com.gammarush.engine.gui.containers.UIContainer;

//GUI COMPONENT WILL INCREASE TRANSPARENCY UNTIL NOT VISIBLE

public class UIAnimationFadeClose extends UIAnimation {
	
	private float step;
	
	public UIAnimationFadeClose(UIContainer container, int time) {
		super(container, time);
		step = container.color.w / time;
	}
	
	public void update() {
		if(!running) return;
		if(frame >= max) stop();
		else {
			container.color.w -= step;
			frame++;
		}	
	}

	public void stop() {
		super.stop();
		container.visible = false;
	}

}

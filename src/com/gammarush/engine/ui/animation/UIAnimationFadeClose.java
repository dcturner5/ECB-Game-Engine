package com.gammarush.engine.ui.animation;

import com.gammarush.engine.ui.containers.UIContainer;

//GUI COMPONENT WILL INCREASE TRANSPARENCY UNTIL NOT VISIBLE

public class UIAnimationFadeClose extends UIAnimation {
	
	private float step;
	
	public UIAnimationFadeClose(UIContainer container, int time) {
		super(container, time);
		step = container.getColor().w / time;
	}
	
	public void update() {
		if(!running) return;
		if(frame >= max) stop();
		else {
			container.getColor().w -= step;
			frame++;
		}	
	}

	public void stop() {
		super.stop();
		container.setVisible(false);
	}

}

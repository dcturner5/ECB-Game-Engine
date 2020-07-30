package com.gammarush.engine.ui.containers;

import com.gammarush.engine.graphics.Color;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.ui.animation.UIAnimationFadeClose;

public class UIBlackScreen extends UIContainer {

	public UIBlackScreen(Vector3f position, int width, int height) {
		super(position, width, height, Color.BLACK);
		
		setSolid(true);
		setVisible(false);
		setCloseAnimation(new UIAnimationFadeClose(this, 30));
	}
	
	public void update() {
		super.update();
		if(!getCloseAnimation().running) {
			close();
		}
	}

}

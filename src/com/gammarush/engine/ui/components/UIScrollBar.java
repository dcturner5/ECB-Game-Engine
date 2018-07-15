package com.gammarush.engine.ui.components;

import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector4f;
import com.gammarush.engine.ui.event.UIEventHandler;

public class UIScrollBar extends UIComponent {

	public UIScrollBar(Vector2f position, int width, int height, Vector4f color) {
		super(position, width, height, color);
		
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

}

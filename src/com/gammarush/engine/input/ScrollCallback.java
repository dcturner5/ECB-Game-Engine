package com.gammarush.engine.input;

import org.lwjgl.glfw.GLFWScrollCallback;

public class ScrollCallback extends GLFWScrollCallback {
	
	private Input input;
	
	public ScrollCallback(Input input) {
		this.input = input;
	}

	@Override
	public void invoke(long window, double xOffset, double yOffset) {
		input.scroll((float) xOffset, (float) yOffset);
	}

}

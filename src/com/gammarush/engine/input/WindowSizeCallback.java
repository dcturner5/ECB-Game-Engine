package com.gammarush.engine.input;

import org.lwjgl.glfw.GLFWWindowSizeCallback;

public class WindowSizeCallback extends GLFWWindowSizeCallback {
	
	private Input input;
	
	public WindowSizeCallback(Input input) {
		this.input = input;
	}

	@Override
    public void invoke(long window, int width, int height){
        input.windowSize(width, height);
	}
}

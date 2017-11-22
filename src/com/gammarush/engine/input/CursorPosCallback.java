package com.gammarush.engine.input;

import org.lwjgl.glfw.GLFWCursorPosCallback;

public class CursorPosCallback extends GLFWCursorPosCallback {

	private Input input;
	
	public CursorPosCallback(Input input) {
		this.input = input;
	}
	
	@Override
	public void invoke(long window, double x, double y) {
		input.move((float) x, (float) y);
	}

}

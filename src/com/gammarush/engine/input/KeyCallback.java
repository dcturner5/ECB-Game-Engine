package com.gammarush.engine.input;

import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import org.lwjgl.glfw.GLFWKeyCallback;

public class KeyCallback extends GLFWKeyCallback {
	
	private Input input;
	
	public static boolean[] keys = new boolean[65536];
	
	public KeyCallback(Input input) {
		this.input = input;
	}

	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		if(key < 0 || key >= 65536) return;
		keys[key] = action != GLFW_RELEASE;
	}
	
	public static boolean isKeyDown(int keycode) {
		return keys[keycode];
	}

}

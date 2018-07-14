package com.gammarush.engine.input;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_RIGHT;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;

import org.lwjgl.glfw.GLFWMouseButtonCallback;

public class MouseButtonCallback extends GLFWMouseButtonCallback {
	
	private InputManager input;
	
	public MouseButtonCallback(InputManager input) {
		this.input = input;
	}

	@Override
	public void invoke(long window, int button, int action, int mods) {
		if(button == GLFW_MOUSE_BUTTON_LEFT) {
			if(action == GLFW_PRESS) input.leftClick();
			if(action == GLFW_RELEASE) input.leftRelease();
		}
		if(button == GLFW_MOUSE_BUTTON_RIGHT) {
			if(action == GLFW_PRESS) input.rightClick();
			if(action == GLFW_RELEASE) input.rightRelease();
		}
	}

}

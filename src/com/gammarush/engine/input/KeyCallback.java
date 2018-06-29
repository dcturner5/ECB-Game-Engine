package com.gammarush.engine.input;

import static org.lwjgl.glfw.GLFW.glfwGetKey;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_SHIFT;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import org.lwjgl.glfw.GLFWKeyCallback;

public class KeyCallback extends GLFWKeyCallback {
	
	public static boolean[] keys = new boolean[65536];
	
	private Input input;
	
	public KeyCallback(Input input) {
		this.input = input;
	}
	
	@Override
	public void invoke(long window, int key, int scankey, int action, int mods) {
		if(key < 0 || key >= 65536) return;
		if(key == 222) key = '\'';
		
		int code = key;
		
		if(glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS || glfwGetKey(window, GLFW_KEY_RIGHT_SHIFT) == GLFW_PRESS) {
			if(code == '1') key = '!';
			else if(code == '2') code = '@';
			else if(code == '3') code = '#';
			else if(code == '4') code = '$';
			else if(code == '5') code = '%';
			else if(code == '6') code = '^';
			else if(code == '7') code = '&';
			else if(code == '8') code = '*';
			else if(code == '9') code = '(';
			else if(code == '0') code = ')';
			else if(code == '-') code = '_';
			else if(code == '=') code = '+';
			else if(code == '[') code = '{';
			else if(code == ']') code = '}';
			else if(code == '\\') code = '|';
			else if(code == ';') code = ':';
			else if(code == '\'') code = '"';
			else if(code == ',') code = '<';
			else if(code == '.') code = '>';
			else if(code == '/') code = '?';
			else if(code == 257) {
				code = '\n';
			}
		}
		else {
			if(code >= 65 && code <= 90) code += 32;
		}
		
		if(action == GLFW_RELEASE || !input.keyInput(code)) {
			keys[key] = action != GLFW_RELEASE;
		}
	}
	
	public static boolean isKeyDown(int key) {
		return keys[key];
	}

}

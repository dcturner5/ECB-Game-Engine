package com.gammarush.engine.gui;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;

import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.gui.containers.UIContainer;
import com.gammarush.engine.input.KeyCallback;
import com.gammarush.engine.math.vector.Vector4f;
import com.gammarush.engine.Game;

public class UIManager {
	
	public static final Vector4f BASE_COLOR = new Vector4f(0, 0, 0, .7f);
	public static final Vector4f ACCENT_COLOR = new Vector4f(.05f, .05f, .06f, .7f);
	
	public static final Vector4f FONT_COLOR = new Vector4f(.9f, .9f, .9f, 1);
	
	public static final Vector4f BUTTON_COLOR = new Vector4f(0, 0, 0, 1);
	public static final Vector4f BUTTON_CLICK_COLOR = new Vector4f(.09f, .09f, .1f, 1);
	public static final Vector4f BUTTON_HOVER_COLOR = new Vector4f(.05f, .05f, .06f, 1);
	
	public static final Vector4f RED_BUTTON_COLOR = new Vector4f(.5f, 0, 0, 1);
	public static final Vector4f RED_BUTTON_CLICK_COLOR = new Vector4f(.59f, .09f, .1f, 1);
	public static final Vector4f RED_BUTTON_HOVER_COLOR = new Vector4f(.55f, .05f, .06f, 1);

	public static final int TEXTURE_LOCATION = 0;
	
	public Game game;
	public ArrayList<UIContainer> containers = new ArrayList<UIContainer>();
	
	public UIContainer main;
	
	public UIManager(Game game) {
		this.game = game;
		init();
	}
	
	public void update(double delta) {
		for(UIContainer c : containers) {
			if(c.visible) c.update();
		}
		
		if(KeyCallback.isKeyDown(GLFW_KEY_ESCAPE)) {
			
		}
	}
	
	public void render(Renderer renderer) {
		Renderer.GUI.enable();
		for(UIContainer c : containers) {
			if(c.visible) {
				c.prepare();
				c.render(renderer);
			}
		}
		Renderer.GUI.disable();
	}

	public void init() {
		
	}
	
}
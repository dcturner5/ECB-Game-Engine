package com.gammarush.engine.gui;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;

import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.gui.components.UIButton;
import com.gammarush.engine.gui.components.UITextBox;
import com.gammarush.engine.gui.containers.UIContainer;
import com.gammarush.engine.gui.event.UIEventHandler;
import com.gammarush.engine.gui.fonts.Font;
import com.gammarush.engine.input.KeyCallback;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.math.vector.Vector4f;
import com.gammarush.axil.AxilScript;
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
	
	private Game game;
	private ArrayList<UIContainer> containers = new ArrayList<UIContainer>();
	
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
	
	public void render() {
		Renderer.GUI.enable();
		for(UIContainer c : containers) {
			if(c.visible) {
				c.prepare();
				c.render();
			}
		}
		Renderer.GUI.disable();
	}

	public void init() {
		main = new UIContainer(new Vector3f(8, 8, 8), 1000, 100, BASE_COLOR);
		main.visible = true;
		
		UITextBox consoleTextBox = new UITextBox(new Vector2f(), 1000, 100, true);
		consoleTextBox.setFontColor(FONT_COLOR);
		consoleTextBox.setScale(4);
		consoleTextBox.setEventHandler(new UIEventHandler() {
			@Override
			public void leftClick() {}
			@Override
			public void rightClick() {}
			@Override
			public void leftRelease() {}
			@Override
			public void rightRelease() {}
			@Override
			public void hoverEnter() {}
			@Override
			public void hoverExit() {}
			@Override
			public void keyInput(int key) {
				int backspace = 259, enter = 257, leftShift = 340, rightShift = 344;
				if(key == enter) {
					AxilScript script = game.scriptManager.getCompiler().compileString(consoleTextBox.getString());
					script.run();
				}
				else if(key == backspace) consoleTextBox.backspace();
				else if(key != leftShift && key != rightShift) consoleTextBox.addToString((char) key);
			}
		});
		
		main.add(consoleTextBox);
		containers.add(main);
	}
	
	public ArrayList<UIContainer> getContainers() {
		return containers;
	}
	
}
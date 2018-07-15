package com.gammarush.engine.ui;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;

import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.input.KeyCallback;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.math.vector.Vector4f;
import com.gammarush.engine.ui.containers.UIConsole;
import com.gammarush.engine.ui.containers.UIContainer;
import com.gammarush.engine.ui.containers.UIDialogue;
import com.gammarush.engine.ui.fonts.Font;
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
	
	public UIConsole console;
	public UIDialogue dialogue;
	
	Font font = new Font();
	
	public UIManager(Game game) {
		this.game = game;
		init();
	}
	
	public void update(double delta) {
		for(UIContainer c : containers) {
			if(c.getVisible()) c.update();
		}
		
		if(KeyCallback.isKeyDown(GLFW_KEY_ESCAPE)) {
			
		}
		
		if(KeyCallback.isKeyDown(GLFW_KEY_GRAVE_ACCENT)) {
			console.toggle();
		}
	}
	
	public void render() {
		Renderer.GUI.enable();
		for(UIContainer c : containers) {
			if(c.getVisible()) {
				c.prepare();
				c.render();
			}
		}
		Renderer.GUI.disable();
		
		//font.drawStringWorld("Welcome to Martín Simulator", new Vector3f(16, 32, Renderer.ENTITY_LAYER), 5, Color.WHITE, new Vector4f(0, 0, 1000, 1000));
	}

	public void init() {
		console = new UIConsole(new Vector3f(8, 8, Renderer.GUI_LAYER), 512, 128, game);
		add(console);
		
		dialogue = new UIDialogue(new Vector3f(game.width / 2 - 720 / 2, game.height - 192 - 128, Renderer.GUI_LAYER), 720, 192);
		add(dialogue);
	}
	
	public void add(UIContainer container) {
		containers.add(container);
	}
	
	public ArrayList<UIContainer> getContainers() {
		return containers;
	}
	
}
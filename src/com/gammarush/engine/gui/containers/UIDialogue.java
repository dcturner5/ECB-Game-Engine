package com.gammarush.engine.gui.containers;

import com.gammarush.engine.gui.UIManager;
import com.gammarush.engine.gui.components.UITextBox;
import com.gammarush.engine.gui.event.UIEventHandler;
import com.gammarush.engine.gui.components.UIComponent.Alignment;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.math.vector.Vector4f;

public class UIDialogue extends UIContainer {
	
	public static final Vector4f TEXTBOX_COLOR = new Vector4f(0, 0, 0, .3f);
	
	private UITextBox textbox;

	public UIDialogue(Vector3f position, int width, int height) {
		super(position, width, height);
		setSolid(false);
		
		textbox = new UITextBox(new Vector2f(0, height - 16), width, 16, TEXTBOX_COLOR);
		textbox.setAlignment(Alignment.LEFT);
		textbox.setFontColor(UIManager.FONT_COLOR);
		textbox.setScale(3);
		textbox.setString("Martin: Hello, this is a test sentence and shouldn't mean anything.");
		add(textbox);
		
		setOptions(new String[] {"Have you heard of my grandfather?", "What's your opinion on Israel?", "[Give Gelato]", "Goodbye."});
	}

	public void setOptions(String[] strings) {
		for(int i = 0; i < strings.length; i++) {
			String string = strings[i];
			int width = getWidth() / 2;
			int height = 16;
			int padding = 8;
			UITextBox textbox = new UITextBox(new Vector2f(getWidth() - width, i * (height + padding)), width, height, TEXTBOX_COLOR);
			textbox.setFontColor(UIManager.FONT_COLOR);
			textbox.setScale(3);
			textbox.setString(string);
			textbox.setEventHandler(new UIEventHandler() {
				@Override
				public void leftClick() {
					textbox.setFontColor(new Vector4f((float)Math.random(), (float)Math.random(), (float)Math.random(), 1f));
				}
				@Override
				public void rightClick() {}
				@Override
				public void leftRelease() {
				}
				@Override
				public void rightRelease() {}
				@Override
				public void hoverEnter() {}
				@Override
				public void hoverExit() {}
				@Override
				public void keyInput(int key) {}
			});
			add(textbox);
		}
	}

}

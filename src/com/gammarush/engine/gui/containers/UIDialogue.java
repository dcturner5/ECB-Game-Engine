package com.gammarush.engine.gui.containers;

import java.util.ArrayList;

import com.gammarush.engine.gui.UIManager;
import com.gammarush.engine.gui.components.UITextBox;
import com.gammarush.engine.gui.event.UIEventHandler;
import com.gammarush.engine.gui.components.UIComponent.Alignment;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.math.vector.Vector4f;
import com.gammarush.engine.quests.Dialogue;
import com.gammarush.engine.quests.DialogueOption;

public class UIDialogue extends UIContainer {
	
	public static final Vector4f TEXTBOX_COLOR = new Vector4f(0, 0, 0, .3f);
	
	private UITextBox textbox;
	private ArrayList<UITextBox> optionTextBoxes = new ArrayList<UITextBox>();

	public UIDialogue(Vector3f position, int width, int height) {
		super(position, width, height);
		
		setSolid(false);
		//setVisible(false);
		
		textbox = new UITextBox(new Vector2f(0, height - 16), width, 16, TEXTBOX_COLOR);
		textbox.setAlignment(Alignment.LEFT);
		textbox.setFontColor(UIManager.FONT_COLOR);
		textbox.setScale(3);
		textbox.setString("Martin: Hello, this is a test sentence and shouldn't mean anything.");
		add(textbox);
	}
	
	public void set(Dialogue dialogue) {
		textbox.setString(dialogue.getText());
		setOptions(dialogue.getOptions());
	}

	public void setOptions(ArrayList<DialogueOption> options) {
		for(UITextBox textbox : optionTextBoxes) {
			getComponents().remove(textbox);
		}
		
		for(int i = 0; i < options.size(); i++) {
			DialogueOption option = options.get(i);
			String text = option.getText();
			int width = getWidth() / 2;
			int height = 16;
			int padding = 8;
			UITextBox textbox = new UITextBox(new Vector2f(getWidth() - width, i * (height + padding)), width, height, TEXTBOX_COLOR);
			textbox.setFontColor(UIManager.FONT_COLOR);
			textbox.setScale(3);
			textbox.setString(text);
			textbox.setEventHandler(new UIEventHandler() {
				@Override
				public void leftClick() {}
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
			optionTextBoxes.add(textbox);
			add(textbox);
		}
	}

}

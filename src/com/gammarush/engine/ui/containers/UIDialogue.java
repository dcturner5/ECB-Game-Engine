package com.gammarush.engine.ui.containers;

import java.util.ArrayList;

import com.gammarush.engine.entities.mobs.actors.Actor;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.math.vector.Vector4f;
import com.gammarush.engine.quests.Dialogue;
import com.gammarush.engine.quests.DialogueOption;
import com.gammarush.engine.quests.DialogueOption.OptionType;
import com.gammarush.engine.ui.components.UITextBox;
import com.gammarush.engine.ui.components.UIComponent.Alignment;
import com.gammarush.engine.ui.event.UIEventHandler;

public class UIDialogue extends UIContainer {
	
	private static final Vector4f BASE_COLOR = new Vector4f(0, 0, 0, .5f);
	private static final Vector4f FONT_COLOR = new Vector4f(.9f, .9f, .9f, 1);
	private static final Vector4f FONT_PROGRESS_COLOR = new Vector4f(1f, 1f, 0, 1);
	private static final Vector4f FONT_EXIT_COLOR = new Vector4f(1f, .3f, .3f, 1);
	
	private UITextBox textbox;
	private ArrayList<UITextBox> optionTextBoxes = new ArrayList<UITextBox>();

	public UIDialogue(Vector3f position, int width, int height) {
		super(position, width, height);
		
		setSolid(false);
		setVisible(false);
		
		textbox = new UITextBox(new Vector2f(0, height - 16), width, 16, BASE_COLOR);
		textbox.setAlignment(Alignment.LEFT);
		textbox.setFontColor(FONT_COLOR);
		textbox.setScale(3);
		add(textbox);
	}
	
	public void set(Dialogue dialogue) {
		String name = ((Actor) (dialogue.getQuestManager().getPlayerManager().getMob().getInteractingMob())).getName();
		textbox.setString(name + ": " + dialogue.getText());
		
		ArrayList<DialogueOption> options = dialogue.getOptions();
		
		for(UITextBox textbox : optionTextBoxes) {
			getComponents().remove(textbox);
		}
		
		for(int i = 0; i < options.size(); i++) {
			DialogueOption option = options.get(i);
			String text = option.getText();
			int width = getWidth() / 2;
			int height = 16;
			int padding = 8;
			UITextBox textbox = new UITextBox(new Vector2f(getWidth() - width, i * (height + padding)), width, height, BASE_COLOR);
			
			if(option.getType() == OptionType.DEFAULT) textbox.setFontColor(FONT_COLOR);
			else if(option.getType() == OptionType.PROGRESS) textbox.setFontColor(FONT_PROGRESS_COLOR);
			else if(option.getType() == OptionType.EXIT) textbox.setFontColor(FONT_EXIT_COLOR);
			
			textbox.setScale(3);
			textbox.setString(text);
			textbox.setEventHandler(new UIEventHandler() {
				@Override
				public void leftClick() {
					Dialogue link = option.getLink();
					if(link != null) {
						set(option.getLink());
					}
					else {
						close();
						option.getQuestManager().getPlayerManager().getMob().getInteractingMob().setInteractingMob(null);
						option.getQuestManager().getPlayerManager().getMob().setInteractingMob(null);
					}
					
					option.getQuestManager().getScriptManager().callMethod("event_dialogue", dialogue.getName(), option.getName());
				}
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
				public void keyInput(int key) {}
			});
			optionTextBoxes.add(textbox);
			add(textbox);
		}
	}

}

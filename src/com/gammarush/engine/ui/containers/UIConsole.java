package com.gammarush.engine.ui.containers;

import com.gammarush.axil.AxilScript;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.ui.UIManager;
import com.gammarush.engine.ui.animation.AnimationType;
import com.gammarush.engine.ui.animation.UIAnimationSlideClose;
import com.gammarush.engine.ui.animation.UIAnimationSlideOpen;
import com.gammarush.engine.ui.components.UITextBox;
import com.gammarush.engine.ui.event.UIEventHandler;

public class UIConsole extends UIContainer {
	
	private UITextBox textbox;

	public UIConsole(Vector3f position, int width, int height, UIManager uiManager) {
		super(position, width, height, UIManager.BASE_COLOR);
		setVisible(false);
		setOpenAnimation(new UIAnimationSlideOpen(this, 10, AnimationType.DOWN));
		setCloseAnimation(new UIAnimationSlideClose(this, 10, AnimationType.UP));
		
		textbox = new UITextBox(new Vector2f(), width, height, true);
		textbox.setFontColor(UIManager.FONT_COLOR);
		textbox.setScale(3);
		textbox.setEventHandler(new UIEventHandler() {
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
					try {
						AxilScript script = uiManager.getScriptManager().getCompiler().compileString(textbox.getString());
						script.run();
					}
					catch(Exception e) {
						System.err.println("Script Error");
					}
				}
				else if(key == '`') close();
				else if(key == backspace) textbox.backspace();
				else if(key != leftShift && key != rightShift) textbox.addToString((char) key);
			}
		});
		add(textbox);
	}
	
	public void open() {
		super.open();
		textbox.setFocus(true);
	}
	
	public void close() {
		super.close();
		textbox.setFocus(false);
	}
	
	public void print(String value) {
		textbox.setString(textbox.getString() + "\n" + value);
	}

}

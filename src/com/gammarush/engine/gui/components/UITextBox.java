package com.gammarush.engine.gui.components;

import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.gui.event.UIEventHandler;
import com.gammarush.engine.gui.fonts.Font;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.math.vector.Vector4f;

public class UITextBox extends UIComponent {

	private String string = "";
	private Font font;
	private int scale = 0;
	private Vector4f fontColor = new Vector4f(0, 0, 0, 1);
	private Alignment alignment = Alignment.LEFT;
	
	private UIEventHandler editEventHandler = new UIEventHandler() {
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
			if(key == enter) string += '\n';
			else if(key == backspace) backspace();
			else if(key != leftShift && key != rightShift) addToString((char) key);
		}
	};
	
	public UITextBox(Vector2f position, int width, int height) {
		super(position, width, height, new Vector4f(0, 0, 0, 0));
		setFont(new Font());
	}
	
	public UITextBox(Vector2f position, int width, int height, Vector4f color) {
		super(position, width, height, color);
		setFont(new Font());
	}
	
	public UITextBox(Vector2f position, int width, int height, boolean editable) {
		super(position, width, height, new Vector4f(0, 0, 0, 0));
		setFont(new Font());
		
		if(editable) {
			setEditable(true);
			setEventHandler(editEventHandler);
		}
	}
	
	public Alignment getAlignment() {
		return alignment;
	}
	
	public String getString() {
		return string;
	}
	
	public void setString(String string) {
		this.string = string;
	}
	
	public void setAlignment(Alignment alignment) {
		this.alignment = alignment;
	}
	
	public void setScale(int scale) {
		this.scale = scale;
	}
	
	public void setFont(Font font) {
		this.font = font;
	}
	
	public void setFontColor(Vector4f color) {
		this.fontColor = color;
	}
	
	public void addToString(char c) {
		string += c;
	}
	
	public void backspace() {
		string = string.substring(0, Math.max(0, string.length() - 1));
	}
	
	@Override
	public void render() {
		//String string1 = string;
		//if(Math.random() < .8f) string1 += "|";
		
		MODEL.bind();
		MODEL.draw();
		MODEL.unbind();
		
		float stringWidth = string.length() * 3.2f;
		int scale = Math.max((int) (width / stringWidth), 1);
		if(this.scale != 0) scale = this.scale;
		stringWidth *= scale;
		
		int offset = 0;
		if(alignment == Alignment.CENTER) {
			offset = (int) (width / 2 - stringWidth / 2);
		}
		else if(alignment == Alignment.RIGHT) {
			
		}

		Renderer.GUI.disable();
		font.drawString(string,
				new Vector3f(position.x + container.getPosition().x + offset, position.y + container.getPosition().y, position.z + container.getPosition().z + Z_OFFSET),
				scale, fontColor, getContainerBounds());
		Renderer.GUI.enable();
	}

}

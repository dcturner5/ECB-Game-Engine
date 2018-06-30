package com.gammarush.engine.gui.components;

import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.gui.fonts.Font;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.math.vector.Vector4f;

public class UIButton extends UIComponent {
	
	private String string = "";
	private Font font;
	private int scale = 0;
	private Vector4f fontColor = new Vector4f(0, 0, 0, 1);

	public UIButton(Vector2f position, int width, int height) {
		super(position, width, height, new Vector4f(1, 1, 1, 1));
	}
	
	public UIButton(Vector2f position, int width, int height, Vector4f color) {
		super(position, width, height, color);
	}
	
	public void setString(String string) {
		this.string = string;
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
	
	public void render() {
		MODEL.bind();
		MODEL.draw();
		MODEL.unbind();
		
		float stringWidth = string.length() * 2.1f;
		float stringHeight = 5.1f;
		int scale = (int) (width / (stringWidth * 2.0f));
		if(this.scale != 0) scale = this.scale;
		stringWidth *= scale;
		stringHeight *= scale;
		
		//System.out.println(stringWidth + " " + stringHeight);
		
		Renderer.GUI.disable();
		font.drawString(string,
				new Vector3f(position.x + container.getPosition().x + width / 2 - stringWidth / 2,
						position.y + container.getPosition().y + height / 2 - stringHeight / 2,
						position.z + container.getPosition().z + Z_OFFSET),
				scale, fontColor, new Vector4f(container.getPosition().x, container.getPosition().y, container.getPosition().x + container.getWidth(), container.getPosition().y + container.getHeight()));
		Renderer.GUI.enable();
	}

}
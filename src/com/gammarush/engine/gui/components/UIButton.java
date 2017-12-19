package com.gammarush.engine.gui.components;

import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.gui.fonts.Font;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.math.vector.Vector4f;

//BUTTON COMPONENT
//CAN BE CLICKED AND HOVERED OVER

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
	
	public void render(Renderer renderer) {
		MODEL.bind();
		MODEL.draw();
		MODEL.unbind();
		
		float stringWidth = string.length() * 3.2f;
		float stringHeight = 5.0f;
		int scale = (int) (width / (stringWidth * 2.0f));
		if(this.scale != 0) scale = this.scale;
		stringWidth *= scale;
		stringHeight *= scale;
		
		Renderer.GUI.disable();
		font.drawString(string, 
				new Vector3f(position.x + container.position.x + width / 2 - stringWidth / 2,
						position.y + container.position.y + height / 2 - stringHeight / 2,
						position.z + container.position.z + Z_OFFSET),
				scale, fontColor, new Vector4f(container.position.x, container.position.y, container.position.x + container.width, container.position.y + container.height));
		Renderer.GUI.enable();
	}

}
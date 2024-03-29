package com.gammarush.engine.ui.fonts;

import java.util.ArrayList;

import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.model.Texture;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.math.vector.Vector4f;

public class Font {
	
	public static final Texture TEXTURE_ATLAS = new Texture("res/fonts/font.png");
	public static final int TEXTURE_LOCATION = 0;
	
	public ArrayList<Character> characters = new ArrayList<Character>();
	
	public Font() {
		int offset = 0;
		int height = 5;
		//126
		for(int i = 32; i <= 237; i++) {
			int width = 3;
			if(i == 105 || i == 108) width = 1;
			if(i == 106 || i == 237) width = 2;
			if(i == 78 || i == 81 || i == 113 || i == 126) width = 4;
			if(i == 35 || i == 36 || i == 64 || i == 77 || i == 87 || i == 109 || i == 119) width = 5;
			characters.add(new Character(offset, width, height));
			offset += width;
		}
	}
	
	public void drawString(String string, Vector3f position, int scale, Vector4f color, Vector4f border) {
		Renderer.FONT.enable();
		Renderer.FONT.setUniform1i("texture_width", TEXTURE_ATLAS.width);
		Renderer.FONT.setUniform1i("texture_height", TEXTURE_ATLAS.height);
		Renderer.FONT.setUniform4f("color", color);
		TEXTURE_ATLAS.bind();
		
		Vector4f cutoff = new Vector4f(0f, 0f, 1f, 1f);
		float startX = position.x;
		for(int i = 0; i < string.length(); i++) {
			int code = (int) string.charAt(i);
			
			if(code == 10) {
				position.x = startX;
				position.y += 6 * scale;
			}
			if(code < 32) continue;
			
			int offset = 32;
			
			Character character = null;
			try {
				character = characters.get(code - offset);
			}
			catch(IndexOutOfBoundsException e) {
				continue;
			}
			
			cutoff.x = (border.x - position.x) / (character.width * scale);
			cutoff.y = (border.y - position.y) / (character.height * scale);;
			cutoff.z = (border.z - position.x) / (character.width * scale);
			cutoff.w = (border.w - position.y) / (character.height * scale);
			
			character.prepare(position, character.width * scale, character.height * scale, cutoff);
			character.model.getMesh().bind();
			character.model.draw();
			character.model.getMesh().unbind();
			
			position.x += (int) Math.max(((character.width + .5f) * scale), character.width + 1);
		}
		Renderer.FONT.disable();
	}
	
	public void drawStringWorld(String string, Vector3f position, int scale, Vector4f color, Vector4f border) {
		Renderer.FONT_WORLD.enable();
		Renderer.FONT_WORLD.setUniform1i("texture_width", TEXTURE_ATLAS.width);
		Renderer.FONT_WORLD.setUniform1i("texture_height", TEXTURE_ATLAS.height);
		Renderer.FONT_WORLD.setUniform4f("color", color);
		TEXTURE_ATLAS.bind();
		
		Vector4f cutoff = new Vector4f(0f, 0f, 1f, 1f);
		float startX = position.x;
		for(int i = 0; i < string.length(); i++) {
			int code = (int) string.charAt(i);
			
			if(code == 10) {
				position.x = startX;
				position.y += 6 * scale;
			}
			if(code < 32) continue;
			
			int offset = 32;
			
			Character character = null;
			try {
				character = characters.get(code - offset);
			}
			catch(IndexOutOfBoundsException e) {
				continue;
			}
			
			cutoff.x = (border.x - position.x) / (character.width * scale);
			cutoff.y = (border.y - position.y) / (character.height * scale);;
			cutoff.z = (border.z - position.x) / (character.width * scale);
			cutoff.w = (border.w - position.y) / (character.height * scale);
			
			character.prepareWorld(position, character.width * scale, character.height * scale, cutoff);
			character.model.getMesh().bind();
			character.model.draw();
			character.model.getMesh().unbind();
			
			position.x += (int) Math.max(((character.width + .5f) * scale), character.width + 1);
		}
		Renderer.FONT_WORLD.disable();
	}
}

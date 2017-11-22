package com.gammarush.engine.graphics.model;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL42.*;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.gammarush.engine.utils.BufferUtils;

public class TextureArray extends Texture {
	
	private int width, height, layerCount;
	private int texture;
	
	public TextureArray(String path, int layerCount) {
		super(path, false);
		this.layerCount = layerCount;
		texture = load(path);
	}
	
	private int load(String path) {
		int[] pixels = null;
		try {
			BufferedImage image = ImageIO.read(new FileInputStream(path));
			width = image.getWidth();
			height = image.getHeight();
			pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		
		int twidth = width / layerCount;
		int[][] data = new int[layerCount][twidth * height];
		
		for(int i = 0; i < layerCount; i++) {
			for(int x = 0; x < twidth; x++) {
				for(int y = 0; y < height; y++) {
					int tx = i * twidth + x;
					int index = tx + y * width;
					
					int a = (pixels[index] & 0xff000000) >> 24;
					int r = (pixels[index] & 0xff0000) >> 16;
					int g = (pixels[index] & 0xff00) >> 8;
					int b = (pixels[index] & 0xff);
					
					data[i][x + y * twidth] = a << 24 | b << 16 | g << 8 | r;
				}
			}
		}
		
		int result = glGenTextures();
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D_ARRAY, result);
		glTexStorage3D(GL_TEXTURE_2D_ARRAY, 1, GL_RGBA8, twidth, height, layerCount);
		
		for(int i = 0; i < layerCount; i++) {
			glTexSubImage3D(GL_TEXTURE_2D_ARRAY, 0, 0, 0, i, twidth, height, 1, GL_RGBA, GL_UNSIGNED_BYTE, BufferUtils.createIntBuffer(data[i]));
		}
		
		
		glTexParameteri(GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameterf( GL_TEXTURE_2D_ARRAY, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		
		glBindTexture(GL_TEXTURE_2D_ARRAY, 0);
		return result;
	}
	
	@Override
	public void bind() {
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D_ARRAY, texture);
	}
	
	@Override
	public void bind(int index) {
		switch(index) {
		case 0:
			glActiveTexture(GL_TEXTURE0);
			break;
		case 1:
			glActiveTexture(GL_TEXTURE1);
			break;
		case 2:
			glActiveTexture(GL_TEXTURE2);
			break;
		case 3:
			glActiveTexture(GL_TEXTURE3);
			break;
		}
		glBindTexture(GL_TEXTURE_2D_ARRAY, texture);
	}
	
	@Override
	public void unbind() {
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D_ARRAY, 0);
	}
	
	@Override
	public void unbind(int index) {
		switch(index) {
		case 0:
			glActiveTexture(GL_TEXTURE0);
			break;
		case 1:
			glActiveTexture(GL_TEXTURE1);
			break;
		case 2:
			glActiveTexture(GL_TEXTURE2);
			break;
		case 3:
			glActiveTexture(GL_TEXTURE3);
			break;
		}
		glBindTexture(GL_TEXTURE_2D_ARRAY, 0);
	}

}

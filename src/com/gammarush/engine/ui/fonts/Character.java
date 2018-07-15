package com.gammarush.engine.ui.fonts;

import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.math.matrix.Matrix4f;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.math.vector.Vector4f;

public class Character {
	
	public int offset;
	public int width;
	public int height;
	public Model model;
	
	public Character(int offset, int width, int height) {
		this.offset = offset;
		this.width = width;
		this.height = height;
		model = new Model(this.width, this.height);
	}
	
	public void prepare(Vector3f position, float width, float height, Vector4f cutoff) {
		Renderer.FONT.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.scale(new Vector3f(width, height, 0))).add(new Vector3f(width / 2, height / 2, 0)));
				/*.multiply(Matrix4f.scale(new Vector3f(width / this.width, height / this.height, 0)))
				.add(new Vector3f(width / 2, height / 2, 0)));*/
		Renderer.FONT.setUniform1i("offset", this.offset);
		Renderer.FONT.setUniform1i("width", this.width);
		Renderer.FONT.setUniform4f("cutoff", cutoff);
	}
	
	public void prepareWorld(Vector3f position, float width, float height, Vector4f cutoff) {
		Renderer.FONT_WORLD.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.scale(new Vector3f(width, height, 0))).add(new Vector3f(width / 2, height / 2, 0)));
				/*.multiply(Matrix4f.scale(new Vector3f(width / this.width, height / this.height, 0)))
				.add(new Vector3f(width / 2, height / 2, 0)));*/
		Renderer.FONT_WORLD.setUniform1i("offset", this.offset);
		Renderer.FONT_WORLD.setUniform1i("width", this.width);
		Renderer.FONT_WORLD.setUniform4f("cutoff", cutoff);
	}

}

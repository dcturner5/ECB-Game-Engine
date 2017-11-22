package com.gammarush.engine.graphics;

import java.util.HashMap;
import java.util.Map;

import com.gammarush.engine.math.matrix.Matrix4f;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.math.vector.Vector4f;
import com.gammarush.engine.utils.ShaderUtils;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
	
	public static final int VERTEX_ATTRIB = 0;
	public static final int TEXTURE_COORDINATES_ATTRIB = 1;
	
	private final int ID;
	private Map<String, Integer> locationCache = new HashMap<String, Integer>();
	
	public Shader(String vertex, String fragment) {
		ID = ShaderUtils.load(vertex, fragment);
	}
	
	public int getUniform(String name) {
		if(locationCache.containsKey(name)) {
			return locationCache.get(name);
		}
		
		int result = glGetUniformLocation(ID, name);

		if(result == -1) {
			System.err.println("Could not find variable '" + name + "'");
		}
		else {
			locationCache.put(name, result);
		}
		
		return result;
	}
	
	public void setUniform1i(String name, int value) {
		glUniform1i(getUniform(name), value);
	}
	
	public void setUniform1f(String name, float value) {
		glUniform1f(getUniform(name), value);
	}
	
	public void setUniform1iv(String name, int[] values) {
		glUniform1iv(getUniform(name), values);
	}
	
	public void setUniform2f(String name, Vector2f vector) {
		glUniform2f(getUniform(name), vector.x, vector.y);
	}
	
	public void setUniform3f(String name, Vector3f vector) {
		glUniform3f(getUniform(name), vector.x, vector.y, vector.z);
	}
	
	public void setUniform4f(String name, Vector4f vector) {
		glUniform4f(getUniform(name), vector.x, vector.y, vector.z, vector.w);
	}
	
	public void setUniformMat4f(String name, Matrix4f matrix) {
		glUniformMatrix4fv(getUniform(name), false, matrix.toFloatBuffer());
	}
	
	public void enable() {
		glUseProgram(ID);
	}
	
	public void disable() {
		glUseProgram(0);
	}

}

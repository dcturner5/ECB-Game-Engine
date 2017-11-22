package com.gammarush.engine.math.matrix;

import java.nio.FloatBuffer;

import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.utils.BufferUtils;

public class Matrix4f {
	
	public static final int SIZE = 4 * 4;
	public float[] elements = new float[4 * 4];
	
	public Matrix4f() {
		
	}
	
	public static Matrix4f identity() {
		Matrix4f matrix = new Matrix4f();
		for(int i = 0; i < SIZE; i++) {
			matrix.elements[i] = 0.0f;
		}
		matrix.elements[0 + 0 * 4] = 1.0f;
		matrix.elements[1 + 1 * 4] = 1.0f;
		matrix.elements[2 + 2 * 4] = 1.0f;
		matrix.elements[3 + 3 * 4] = 1.0f;
		return matrix;
	}
	
	public static Matrix4f orthographic(float left, float right, float bottom, float top, float near, float far) {
		Matrix4f matrix = identity();
		matrix.elements[0 + 0 * 4] = 2.0f / (right - left);
		matrix.elements[1 + 1 * 4] = 2.0f / (top - bottom);
		matrix.elements[2 + 2 * 4] = 2.0f / (near - far);
		matrix.elements[0 + 3 * 4] = (left + right) / (left - right);
		matrix.elements[1 + 3 * 4] = (bottom + top) / (bottom - top);
		matrix.elements[2 + 3 * 4] = (far + near) / (far - near);
		return matrix;
	}
	
	public static Matrix4f translate(Vector3f vector) {
		Matrix4f matrix = identity();
		matrix.elements[0 + 3 * 4] = vector.x;
		matrix.elements[1 + 3 * 4] = vector.y;
		matrix.elements[2 + 3 * 4] = vector.z;
		return matrix;
	}
	
	public static Matrix4f rotate(float angle) {
		Matrix4f matrix = identity();
		float r = (float) Math.toRadians(angle);
		float cos = (float) Math.cos(r);
		float sin = (float) Math.sin(r);
		matrix.elements[0 + 0 * 4] = cos;
		matrix.elements[1 + 0 * 4] = sin;
		matrix.elements[0 + 1 * 4] = -sin;
		matrix.elements[1 + 1 * 4] = cos;
		return matrix;
	}
	
	public static Matrix4f scale(Vector3f vector) {
		Matrix4f matrix = identity();
		matrix.elements[0 + 0 * 4] = vector.x;
		matrix.elements[1 + 1 * 4] = vector.y;
		matrix.elements[2 + 2 * 4] = vector.z;
		return matrix;
	}
	
	public Matrix4f multiply(Matrix4f matrix) {
		Matrix4f result = new Matrix4f();
		for(int y = 0; y < 4; y++) {
			for(int x = 0; x < 4; x++) {
				float sum = 0.0f;
				for(int e = 0; e < 4; e++) {
					sum += this.elements[x + e * 4] * matrix.elements[e + y * 4];
				}
				result.elements[x + y * 4] = sum;
			}
		}
		return result;
	}
	
	public Matrix4f add(Vector3f vector) {
		Matrix4f result = this;
		result.elements[0 + 3 * 4] += vector.x;
		result.elements[1 + 3 * 4] += vector.y;
		result.elements[2 + 3 * 4] += vector.z;
		return result;
	}
	
	public void print() {
		String result = "\nMatrix4f:\n";
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				result += elements[j + i * 4];
				if(j != 3) result += ", ";
			}
			result += "\n";
		}
		System.out.println(result);
	}
	
	public FloatBuffer toFloatBuffer() {
		return BufferUtils.createFloatBuffer(elements);
	}

}

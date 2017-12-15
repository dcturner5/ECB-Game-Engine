package com.gammarush.engine.graphics;

import com.gammarush.engine.math.vector.Vector4f;

public class Color {
	
	public static final Vector4f BLACK = new Vector4f(0, 0, 0, 1);
	public static final Vector4f BLUE = new Vector4f(0, 0, 1, 1);
	public static final Vector4f GRAY = new Vector4f(.5f, .5f, .5f, 1);
	public static final Vector4f GREEN = new Vector4f(0, .5f, 0, 1);
	public static final Vector4f RED = new Vector4f(1, 0, 0, 1);
	public static final Vector4f WHITE = new Vector4f(1, 1, 1, 1);
	
	public static Vector4f applyAlpha(Vector4f color, float alpha) {
		return new Vector4f(color.x, color.y, color.z, alpha);
	}

}

package com.gammarush.engine.lights;

import com.gammarush.engine.math.vector.Vector3f;

public class AmbientLight {
	
	public Vector3f color;
	public float intensity;
	
	public AmbientLight(Vector3f color, float intensity) {
		this.color = color;
		this.intensity = intensity;
	}
}

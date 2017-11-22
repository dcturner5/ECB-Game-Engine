package com.gammarush.engine.lights;

import com.gammarush.engine.math.vector.Vector3f;

public class GlobalLight {
	
	public Vector3f direction;
	public Vector3f color;
	public float intensity;
	
	public GlobalLight(Vector3f direction, Vector3f color, float intensity) {
		this.direction = direction.normalize();
		this.color = color;
		this.intensity = intensity;
	}

}

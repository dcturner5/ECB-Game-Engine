package com.gammarush.engine.lights;

import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector3f;

public class PointLight {
	
	public Vector3f position;
	public float radius;
	public Vector3f color;
	public float intensity;
	
	public PointLight(Vector2f position, float radius, Vector3f color, float intensity) {
		this.position = new Vector3f(position.x, position.y, radius / 2f);
		this.radius = radius;
		this.color = color;
		this.intensity = intensity;
	}
	
	public void setPosition(Vector2f position) {
		this.position = new Vector3f(position.x, position.y, radius / 2f);
	}
	
	public void setRadius(float radius) {
		this.radius = radius;
		this.position.z = radius / 2f;
	}

}

package com.gammarush.engine.graphics;

import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.math.matrix.Matrix4f;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector3f;

public class Camera {
	
	public Vector3f position;
	private float zoom;
	private Renderer renderer;
	
	public static final float MIN_DEPTH = -1.0f;
	public static final float MAX_DEPTH = 16.0f;
	public static final float MAX_ZOOM = 1f;
	public static final float MIN_ZOOM = 1 / 4f;
	
	public Camera(Vector3f position, Renderer renderer) {
		this.position = position;
		this.zoom = 1f;
		this.renderer = renderer;
	}
	
	public float getZoom() {
		return zoom;
	}
	
	public void setZoom(float zoom) {
		if(zoom < MIN_ZOOM || zoom > MAX_ZOOM) return;
		Vector2f center = new Vector2f(position.x - renderer.width / this.zoom / 2, position.y - renderer.height / this.zoom / 2);
		this.zoom = zoom;
		float width = renderer.width / this.zoom;
		float height = renderer.height / this.zoom;
		position = new Vector3f(center.x + width / 2, center.y + height / 2, 0);
		Renderer.setProjectionMatrix(Matrix4f.orthographic(0.0f, width, height, 0.0f, MIN_DEPTH, MAX_DEPTH));
	}
	
	public void follow(Entity target) {
		if(target == null) return;
		position = new Vector3f(-target.position.x + renderer.width / zoom / 2 - target.width / 2, -target.position.y + renderer.height / zoom / 2 - target.height / 2, 0);
	}

}

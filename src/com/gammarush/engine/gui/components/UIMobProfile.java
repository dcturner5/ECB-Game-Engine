package com.gammarush.engine.gui.components;

import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector4f;

public class UIMobProfile extends UIComponent {
	
	public Mob entity;
	
	public UIMobProfile(Vector2f position, int width, int height, Vector4f color, Mob entity) {
		super(position, width, height, color);
		this.entity = entity;
	}
	
}

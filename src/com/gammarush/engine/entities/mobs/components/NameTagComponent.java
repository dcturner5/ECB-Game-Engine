package com.gammarush.engine.entities.mobs.components;

import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.entities.mobs.actors.ActorComponent;
import com.gammarush.engine.graphics.Color;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.math.vector.Vector4f;
import com.gammarush.engine.ui.fonts.Font;

public class NameTagComponent extends ActorComponent {
	
	public static final String NAME = "nametag";
	public static final String[] DEPENDENCIES = new String[]{};
	public static final int PRIORITY = 0;
	public static final Font FONT = new Font();

	public NameTagComponent(Entity entity) {
		super(NAME, DEPENDENCIES, PRIORITY, entity);
	}

	@Override
	public void update(double delta) {
		
	}
	
	@Override
	public void render() {
		Vector3f position = getActor().position;
		FONT.drawStringWorld(getActor().getName(), position.add(8, -8, 1), 2, Color.WHITE, new Vector4f(position.x - 1000, position.y - 1000, position.x + 1000, position.y + 1000));
	}

}

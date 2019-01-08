package com.gammarush.engine.entities.components;

import com.gammarush.engine.entities.Entity;

public class FrictionComponent extends Component {
	
	public static final String NAME = "friction";
	public static final String[] DEPENDENCIES = new String[]{"physics"};
	public static final int PRIORITY = 3;

	public FrictionComponent(Entity entity) {
		super(NAME, DEPENDENCIES, PRIORITY, entity);
	}

	@Override
	public void update(double delta) {
		PhysicsComponent pc = (PhysicsComponent) getEntity().getComponent("physics");
		
		float deceleration = 2;
		if(pc.velocity.x < 0) {
			pc.velocity.x = Math.min(pc.velocity.x + deceleration, 0);
		}
		if(pc.velocity.x > 0) {
			pc.velocity.x = Math.max(pc.velocity.x - deceleration, 0);
		}
		if(pc.velocity.y < 0) {
			pc.velocity.y = Math.min(pc.velocity.y + deceleration, 0);
		}
		if(pc.velocity.y > 0) {
			pc.velocity.y = Math.max(pc.velocity.y - deceleration, 0);
		}
	}

}

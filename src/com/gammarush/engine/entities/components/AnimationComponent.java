package com.gammarush.engine.entities.components;

import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.entities.animations.Animation;
import com.gammarush.engine.entities.animations.AnimationHashMap;
import com.gammarush.engine.entities.interactives.vehicles.Vehicle;
import com.gammarush.engine.entities.mobs.Mob;

public class AnimationComponent extends Component {
	
	public static final String NAME = "animation";
	public static final String[] DEPENDENCIES = new String[]{};
	public static final int PRIORITY = 1;
	
	private Animation active;
	private AnimationHashMap animations = new AnimationHashMap();

	public AnimationComponent(Entity entity) {
		super(NAME, DEPENDENCIES, PRIORITY, entity);
	}
	
	public AnimationComponent(Entity entity, AnimationHashMap animations) {
		super(NAME, DEPENDENCIES, PRIORITY, entity);
		this.animations = animations;
	}

	@Override
	public void update(double delta) {
		if(active != null) {
			Entity e = getEntity();
			if(e instanceof Mob) {
				active.setDirection(((Mob) e).direction);
			}
			if(e instanceof Vehicle) {
				active.setDirection(((Vehicle) e).direction);
			}
			
			active.update(delta);
		}
	}
	
	public void add(Animation animation) {
		animations.put(animation);
	}
	
	public void start2(String name) {
		if(active == null || !active.isRunning()) {
			active = animations.get(name);
			active.start();
		}
	}
	
	public void start(String name) {
		if(active != null) active.stop();
		active = animations.get(name);
		active.start();
	}
	
	public void stop() {
		active.stop();
	}
	
	public void stop(String name) {
		animations.get(name).stop();
	}
	
	public Animation getAnimation() {
		return active;
	}
	
	public int getIndex() {
		return active.getIndex();
	}

}

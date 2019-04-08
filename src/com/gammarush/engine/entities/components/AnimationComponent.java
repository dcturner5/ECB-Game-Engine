package com.gammarush.engine.entities.components;

import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.entities.animations.Animation;
import com.gammarush.engine.entities.animations.AnimationHashMap;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.vehicles.Vehicle;

public class AnimationComponent extends Component {
	
	public static final String NAME = "animation";
	public static final String[] DEPENDENCIES = new String[]{};
	public static final int PRIORITY = 1;
	
	private Animation active;
	private AnimationHashMap animations = new AnimationHashMap();

	public AnimationComponent(Entity entity) {
		super(NAME, DEPENDENCIES, PRIORITY, entity);
		
		setPermanentlyEnabled(true);
	}
	
	public AnimationComponent(Entity entity, AnimationHashMap animations) {
		super(NAME, DEPENDENCIES, PRIORITY, entity);
		this.animations = animations;
		
		setPermanentlyEnabled(true);
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
	
	public void start(String name) {
		if(active != null && !active.equals(animations.get(name))) {
			active.stop();
		}
		
		active = animations.get(name);
		if(active == null) {
			System.out.println("Animation \"" + name + "\" Does Not Exist");
			return;
		}
		
		active.start();
		
		Entity e = getEntity();
		if(e instanceof Mob) {
			active.setDirection(((Mob) e).direction);
		}
		if(e instanceof Vehicle) {
			active.setDirection(((Vehicle) e).direction);
		}
	}
	
	public void stop() {
		active.stop();
	}
	
	public void stop(String name) {
		Animation animation = animations.get(name);
		if(animation != null) {
			animation.stop();
		}
	}
	
	public Animation getAnimation() {
		return active;
	}
	
	public int getIndex() {
		if(active == null) return 0;
		return active.getIndex();
	}
	
	public boolean isRunning() {
		if(active != null && active.isRunning()) return true;
		return false;
	}

}

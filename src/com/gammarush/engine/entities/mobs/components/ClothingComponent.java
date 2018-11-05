package com.gammarush.engine.entities.mobs.components;

import java.util.ArrayList;

import com.gammarush.engine.GameManager;
import com.gammarush.engine.entities.Color;
import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.entities.components.AnimationComponent;
import com.gammarush.engine.entities.items.Item;
import com.gammarush.engine.entities.items.clothing.ClothingTemplate;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.physics.Physics;

public class ClothingComponent extends MobComponent {
	
	public static final String NAME = "clothing";
	public static final String[] DEPENDENCIES = new String[]{};
	public static final int PRIORITY = 1;
	
	public ClothingSet outfit;
	
	public class ClothingSet {
		
		private ArrayList<ClothingTemplate> array = new ArrayList<ClothingTemplate>();
		private Mob mob;
		
		public ClothingSet(Mob mob) {
			this.mob = mob;
		}
		
		public void render() {
			for(ClothingTemplate c : array) {
				//TODO ADD SIZE ATTRIBS SO CLOTHES SCALE WITH MOB
				mob.getWorld().getClothingBatchManager().add(c, mob.position, ((AnimationComponent) mob.getComponent("animation")).getAnimation(), 
						c.getType().equals("hair") ? mob.hairColor : new Color());
			}
		}
		
		public boolean add(ClothingTemplate clothing) {
			for(ClothingTemplate c : array) {
				if(c.getType().equals(clothing.getType())) {
					return false;
				}
			}
			return array.add(clothing);
		}
		
		public boolean remove(ClothingTemplate clothing) {
			return array.remove(clothing);
		}
		
		public int getStat(String name) {
			int total = 0;
			for(ClothingTemplate c : array) {
				total += c.getStats().getStat(name);
			}
			return total;
		}
		
	}
	
	public ClothingComponent(Entity entity) {
		super(NAME, DEPENDENCIES, PRIORITY, entity);
		outfit = new ClothingSet(getMob());
	}

	@Override
	public void update(double delta) {
		Mob e = getMob();
		
		ArrayList<Item> removeQueue = new ArrayList<Item>();
		for(Item e1 : e.getWorld().getItems()) {
			if(Physics.getCollision(e.getAABB(), e1.getAABB())) {
				ClothingTemplate t = GameManager.getClothing(e1.getTemplate().getId());
				if(t != null && outfit.add(t)) {
					removeQueue.add(e1);
				}
			}
		}
		for(Item e1 : removeQueue) {
			//fix this
			e.getWorld().getItems().remove(e1);
		}
	}
	
	@Override
	public void render() {
		outfit.render();
	}
	
	public int getStat(String name) {
		return outfit.getStat(name);
	}

}

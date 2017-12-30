package com.gammarush.engine.entities.mobs.components;

import java.util.ArrayList;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.entities.items.Item;
import com.gammarush.engine.entities.items.clothing.ClothingTemplate;
import com.gammarush.engine.entities.mobs.ClothingOutfit;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.physics.Physics;

public class ClothingComponent extends MobComponent {
	
	public static final int PRIORITY = 1;
	
	public ClothingOutfit outfit;
	
	public ClothingComponent(Entity entity) {
		super(PRIORITY, entity);
		outfit = new ClothingOutfit(getMob());
	}

	@Override
	public void update(double delta) {
		Mob e = getMob();
		
		ArrayList<Item> removeQueue = new ArrayList<Item>();
		for(Item e1 : e.getWorld().items) {
			if(Physics.getCollision(e.getAABB(), e1.getAABB())) {
				ClothingTemplate t = Game.clothings.get(e1.getTemplate().getName());
				if(t != null && outfit.add(t)) {
					removeQueue.add(e1);
				}
			}
		}
		for(Item e1 : removeQueue) {
			e.getWorld().items.remove(e1);
		}
	}
	
	@Override
	public void render() {
		outfit.render();
	}

}

package com.gammarush.engine.entities.mobs.components;

import java.util.ArrayList;

import com.gammarush.engine.GameManager;
import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.entities.items.Item;
import com.gammarush.engine.entities.items.clothing.ClothingTemplate;
import com.gammarush.engine.entities.mobs.ClothingOutfit;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.physics.Physics;

public class ClothingComponent extends MobComponent {
	
	public static final String NAME = "clothing";
	public static final String[] DEPENDENCIES = new String[]{};
	public static final int PRIORITY = 1;
	
	public ClothingOutfit outfit;
	
	public ClothingComponent(Entity entity) {
		super(NAME, DEPENDENCIES, PRIORITY, entity);
		outfit = new ClothingOutfit(getMob());
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

}

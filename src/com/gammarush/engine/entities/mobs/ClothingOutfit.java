package com.gammarush.engine.entities.mobs;

import java.util.ArrayList;

import com.gammarush.engine.entities.items.clothing.ClothingTemplate;
import com.gammarush.engine.math.vector.Vector4f;

public class ClothingOutfit {
	
	private ArrayList<ClothingTemplate> array = new ArrayList<ClothingTemplate>();
	private Mob mob;
	
	public ClothingOutfit(Mob mob) {
		this.mob = mob;
	}
	
	public void render() {
		for(ClothingTemplate c : array) {
			mob.getWorld().clothingBatchManager.add(c, mob.position, mob.animation, 
					c.getType() == ClothingTemplate.TYPE_HAIR ? mob.hairColor : new Vector4f[] {new Vector4f(), new Vector4f()});
		}
	}
	
	public boolean add(ClothingTemplate clothing) {
		for(ClothingTemplate c : array) {
			if(c.getType() == clothing.getType()) {
				return false;
			}
		}
		return array.add(clothing);
	}
	
	public boolean remove(ClothingTemplate clothing) {
		return array.remove(clothing);
	}
	

}

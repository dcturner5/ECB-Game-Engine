package com.gammarush.engine.entities.mobs;

import java.util.ArrayList;

import com.gammarush.engine.entities.items.clothing.Clothing;
import com.gammarush.engine.math.vector.Vector4f;

public class ClothingOutfit {
	
	private ArrayList<Clothing> array = new ArrayList<Clothing>();
	private Mob mob;
	
	public ClothingOutfit(Mob mob) {
		this.mob = mob;
	}
	
	public void render() {
		for(Clothing c : array) {
			mob.getWorld().clothingBatchManager.add(c, mob.position, mob.animation, 
					c.getType() == Clothing.TYPE_HAIR ? mob.hairColor : new Vector4f[] {new Vector4f(), new Vector4f()});
		}
	}
	
	public boolean add(Clothing clothing) {
		for(Clothing c : array) {
			if(c.getType() == clothing.getType()) {
				return false;
			}
		}
		return array.add(clothing);
	}
	
	public boolean remove(Clothing clothing) {
		return array.remove(clothing);
	}
	

}

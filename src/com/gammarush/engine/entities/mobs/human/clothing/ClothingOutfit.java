package com.gammarush.engine.entities.mobs.human.clothing;

import java.util.ArrayList;

import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.math.vector.Vector4f;

public class ClothingOutfit {
	
	private ArrayList<Clothing> array = new ArrayList<Clothing>();
	private Mob mob;
	
	public ClothingOutfit(Mob mob) {
		this.mob = mob;
	}
	
	public void render(Renderer renderer) {
		for(Clothing c : array) {
			renderer.clothingBatchManager.add(c, mob.position, mob.animation, 
					c.getType() == Clothing.CLOTHING_TYPE_HAIR ? mob.hairColor : new Vector4f[] {new Vector4f(), new Vector4f()});
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

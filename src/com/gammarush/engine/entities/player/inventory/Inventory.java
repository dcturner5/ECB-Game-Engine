package com.gammarush.engine.entities.player.inventory;

import java.util.HashMap;

import com.gammarush.engine.entities.items.Item;
import com.gammarush.engine.graphics.model.Model;

public class Inventory {
	
	private HashMap<Integer, InventorySlot> slots = new HashMap<Integer, InventorySlot>();
	
	public Inventory() {
		slots.put(Item.WOOD, new InventorySlot(Item.WOOD));
	}
	
	public Model getModel(int type) {
		InventorySlot slot = slots.get(type);
		return slot.model;
	}
	
	public int getAmount(int type) {
		InventorySlot slot = slots.get(type);
		return slot.getAmount();
	}
	
	public void setAmount(int type, int amount) {
		InventorySlot slot = slots.get(type);
		slot.setAmount(amount);
	}
	
	public void increaseAmount(int type, int delta) {
		InventorySlot slot = slots.get(type);
		slot.increaseAmount(delta);
	}
	
	public void decreaseAmount(int type, int delta) {
		InventorySlot slot = slots.get(type);
		slot.decreaseAmount(delta);
	}
	
	public boolean checkAmount(int type, int delta) {
		InventorySlot slot = slots.get(type);
		return slot.checkAmount(delta);
	}
	
}

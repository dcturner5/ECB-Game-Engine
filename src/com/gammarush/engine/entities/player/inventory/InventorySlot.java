package com.gammarush.engine.entities.player.inventory;

import com.gammarush.engine.entities.items.Item;
import com.gammarush.engine.entities.items.ItemData;
import com.gammarush.engine.graphics.model.Model;

public class InventorySlot {
	
	public Model model;
	
	private int amount = 0;
	
	public InventorySlot(int type) {
		ItemData data = Item.getItemData(type);
		this.model = data.model;
	}
	
	int getAmount() {
		return this.amount;
	}
	
	void setAmount(int amount) {
		this.amount = amount;
	}
	
	void increaseAmount(int delta) {
		this.amount += delta;
	}
	
	void decreaseAmount(int delta) {
		this.amount -= delta;
	}
	
	boolean checkAmount(int delta) {
		return this.amount >= delta;
	}
	
}

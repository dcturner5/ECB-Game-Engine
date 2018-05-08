package com.gammarush.engine.entities.components;

import java.util.ArrayList;

import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.entities.items.Item;

public class InventoryComponent extends Component {
	
	public static final String NAME = "inventory";
	public static final String[] DEPENDENCIES = new String[]{};
	public static final int PRIORITY = 1;
	
	private int size;
	private ArrayList<Item> items = new ArrayList<Item>();
	
	public InventoryComponent(Entity entity, int size) {
		super(NAME, DEPENDENCIES, PRIORITY, entity);
	}

	@Override
	public void update(double delta) {
		
	}
	
	public void add(Item item) {
		if(items.size() < size) {
			items.add(item);
		}
	}
	
	public void remove(Item item) {
		items.remove(item);
	}

}

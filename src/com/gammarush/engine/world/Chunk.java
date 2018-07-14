package com.gammarush.engine.world;

import java.util.ArrayList;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.entities.Interactive;
import com.gammarush.engine.entities.items.Item;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.vehicles.Vehicle;
import com.gammarush.engine.math.vector.Vector2i;
import com.gammarush.engine.tiles.Tile;
import com.gammarush.engine.utils.json.JSON;

public class Chunk {
	
	public static final int WIDTH = 16;
	public static final int HEIGHT = 16;
	
	private Vector2i position;
	private World world;
	
	private int[] array = new int[WIDTH * HEIGHT];
	private boolean[] entityCollisionArray = new boolean[WIDTH * HEIGHT];
	
	private ArrayList<Entity> entities = new ArrayList<Entity>();
	private ArrayList<Interactive> interactives = new ArrayList<Interactive>();
	private ArrayList<Item> items = new ArrayList<Item>();
	private ArrayList<Mob> mobs = new ArrayList<Mob>();
	private ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
	//private ArrayList<PointLight> lights = new ArrayList<PointLight>();
	
	public Chunk(JSON json, World world) {
		this.world = world;
		this.position = json.getVector2i("position");
		
		ArrayList<Integer> array = json.getIntegerArray("array");
		for(int i = 0; i < array.size(); i++) this.array[i] = array.get(i);
	}
	
	public void update(double delta) {
		interactives.clear();
		interactives.addAll(mobs);
		interactives.addAll(vehicles);
		
		entities.clear();
		entities.addAll(items);
		entities.addAll(interactives);
		
		updateEntityCollisionArray();
		for(Entity e : entities) {
			e.update(delta);
		}
	}
	
	public void render() {
		world.getTileBatchManager().process(this);
		world.getItemBatchManager().process(items);
		world.getVehicleBatchManager().process(vehicles);
		world.getMobBatchManager().process(mobs);
	}
	
	public ArrayList<Entity> getEntities() {
		return entities;
	}
	
	public ArrayList<Interactive> getInteractives() {
		return interactives;
	}
	
	public ArrayList<Item> getItems() {
		return items;
	}
	
	public ArrayList<Mob> getMobs() {
		return mobs;
	}
	
	public ArrayList<Vehicle> getVehicles() {
		return vehicles;
	}
	
	public Vector2i getPosition() {
		return position;
	}
	
	public Vector2i getWorldPosition() {
		return position.mult(WIDTH, HEIGHT);
	}

	public int getTile(int x, int y) {
		if(x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT) return 0;
		return array[x + y * WIDTH];
	}
	
	public void setTile(int id, int x, int y) {
		if(x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT) return;
		array[x + y * WIDTH] = id;
	}
	
	public World getWorld() {
		return world;
	}
	
	public boolean getSolid(int x, int y) {
		if(x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT) return true;
		Tile tile = Game.tiles.get(getTile(x, y));
		return tile.getSolid();
	}
	
	public boolean getEntityCollision(int x, int y) {
		if(x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT) return false;
		return entityCollisionArray[x + y * WIDTH];
	}
	
	public void setEntityCollision(boolean value, int x, int y) {
		if(x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT) return;
		entityCollisionArray[x + y * WIDTH] = value;
	}
	
	public void updateEntityCollisionArray() {
		entityCollisionArray = new boolean[WIDTH * HEIGHT];
		for(Entity e : entities) {
			if(e.getSolid()) {
				int fx = (int) ((e.position.x + e.getCollisionBox().x) / Tile.WIDTH);
				int fy = (int) ((e.position.y + e.getCollisionBox().y) / Tile.HEIGHT);
				int lx = (int) (((e.position.x + e.getCollisionBox().x + e.getCollisionBox().width)) / Tile.WIDTH);
				int ly = (int) (((e.position.y + e.getCollisionBox().y + e.getCollisionBox().height)) / Tile.HEIGHT);
				for(int x = fx; x <= lx; x++) {
					for(int y = fy; y <= ly; y++) {
						setEntityCollision(true, x, y);
					}
				}
			}
		}
	}

}

package com.gammarush.engine.world;

import java.util.ArrayList;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.entities.Interactive;
import com.gammarush.engine.entities.items.Item;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.mobs.actors.Actor;
import com.gammarush.engine.entities.statics.Static;
import com.gammarush.engine.entities.vehicles.Vehicle;
import com.gammarush.engine.math.vector.Vector2f;
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
	private ArrayList<Static> statics = new ArrayList<Static>();
	private ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
	//private ArrayList<PointLight> lights = new ArrayList<PointLight>();
	
	public Chunk(JSON json, World world) {
		this.world = world;
		this.position = json.getVector2i("position");
		
		ArrayList<JSON> actorsJson = json.getArray("actors");
		if(actorsJson != null) {
			for(JSON actorJson : actorsJson) {
				String actorName = actorJson.getString("name");
				Vector2f actorPosition = actorJson.getVector2f("position").mult(Tile.WIDTH, Tile.HEIGHT).add(position.mult(Chunk.WIDTH * Tile.WIDTH, Chunk.HEIGHT * Tile.HEIGHT));
				Actor actor = Game.actors.get(actorName);
				actor.setWorld(world);
				actor.setPosition(actorPosition);
				mobs.add(actor);
			}
		}
		
		ArrayList<JSON> staticsJson = json.getArray("statics");
		if(staticsJson != null) {
			for(JSON staticJson : staticsJson) {
				String staticName = staticJson.getString("name");
				ArrayList<JSON> staticPositions = staticJson.getArray("positions");
				for(JSON staticPositionJson : staticPositions) {
					Vector2f staticPosition = staticPositionJson.getVector2f().mult(Tile.WIDTH, Tile.HEIGHT).add(position.mult(Chunk.WIDTH * Tile.WIDTH, Chunk.HEIGHT * Tile.HEIGHT));
					Vector2f staticStretch = staticPositionJson.getVector2f("stretch");
					if(staticStretch != null) {
						for(int x = 0; x <= staticStretch.x; x++) {
							for(int y = 0; y <= staticStretch.y; y++) {
								statics.add(new Static(Game.statics.get(staticName), staticPosition.add(x * Tile.WIDTH, y * Tile.HEIGHT), world));
							}
						}
					}
					else statics.add(new Static(Game.statics.get(staticName), staticPosition, world));
				}
			}
		}
		
		ArrayList<Integer> array = json.getIntegerArray("array");
		for(int i = 0; i < array.size(); i++) {
			this.array[i] = Game.tiles.getId(getWorld().getTileOrder().get(array.get(i)));
		}
		
		System.out.println(statics.size());
	}
	
	public void update(double delta) {
		interactives.clear();
		interactives.addAll(mobs);
		interactives.addAll(vehicles);
		
		entities.clear();
		entities.addAll(interactives);
		entities.addAll(items);
		entities.addAll(statics);
		
		updateEntityCollisionArray();
		for(Entity e : entities) {
			e.update(delta);
		}
	}
	
	public void render() {
		world.getTileBatchManager().process(this);
		world.getItemBatchManager().process(items);
		world.getStaticBatchManager().process(statics);
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
	
	public ArrayList<Static> getStatics() {
		return statics;
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
				double rlx = (e.position.x + e.getCollisionBox().x + e.getCollisionBox().width) / Tile.WIDTH;
				int lx = (int) rlx;
				double rly = (e.position.y + e.getCollisionBox().y + e.getCollisionBox().height) / Tile.HEIGHT;
				int ly = (int) rly;
				
				if(rlx - lx == 0) lx--;
				if(rly - ly == 0) ly--;
				
				for(int x = fx; x <= lx; x++) {
					for(int y = fy; y <= ly; y++) {
						setEntityCollision(true, x, y);
					}
				}
			}
		}
	}

}

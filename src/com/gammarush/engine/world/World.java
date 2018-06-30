package com.gammarush.engine.world;

import java.util.ArrayList;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.entities.interactives.Interactive;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.mobs.MobBatchManager;
import com.gammarush.engine.entities.interactives.vehicles.Vehicle;
import com.gammarush.engine.entities.interactives.vehicles.VehicleBatchManager;
import com.gammarush.engine.entities.items.Item;
import com.gammarush.engine.entities.items.ItemBatchManager;
import com.gammarush.engine.entities.items.clothing.ClothingBatchManager;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.lights.AmbientLight;
import com.gammarush.engine.lights.GlobalLight;
import com.gammarush.engine.lights.PointLight;
import com.gammarush.engine.math.vector.Vector2i;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.tiles.BlendData;
import com.gammarush.engine.tiles.Tile;
import com.gammarush.engine.tiles.TileBatch;

public class World {
	
	private Game game;
	
	public int width;
	public int height;
	
	private int[] array;
	private boolean[] entityCollisionArray;
	
	//1st tier
	public ArrayList<Entity> entities = new ArrayList<Entity>();
	
	//2nd tier
	public ArrayList<Interactive> interactives = new ArrayList<Interactive>();
	public ArrayList<Item> items = new ArrayList<Item>();
	public ArrayList<Mob> mobs = new ArrayList<Mob>();
	public ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
	
	public GlobalLight global;
	public AmbientLight ambient;
	public ArrayList<PointLight> lights = new ArrayList<PointLight>();
	
	public MobBatchManager mobBatchManager = new MobBatchManager();
	public ItemBatchManager itemBatchManager = new ItemBatchManager();
	public ClothingBatchManager clothingBatchManager = new ClothingBatchManager();
	public VehicleBatchManager vehicleBatchManager = new VehicleBatchManager();
	
	public World(int width, int height, Game game) {
		this.game = game;
		
		this.width = width;
		this.height = height;
		this.array = new int[width * height];
		this.entityCollisionArray = new boolean[width * height];
		
		global = new GlobalLight(new Vector3f(0f, 0f, 1f), new Vector3f(1f, 1f, 1f), 0f);
		ambient = new AmbientLight(new Vector3f(1f, 1f, 1f), .9f);
		//lights.add(new PointLight(new Vector2f(5 * Tile.WIDTH, 5 * Tile.HEIGHT), 1f, new Vector3f(1f, 1f, 1f), 0f));
	}
	
	public void update(double delta) {
		interactives.clear();
		interactives.addAll(vehicles);
		
		entities.clear();
		entities.addAll(mobs);
		entities.addAll(items);
		entities.addAll(interactives);
		
		updateEntityCollisionArray();
		for(Entity e : entities) {
			if(!e.getScreenPresence()) continue;
			e.update(delta);
		}
	}
	
	public void render() {
		renderTiles();
		renderEntities();
	}
	
	public void renderTiles() {
		Renderer.TILE.enable();
		ArrayList<TileBatch> batches = new ArrayList<TileBatch>();
		int fx = (int) Math.max(Math.floor(-game.renderer.camera.position.x / Tile.WIDTH), 0);
		int lx = Math.min(fx + (int)(game.renderer.width / game.renderer.camera.getZoom() / Tile.WIDTH) + 2, width);
		int fy = (int) Math.max(Math.floor(-game.renderer.camera.position.y / Tile.HEIGHT), 0);
		int ly = Math.min(fy + (int)(game.renderer.height / game.renderer.camera.getZoom() / Tile.HEIGHT) + 2, height);
		for(int y = fy; y < ly; y++) {
			for(int x = fx; x < lx; x++) {
				int id = getTile(x, y);
				
				boolean blend = false;
				int[] blendIndices = new int[8];
				
				Tile tile = Game.tiles.get(id), tilef = null;
				
				if(tile == null) continue;
				
				if(tile.getBlendWeight() == null) {
					int[] ids = new int[8];
					int[] idCount = new int[8];
					
					ids[0] = getTile(x - 1, y);
					ids[1] = getTile(x, y - 1);
					ids[2] = getTile(x + 1, y);
					ids[3] = getTile(x, y + 1);
					ids[4] = getTile(x - 1, y - 1);
					ids[5] = getTile(x + 1, y - 1);
					ids[6] = getTile(x + 1, y + 1);
					ids[7] = getTile(x - 1, y + 1);
					
					ArrayList<Tile> tiles = new ArrayList<Tile>(8);
					for(int i = 0; i < ids.length; i++) {
						Tile neighborTile = Game.tiles.get(ids[i]);
						if(neighborTile != null) tiles.add(neighborTile);
						
					}
					
					if(tile.getBlendType() == Tile.BLEND_TYPE_RECESSIVE) {
						for(int i = 0; i < tiles.size(); i++) {
							if(tiles.get(i).getBlendType() == Tile.BLEND_TYPE_DOMINANT) {
								idCount[i] += 1;
								blendIndices[i] = 1;
							}
						}
					}
					
					int highestCountIndex = 0;
					for(int i = 0; i < idCount.length; i++) {
						if(idCount[i] >= idCount[highestCountIndex]) highestCountIndex = i;
					}
					
					
					if(idCount[highestCountIndex] > 0) {
						blend = true;
						tilef = tiles.get(highestCountIndex);
					}
				}
				else {
					if(tile.getBlendType() == Tile.BLEND_TYPE_RECESSIVE) {
						blend = true;
						blendIndices[0] = 1;
						tilef = Game.tiles.get(getTile(x + tile.getBlendWeight().x, y + tile.getBlendWeight().y));
					}
				}
				
				TileBatch batch = null;
				boolean exists = false;
				for(TileBatch b : batches) {
					if(b.id == id && b.blend == blend) {
						batch = b;
						exists = true;
					}
				}
				if(!exists) {
					batch = new TileBatch(id, blend);
					batches.add(batch);
				}
				
				batch.positions.add(new Vector3f(x * Tile.WIDTH, y * Tile.HEIGHT, Renderer.TILE_LAYER));
				if(batch.blend) batch.blendDatas.add(new BlendData(tilef, blendIndices));
				
			}
		}
		for(TileBatch b : batches) {
			Tile tile = Game.tiles.get(b.id);
			if(b.blend) tile.render(b.positions, b.blendDatas);
			else tile.render(b.positions);
		}
		Renderer.TILE.disable();
	}
	
	public void renderEntities() {
		Renderer.DEFAULT.enable();
		itemBatchManager.render(items);
		Renderer.DEFAULT.disable();
		
		Renderer.VEHICLE.enable();
		vehicleBatchManager.render(vehicles);
		Renderer.VEHICLE.disable();
		
		Renderer.MOB.enable();
		mobBatchManager.render(mobs);
		game.player.render();
		clothingBatchManager.render();
		Renderer.MOB.disable();
	}
	
	public boolean checkSolid(int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= height) return true;
		
		Tile tile = Game.tiles.get(getTile(x, y));
		return tile.getSolid();
	}
	
	public int getTile(int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= height) return 0;
		return array[x + y * width];
	}
	
	public void setTile(int id, int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= height) return;
		array[x + y * width] = id;
	}
	
	public boolean getEntityCollision(int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= height) return false;
		return entityCollisionArray[x + y * width];
	}
	
	public void setEntityCollision(boolean value, int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= height) return;
		entityCollisionArray[x + y * width] = value;
	}
	
	public ArrayList<Vector2i> getSolidTiles(int x, int y, int radius) {
		ArrayList<Vector2i> tiles = new ArrayList<Vector2i>();
		int x1 = x - radius;
		int y1 = y - radius;
		int x2 = x + radius;
		int y2 = y + radius;
		for(int i = x1; i < x2; i++) {
			for(int j = y1; j < y2; j++) {
				if(checkSolid(i, j)) {
					tiles.add(new Vector2i(i * Tile.WIDTH, j * Tile.HEIGHT));
				}
			}
		}
		return tiles;
	}
	
	public ArrayList<Vector2i> getNonSolidTiles(int x, int y, int radius) {
		ArrayList<Vector2i> tiles = new ArrayList<Vector2i>();
		int x1 = x - radius;
		int y1 = y - radius;
		int x2 = x + radius;
		int y2 = y + radius;
		for(int i = x1; i < x2; i++) {
			for(int j = y1; j < y2; j++) {
				if(!checkSolid(i, j)) {
					tiles.add(new Vector2i(i, j));
				}
			}
		}
		return tiles;
	}
	
	public void updateEntityCollisionArray() {
		entityCollisionArray = new boolean[width * height];
		for(Entity e :  entities) {
			if(e.getSolid()) {
				int fx = (int) ((e.position.x + e.getCollisionBox().x) / Tile.WIDTH);
				int fy = (int) ((e.position.y + e.getCollisionBox().y) / Tile.HEIGHT);
				int lx = (int) (((e.position.x + e.getCollisionBox().x + e.getCollisionBox().width)) / Tile.WIDTH);
				int ly = (int) (((e.position.y + e.getCollisionBox().y + e.getCollisionBox().height)) / Tile.HEIGHT);
				for(int x = fx; x <= lx; x++) {
					for(int y = fy; y <= ly; y++) {
						game.world.setEntityCollision(true, x, y);
					}
				}
			}
		}
	}
	
	//GENERATE WORLD
	public void generate(int seed) {
		System.out.println("WORLD GENERATED WITH SEED: " + seed);
		
		for(int i = 0; i < width * height; i++) {
			array[i] = Math.random() < .99 ? Game.tiles.getId("grass") : Game.tiles.getId("wall");
		}
	}

}

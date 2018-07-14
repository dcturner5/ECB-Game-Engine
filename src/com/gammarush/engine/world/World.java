package com.gammarush.engine.world;

import java.util.ArrayList;
import java.util.HashMap;

import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.entities.Interactive;
import com.gammarush.engine.entities.items.Item;
import com.gammarush.engine.entities.items.ItemBatchManager;
import com.gammarush.engine.entities.items.clothing.ClothingBatchManager;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.mobs.MobBatchManager;
import com.gammarush.engine.entities.vehicles.Vehicle;
import com.gammarush.engine.entities.vehicles.VehicleBatchManager;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.lights.AmbientLight;
import com.gammarush.engine.lights.GlobalLight;
import com.gammarush.engine.math.vector.Vector2i;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.tiles.Tile;
import com.gammarush.engine.tiles.TileBatchManager;
import com.gammarush.engine.utils.json.JSON;

public class World {
	
	private int id;
	private String name;
	
	private Vector2i mainChunkPosition;
	private ArrayList<Entity> entitySyncQueue = new ArrayList<Entity>();
	
	private GlobalLight global;
	private AmbientLight ambient;
	
	private ArrayList<Chunk> loadedChunks = new ArrayList<Chunk>();
	private HashMap<Vector2i, Chunk> chunks = new HashMap<Vector2i, Chunk>();
	
	private WorldManager worldManager;
	
	private ClothingBatchManager clothingBatchManager = new ClothingBatchManager();
	private ItemBatchManager itemBatchManager = new ItemBatchManager();
	private MobBatchManager mobBatchManager = new MobBatchManager();
	private TileBatchManager tileBatchManager = new TileBatchManager();
	private VehicleBatchManager vehicleBatchManager = new VehicleBatchManager();
	
	public World(int id, JSON json, WorldManager worldManager) {
		this.id = id;
		this.worldManager = worldManager;
		
		name = json.getString("name");
		ArrayList<JSON> chunksArray = json.getArray("chunks");
		for(JSON chunkJson : chunksArray) {
			Chunk c = new Chunk(chunkJson, this);
			chunks.put(c.getPosition(), c);
		}
		
		global = new GlobalLight(new Vector3f(0f, 0f, 1f), new Vector3f(1f, 1f, 1f), 0f);
		ambient = new AmbientLight(new Vector3f(1f, 1f, 1f), .9f);
		//lights.add(new PointLight(new Vector2f(5 * Tile.WIDTH, 5 * Tile.HEIGHT), 1f, new Vector3f(1f, 1f, 1f), 0f));
	}
	
	public void update(double delta) {
		for(Chunk c : loadedChunks) {
			c.update(delta);
		}
		loadChunks(worldManager.getPlayerManager().getMob().getChunkPosition());
		syncEntities();
	}
	
	public void render() {
		for(Chunk c : loadedChunks) {
			c.render();
		}
		
		Renderer.TILE.enable();
		getTileBatchManager().render();
		Renderer.TILE.disable();
		
		Renderer.DEFAULT.enable();
		getItemBatchManager().render();
		Renderer.DEFAULT.disable();
		
		Renderer.VEHICLE.enable();
		getVehicleBatchManager().render();
		Renderer.VEHICLE.disable();
		
		Renderer.MOB.enable();
		getMobBatchManager().render();
		clothingBatchManager.render();
		Renderer.MOB.disable();
	}
	
	public void addItem(Item e) {
		Chunk c = getChunkFromWorldPosition(e.position);
		if(c != null) {
			e.setWorld(this);
			c.getItems().add(e);
		}
	}
	
	public void addMob(Mob e) {
		Chunk c = getChunkFromWorldPosition(e.position);
		if(c != null) {
			e.setWorld(this);
			c.getMobs().add(e);
		}
	}
	
	public void addVehicle(Vehicle e) {
		Chunk c = getChunkFromWorldPosition(e.position);
		if(c != null) {
			e.setWorld(this);
			c.getVehicles().add(e);
		}
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public ArrayList<Entity> getEntities() {
		ArrayList<Entity> result = new ArrayList<Entity>();
		for(Chunk c : loadedChunks) {
			result.addAll(c.getEntities());
		}
		return result;
	}
	
	public ArrayList<Interactive> getInteractives() {
		ArrayList<Interactive> result = new ArrayList<Interactive>();
		for(Chunk c : loadedChunks) {
			result.addAll(c.getInteractives());
		}
		return result;
	}
	
	public ArrayList<Item> getItems() {
		ArrayList<Item> result = new ArrayList<Item>();
		for(Chunk c : loadedChunks) {
			result.addAll(c.getItems());
		}
		return result;
	}
	
	public AmbientLight getAmbientLight() {
		return ambient;
	}
	
	public GlobalLight getGlobalLight() {
		return global;
	}
	
	public Chunk getChunk(Vector2i position) {
		if(chunks.containsKey(position)) {
			return chunks.get(position);
		}
		return null;
	}
	
	public Chunk getChunkFromTilePosition(int x, int y) {
		Vector2i position = new Vector2i((int) Math.floor(x / Chunk.WIDTH), (int) Math.floor(y / Chunk.HEIGHT));
		if(chunks.containsKey(position)) {
			return chunks.get(position);
		}
		return null;
	}
	
	public Chunk getChunkFromWorldPosition(Vector3f v) {
		Vector2i position = new Vector2i((int) Math.floor(v.x / Tile.WIDTH / Chunk.WIDTH), (int) Math.floor(v.y / Tile.HEIGHT / Chunk.HEIGHT));
		if(chunks.containsKey(position)) {
			return chunks.get(position);
		}
		return null;
	}
	
	public Renderer getRenderer() {
		return getWorldManager().getRenderer();
	}
	
	public int getTile(int x, int y) {
		Vector2i position = new Vector2i((int) Math.floor(x / Chunk.WIDTH), (int) Math.floor(y / Chunk.HEIGHT));
		if(chunks.containsKey(position)) {
			Chunk c = chunks.get(position);
			return c.getTile((int) (((float) x / Chunk.WIDTH - position.x) * Chunk.WIDTH), (int) (((float) y / Chunk.HEIGHT - position.y) * Chunk.HEIGHT));
		}
		else {
			return 0;
		}
	}
	
	public void setTile(int id, int x, int y) {
		Vector2i position = new Vector2i((int) Math.floor(x / Chunk.WIDTH), (int) Math.floor(y / Chunk.HEIGHT));
		if(chunks.containsKey(position)) {
			Chunk c = chunks.get(position);
			c.setTile(id, (int) (((float) x / Chunk.WIDTH - position.x) * Chunk.WIDTH), (int) (((float) y / Chunk.HEIGHT - position.y) * Chunk.HEIGHT));
		}
		else {
			return;
		}
	}
	
	public boolean getSolid(int x, int y) {
		Vector2i position = new Vector2i((int) Math.floor(x / Chunk.WIDTH), (int) Math.floor(y / Chunk.HEIGHT));
		if(chunks.containsKey(position)) {
			Chunk c = chunks.get(position);
			return c.getSolid((int) (((float) x / Chunk.WIDTH - position.x) * Chunk.WIDTH), (int) (((float) y / Chunk.HEIGHT - position.y) * Chunk.HEIGHT));
		}
		else {
			return true;
		}
	}
	
	public boolean getEntityCollision(int x, int y) {
		Vector2i position = new Vector2i((int) Math.floor(x / Chunk.WIDTH), (int) Math.floor(y / Chunk.HEIGHT));
		if(chunks.containsKey(position)) {
			Chunk c = chunks.get(position);
			return c.getEntityCollision((int) (((float) x / Chunk.WIDTH - position.x) * Chunk.WIDTH), (int) (((float) y / Chunk.HEIGHT - position.y) * Chunk.HEIGHT));
		}
		else {
			return false;
		}
	}
	
	public void loadChunks(Vector2i position) {
		if(!position.equals(mainChunkPosition)) {
			ArrayList<Vector2i> unloadQueue = new ArrayList<Vector2i>();
			for(Chunk c : loadedChunks) {
				if(Math.abs(position.x - c.getPosition().x) > 1 || Math.abs(position.y - c.getPosition().y) > 1) {
					unloadQueue.add(c.getPosition());
				}
			}
			for(Vector2i p : unloadQueue) unloadChunk(p);
			
			loadChunk(position);
			loadChunk(position.add(-1, 0));
			loadChunk(position.add(0, -1));
			loadChunk(position.add(1, 0));
			loadChunk(position.add(0, 1));
			loadChunk(position.add(-1, -1));
			loadChunk(position.add(-1, 1));
			loadChunk(position.add(1, -1));
			loadChunk(position.add(1, 1));
			mainChunkPosition = position;
		}
	}
	
	public void loadChunk(Vector2i position) {
		Chunk c = getChunk(position);
		if(c != null && !loadedChunks.contains(c)) loadedChunks.add(c);
	}
	
	public void unloadChunk(Vector2i position) {
		Chunk c = getChunk(position);
		if(c != null) loadedChunks.remove(c);
	}
	
	public void setEntityCollision(boolean value, int x, int y) {
		Vector2i position = new Vector2i((int) Math.floor(x / Chunk.WIDTH), (int) Math.floor(y / Chunk.HEIGHT));
		if(chunks.containsKey(position)) {
			Chunk c = chunks.get(position);
			c.setEntityCollision(value, (int) (((float) x / Chunk.WIDTH - position.x) * Chunk.WIDTH), (int) (((float) y / Chunk.HEIGHT - position.y) * Chunk.HEIGHT));
		}
		else {
			return;
		}
	}
	
	public ArrayList<Vector2i> getSolidTiles(int x, int y, int radius) {
		ArrayList<Vector2i> tiles = new ArrayList<Vector2i>();
		int x1 = x - radius;
		int y1 = y - radius;
		int x2 = x + radius;
		int y2 = y + radius;
		for(int i = x1; i < x2; i++) {
			for(int j = y1; j < y2; j++) {
				if(getSolid(i, j)) {
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
				if(!getSolid(i, j)) {
					tiles.add(new Vector2i(i, j));
				}
			}
		}
		return tiles;
	}
	
	public WorldManager getWorldManager() {
		return worldManager;
	}
	
	public ClothingBatchManager getClothingBatchManager() {
		return clothingBatchManager;
	}
	
	public ItemBatchManager getItemBatchManager() {
		return itemBatchManager;
	}
	
	public MobBatchManager getMobBatchManager() {
		return mobBatchManager;
	}
	
	public TileBatchManager getTileBatchManager() {
		return tileBatchManager;
	}
	
	public VehicleBatchManager getVehicleBatchManager() {
		return vehicleBatchManager;
	}
	
	public void syncEntity(Entity entity) {
		entitySyncQueue.add(entity);
	}
	
	public void syncEntities() {
		for(Entity e : entitySyncQueue) {
			Vector2i cp = e.getChunkPosition();
			Vector2i lcp = e.getLastChunkPosition();
			if(!cp.equals(lcp)) {
				Chunk c = getChunk(cp);
				Chunk lc = getChunk(lcp);
				if(c == null) {
					continue;
				}
				
				if(e instanceof Mob) {
					lc.getMobs().remove(e);
					c.getMobs().add((Mob) e);
				}
				if(e instanceof Vehicle) {
					lc.getVehicles().remove(e);
					c.getVehicles().add((Vehicle) e);
				}
				else if(e instanceof Item) {
					lc.getItems().remove(e);
					c.getItems().add((Item) e);
				}
				else if(e instanceof Interactive) {
					lc.getInteractives().remove(e);
					c.getInteractives().add((Interactive) e);
				}
				e.setLastChunkPosition(cp);
			}
		}
		entitySyncQueue.clear();
	}
	
}

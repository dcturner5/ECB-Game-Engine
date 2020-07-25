package com.gammarush.engine.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.entities.Interactive;
import com.gammarush.engine.entities.items.Item;
import com.gammarush.engine.entities.items.ItemBatchManager;
import com.gammarush.engine.entities.items.clothing.ClothingBatchManager;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.mobs.MobBatchManager;
import com.gammarush.engine.entities.statics.Static;
import com.gammarush.engine.entities.statics.StaticBatchManager;
import com.gammarush.engine.entities.vehicles.Vehicle;
import com.gammarush.engine.entities.vehicles.VehicleBatchManager;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.lights.AmbientLight;
import com.gammarush.engine.lights.GlobalLight;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector2i;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.tiles.Tile;
import com.gammarush.engine.tiles.TileBatchManager;
import com.gammarush.engine.utils.json.JSON;

public class World {
	
	private int id;
	private String name;
	private int width = 0;
	private int height = 0;
	private ArrayList<String> tileOrder;
	
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
	private StaticBatchManager staticBatchManager = new StaticBatchManager();
	private TileBatchManager tileBatchManager = new TileBatchManager();
	private VehicleBatchManager vehicleBatchManager = new VehicleBatchManager();
	
	private ArrayList<Item> itemQueue = new ArrayList<Item>();
	private ArrayList<Mob> mobQueue = new ArrayList<Mob>();
	private ArrayList<Static> staticQueue = new ArrayList<Static>();
	private ArrayList<Vehicle> vehicleQueue = new ArrayList<Vehicle>();
	
	private ArrayList<Item> removeItemQueue = new ArrayList<Item>();
	private ArrayList<Mob> removeMobQueue = new ArrayList<Mob>();
	private ArrayList<Static> removeStaticQueue = new ArrayList<Static>();
	private ArrayList<Vehicle> removeVehicleQueue = new ArrayList<Vehicle>();
	
	private HashMap<String, Vector2f> markers = new HashMap<String, Vector2f>();
	
	public World(int id, JSON json, WorldManager worldManager) {
		this.id = id;
		this.worldManager = worldManager;
		
		name = json.getString("name");
		tileOrder = json.getStringArray("tiles");
		ArrayList<JSON> chunksArray = json.getArray("chunks");
		for(JSON chunkJson : chunksArray) {
			Chunk c = new Chunk(chunkJson, this);
			chunks.put(c.getPosition(), c);
			
			int cx = c.getPosition().x + 1;
			int cy = c.getPosition().y + 1;
			if(cx > width)
				width = cx;
			if(cy > height)
				height = cy;
		}
		
		global = new GlobalLight(new Vector3f(0f, 0f, 1f), new Vector3f(1f, 1f, 1f), 0f);
		ambient = new AmbientLight(new Vector3f(1f, 1f, 1f), 1f);
		//lights.add(new PointLight(new Vector2f(5 * Tile.WIDTH, 5 * Tile.HEIGHT), 1f, new Vector3f(1f, 1f, 1f), 0f));
	}
	
	public void update(double delta) {
		addEntitiesFromQueue();
		
		for(Chunk c : loadedChunks) {
			c.update(delta);
		}
		loadChunks(worldManager.getPlayerManager().getMob().getChunkPosition());
		syncEntities();
		
		removeEntitiesFromQueue();
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
		getStaticBatchManager().render();
		Renderer.DEFAULT.disable();
		
		Renderer.VEHICLE.enable();
		getVehicleBatchManager().render();
		Renderer.VEHICLE.disable();
		
		Renderer.MOB.enable();
		getMobBatchManager().render();
		getClothingBatchManager().render();
		Renderer.MOB.disable();
	}
	
	public void addItem(Item e) {
		itemQueue.add(e);
	}
	
	public void addMob(Mob e) {
		mobQueue.add(e);
	}
	
	public void addStatic(Static e) {
		staticQueue.add(e);
	}
	
	public void addVehicle(Vehicle e) {
		vehicleQueue.add(e);
	}
	
	private void addEntitiesFromQueue() {
		for(Item e : itemQueue) {
			Chunk c = getChunkFromWorldPosition(e.position);
			if(c != null) {
				e.setWorld(this);
				c.getItems().add(e);
				c.getEntities().add(e);
			}
		}
		for(Mob e : mobQueue) {
			Chunk c = getChunkFromWorldPosition(e.position);
			if(c != null) {
				e.setWorld(this);
				c.getMobs().add(e);
				c.getInteractives().add(e);
				c.getEntities().add(e);
			}
		}
		
		for(Vehicle e : vehicleQueue) {
			Chunk c = getChunkFromWorldPosition(e.position);
			if(c != null) {
				e.setWorld(this);
				c.getVehicles().add(e);
				c.getInteractives().add(e);
				c.getEntities().add(e);
			}
		}
		
		for(Static e : staticQueue) {
			Chunk c = getChunkFromWorldPosition(e.position);
			if(c != null) {
				e.setWorld(this);
				c.getStatics().add(e);
				c.getEntities().add(e);
			}
		}
		
		itemQueue.clear();
		mobQueue.clear();
		staticQueue.clear();
		vehicleQueue.clear();
	}
	
	private void removeEntitiesFromQueue() {
		for(Item e : removeItemQueue) {
			Chunk c = getChunkFromWorldPosition(e.position);
			if(c != null) {
				c.getItems().remove(e);
				c.getEntities().remove(e);
			}
		}
		for(Mob e : removeMobQueue) {
			Chunk c = getChunkFromWorldPosition(e.position);
			if(c != null) {
				c.getMobs().remove(e);
				c.getEntities().remove(e);
			}
		}
		
		for(Vehicle e : removeVehicleQueue) {
			Chunk c = getChunkFromWorldPosition(e.position);
			if(c != null) {
				c.getVehicles().remove(e);
				c.getEntities().remove(e);
			}
		}
		
		for(Static e : removeStaticQueue) {
			Chunk c = getChunkFromWorldPosition(e.position);
			if(c != null) {
				c.getStatics().remove(e);
				c.getEntities().remove(e);
			}
		}
		
		removeItemQueue.clear();
		removeMobQueue.clear();
		removeStaticQueue.clear();
		removeVehicleQueue.clear();
	}
	
	public Entity getEntity(UUID uuid) {
		//May have to change this back to getEntities()
		for(Entity e : getAllEntities()) {
			if(e.getUUID().equals(uuid)) {
				return e;
			}
		}
		return null;
	}
	
	public void removeItem(Item e) {
		Chunk c = getChunkFromWorldPosition(e.position);
		if(c != null) {
			c.getItems().remove(e);
			c.getEntities().remove(e);
		}
	}
	
	public void removeMob(Mob e) {
		/*Chunk c = getChunkFromWorldPosition(e.position);
		if(c != null) {
			c.getMobs().remove(e);
			c.getEntities().remove(e);
		}*/
		removeMobQueue.add(e);
	}
	
	public void removeVehicle(Vehicle e) {
		Chunk c = getChunkFromWorldPosition(e.position);
		if(c != null) {
			c.getVehicles().remove(e);
			c.getEntities().remove(e);
		}
	}
	
	public void removeStatic(Static e) {
		Chunk c = getChunkFromWorldPosition(e.position);
		if(c != null) {
			c.getStatics().remove(e);
			c.getEntities().remove(e);
		}
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public ArrayList<String> getTileOrder() {
		return tileOrder;
	}
	
	public ArrayList<Entity> getAllEntities() {
		ArrayList<Entity> result = new ArrayList<Entity>();
		for(Map.Entry<Vector2i, Chunk> entry : chunks.entrySet()) {
			Chunk c = (Chunk) entry.getValue();
			result.addAll(c.getEntities());
		}
		return result;
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
	
	public ArrayList<Mob> getMobs() {
		ArrayList<Mob> result = new ArrayList<Mob>();
		for(Chunk c : loadedChunks) {
			result.addAll(c.getMobs());
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
				if(!getSolid(i, j) && !getEntityCollision(i, j)) {
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
	
	public StaticBatchManager getStaticBatchManager() {
		return staticBatchManager;
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
				if(c == null || lc == null) {
					continue;
				}
				if(e instanceof Mob) {
					//Test code
					if(c.getMobs().contains((Mob) e)) {
						System.out.println("Duplicate Mob");
						lc.print();
						c.print();
						continue;
					}
					
					lc.getMobs().remove(e);
					c.getMobs().add((Mob) e);
					c.refreshEntityArray();
				}
				else if(e instanceof Vehicle) {
					lc.getVehicles().remove(e);
					c.getVehicles().add((Vehicle) e);
					c.refreshEntityArray();
				}
				else if(e instanceof Item) {
					lc.getItems().remove(e);
					c.getItems().add((Item) e);
					c.refreshEntityArray();
				}
				else if(e instanceof Interactive) {
					lc.getInteractives().remove(e);
					c.getInteractives().add((Interactive) e);
					c.refreshEntityArray();
				}
				e.setLastChunkPosition(cp);
			}
		}
		entitySyncQueue.clear();
	}
	
	public void addMarker(String name, Vector2f position) {
		markers.put(name, position);
	}
	
	public Vector2f getMarker(String name) {
		if(markers.containsKey(name)) {
			return markers.get(name);
		}
		return new Vector2f();
	}
	
}

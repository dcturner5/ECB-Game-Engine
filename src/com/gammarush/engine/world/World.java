package com.gammarush.engine.world;

import java.util.ArrayList;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.entities.interactives.Interactive;
import com.gammarush.engine.entities.items.Item;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.mobs.human.Human;
import com.gammarush.engine.entities.interactives.vehicles.Vehicle;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.lights.AmbientLight;
import com.gammarush.engine.lights.GlobalLight;
import com.gammarush.engine.lights.PointLight;
import com.gammarush.engine.math.matrix.Matrix4f;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector2i;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.math.vector.Vector4f;
import com.gammarush.engine.tiles.BlendData;
import com.gammarush.engine.tiles.Tile;
import com.gammarush.engine.tiles.TileBatch;

public class World {
	
	private Game game;
	
	public int width;
	public int height;
	
	private int[] array;
	private boolean[] entityCollisionArray;
	private int[] landscapeArray;
	private int[] biomeArray;
	private float[] elevationArray;
	private int[] structureArray;
	private int[] structureTopArray;
	private ArrayList<Biome> biomes = new ArrayList<Biome>();
	
	
	//1st tier
	public ArrayList<Entity> entities = new ArrayList<Entity>();
	
	//2nd tier
	public ArrayList<Interactive> interactives = new ArrayList<Interactive>();
	public ArrayList<Item> items = new ArrayList<Item>();
	public ArrayList<Mob> mobs = new ArrayList<Mob>();
	public ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();
	
	//3rd tier
	public ArrayList<Human> humans = new ArrayList<Human>();
	
	
	public GlobalLight global;
	public AmbientLight ambient;
	public ArrayList<PointLight> lights = new ArrayList<PointLight>();
	
	public static final float WATER_LEVEL = -.4f;
	public static final float[] LAYERS = new float[] {WATER_LEVEL, .15f, .4f};
	
	//COMPARATORS FOR EFFICENT TEXTURE BINDING
	/*private Comparator<Structure> structureSorter = new Comparator<Structure>() {
		@Override
		public int compare(Structure e1, Structure e2) {
			if(e1.id > e2.id) return -1;
			if(e1.id < e2.id) return 1;
			return 0;
		}
	};*/
	
	public World(int width, int height, Game game) {
		this.game = game;
		
		this.width = width;
		this.height = height;
		this.array = new int[width * height];
		this.entityCollisionArray = new boolean[width * height];
		this.landscapeArray = new int[width * height];
		this.biomeArray = new int[width * height];
		this.elevationArray = new float[width * height];
		this.structureArray = new int[width * height];
		this.structureTopArray = new int[width * height];
		
		biomes.add(Biome.FOREST);
		biomes.add(Biome.DESERT);
		biomes.add(Biome.PLAINS);
		
		global = new GlobalLight(new Vector3f(0f, -.7f, 1f), new Vector3f(1f, 1f, 1f), .5f);
		ambient = new AmbientLight(new Vector3f(1f, 1f, 1f), .5f);
		//lights.add(new PointLight(new Vector2f(Tile.WIDTH * 1, Tile.HEIGHT * 5), .5f, new Vector3f(1f, 1f, 1f), 0f));
	}
	
	public void update(double delta) {
		interactives.clear();
		interactives.addAll(vehicles);
		
		mobs.clear();
		mobs.addAll(humans);
		
		entities.clear();
		entities.addAll(mobs);
		entities.addAll(items);
		entities.addAll(interactives);
		
		updateEntityCollisionArray();
		for(Entity e : entities) {
			e.update(delta);
		}
	}
	
	public void render(Renderer renderer) {
		loadShaderUniforms(renderer);
		
		renderTiles(renderer);
		renderEntities(renderer);
	}
	
	public void loadShaderUniforms(Renderer renderer) {
		Matrix4f viewMatrix = Matrix4f.translate(renderer.camera.position);
		
		Renderer.DEFAULT.enable();
		Renderer.DEFAULT.setUniformMat4f("vw_matrix", viewMatrix);
		Renderer.DEFAULT.setUniform2f("resolution", new Vector2f(Renderer.screenWidth, Renderer.screenHeight));
		Renderer.DEFAULT.setUniform4f("global_color", new Vector4f(global.color.x, global.color.y, global.color.z, global.intensity));
		Renderer.DEFAULT.setUniform3f("global_direction", new Vector3f(global.direction.x, global.direction.y, global.direction.z));
		Renderer.DEFAULT.setUniform4f("ambient_color", new Vector4f(ambient.color.x, ambient.color.y, ambient.color.z, ambient.intensity));
		for(int i = 0; i < lights.size(); i++) {
			PointLight light = lights.get(i);
			Renderer.DEFAULT.setUniform3f("point_position[" + i + "]", light.position);
			Renderer.DEFAULT.setUniform1f("point_radius[" + i + "]", light.radius);
			Renderer.DEFAULT.setUniform4f("point_color[" + i + "]", new Vector4f(light.color.x, light.color.y, light.color.z, light.intensity));
		}
		Renderer.DEFAULT.disable();
		
		Renderer.MOB.enable();
		Renderer.MOB.setUniformMat4f("vw_matrix", viewMatrix);
		Renderer.MOB.disable();
		
		Renderer.TILE.enable();
		Renderer.TILE.setUniformMat4f("vw_matrix", viewMatrix);
		Renderer.TILE.setUniform2f("resolution", new Vector2f(Renderer.screenWidth, Renderer.screenHeight));
		Renderer.TILE.setUniform4f("global_color", new Vector4f(global.color.x, global.color.y, global.color.z, global.intensity));
		Renderer.TILE.setUniform3f("global_direction", new Vector3f(global.direction.x, global.direction.y, global.direction.z));
		Renderer.TILE.setUniform4f("ambient_color", new Vector4f(ambient.color.x, ambient.color.y, ambient.color.z, ambient.intensity));
		for(int i = 0; i < lights.size(); i++) {
			PointLight light = lights.get(i);
			Renderer.TILE.setUniform3f("point_position[" + i + "]", light.position);
			Renderer.TILE.setUniform1f("point_radius[" + i + "]", light.radius);
			Renderer.TILE.setUniform4f("point_color[" + i + "]", new Vector4f(light.color.x, light.color.y, light.color.z, light.intensity));
		}
		Renderer.TILE.disable();
		
		Renderer.VEHICLE.enable();
		Renderer.VEHICLE.setUniformMat4f("vw_matrix", viewMatrix);
		Renderer.VEHICLE.disable();
	}
	
	public void renderTiles(Renderer renderer) {
		Renderer.TILE.enable();
		ArrayList<TileBatch> batches = new ArrayList<TileBatch>();
		int fx = (int) Math.max(Math.floor(-renderer.camera.position.x / Tile.WIDTH), 0);
		int lx = Math.min(fx + (int)(renderer.width / renderer.camera.getZoom() / Tile.WIDTH) + 2, width);
		int fy = (int) Math.max(Math.floor(-renderer.camera.position.y / Tile.HEIGHT), 0);
		int ly = Math.min(fy + (int)(renderer.height / renderer.camera.getZoom() / Tile.HEIGHT) + 2, height);
		for(int y = fy; y < ly; y++) {
			for(int x = fx; x < lx; x++) {
				int id = getTile(x, y);
				
				boolean blend = false;
				int[] blendIndices = new int[8];
				
				Tile tile = Game.tiles.get(id), tilef = null;
				
				if(tile == null) continue;
				
				if(getStructure(x, y) == 1) continue;
				
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
	
	public void renderEntities(Renderer renderer) {
		Renderer.DEFAULT.enable();
		for(Item e : items) {
			e.prepare();
			e.render(renderer);
		}
		Renderer.DEFAULT.disable();
		
		Renderer.MOB.enable();
		for(Human e : humans) {
			e.prepare();
			e.render(renderer);
		}
		
		for(Interactive e : interactives) {
			e.prepare();
			e.render(renderer);
		}
		Renderer.MOB.disable();
		
		Renderer.VEHICLE.enable();
		for(Vehicle e : vehicles) {
			e.prepare();
			e.render(renderer);
		}
		Renderer.VEHICLE.disable();
	}
	
	public boolean checkSolid(int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= height) return true;
		
		Tile tile = Game.tiles.get(getTile(x, y));
		if(tile.getSolid() || getStructure(x, y) > 0) return true;
		return false;
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
	
	public int getLandscape(int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= height) return 0;
		return landscapeArray[x + y * width];
	}
	
	public int getBiome(int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= height) return 0;
		return biomeArray[x + y * width];
	}
	
	public void setBiome(int id, int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= height) return;
		biomeArray[x + y * width] = id;
	}
	
	public float getElevation(int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= height) return 0;
		return elevationArray[x + y * width];
	}
	
	public void setElevation(float value, int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= height) return;
		elevationArray[x + y * width] = value;
	}
	
	public int getStructure(int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= height) return 0;
		return structureArray[x + y * width];
	}
	
	public void setStructure(int id, int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= height) return;
		structureArray[x + y * width] = id;
	}
	
	public int getStructureTop(int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= height) return 0;
		return structureTopArray[x + y * width];
	}
	
	public void setStructureTop(int id, int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= height) return;
		structureTopArray[x + y * width] = id;
	}
	
	public void createStructure(int type, float x, float y) {
		
	}
	
	public boolean checkStructurePlacement(int tileX, int tileY, int tileWidth, int baseHeight, int baseOffset, Vector2i placement) {
		int tileXOffsetLeft = placement.x;
		int tileXOffsetRight = tileWidth - tileXOffsetLeft - 1;
		int tileYOffsetUp = placement.y - baseOffset;
		int tileYOffsetDown = baseHeight - tileYOffsetUp - 1;
		for(int x1 = tileX - tileXOffsetLeft; x1 <= tileX + tileXOffsetRight; x1++) {
			for(int y1 = tileY - tileYOffsetUp; y1 <= tileY + tileYOffsetDown; y1++) {
				if(checkSolid(x1, y1)) return false;
			}
		}
		return true;
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
			array[i] = Game.tiles.getId("grass");
		}
	}

}

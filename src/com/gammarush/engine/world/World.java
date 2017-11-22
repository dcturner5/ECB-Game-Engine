package com.gammarush.engine.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.entities.interactives.Door;
import com.gammarush.engine.entities.interactives.Interactive;
import com.gammarush.engine.entities.interactives.Tree;
import com.gammarush.engine.entities.items.Item;
import com.gammarush.engine.entities.mobs.Human;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.lights.AmbientLight;
import com.gammarush.engine.lights.GlobalLight;
import com.gammarush.engine.lights.PointLight;
import com.gammarush.engine.math.matrix.Matrix4f;
import com.gammarush.engine.math.noise.Noise2D;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector2i;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.math.vector.Vector4f;
import com.gammarush.engine.physics.AABB;
import com.gammarush.engine.physics.Physics;
import com.gammarush.engine.structures.Structure;
import com.gammarush.engine.structures.StructureData;
import com.gammarush.engine.tiles.BlendData;
import com.gammarush.engine.tiles.Tile;
import com.gammarush.engine.tiles.TileBatch;

public class World {
	
	private Game game;
	
	public int width;
	public int height;
	
	public TerrainEditor terrainEditor;
	
	private int[] array;
	private int[] landscapeArray;
	private int[] biomeArray;
	private float[] elevationArray;
	private int[] structureArray;
	private int[] structureTopArray;
	private ArrayList<Biome> biomes = new ArrayList<Biome>();
	
	public ArrayList<Entity> entities = new ArrayList<Entity>();
	public ArrayList<Human> humans = new ArrayList<Human>();
	public ArrayList<Item> items = new ArrayList<Item>();
	public ArrayList<Structure> structures = new ArrayList<Structure>();
	
	public ArrayList<Interactive> interactives = new ArrayList<Interactive>();
	public ArrayList<Door> doors = new ArrayList<Door>();
	public ArrayList<Tree> trees = new ArrayList<Tree>();
	
	
	public GlobalLight global;
	public AmbientLight ambient;
	public ArrayList<PointLight> lights = new ArrayList<PointLight>();
	
	public static final float WATER_LEVEL = -.4f;
	public static final float[] LAYERS = new float[] {WATER_LEVEL, .15f, .4f};
	
	//COMPARATORS FOR EFFICENT TEXTURE BINDING
	private Comparator<Structure> structureSorter = new Comparator<Structure>() {
		@Override
		public int compare(Structure e1, Structure e2) {
			if(e1.id > e2.id) return -1;
			if(e1.id < e2.id) return 1;
			return 0;
		}
	};
	
	public World(int width, int height, Game game) {
		this.game = game;
		
		this.terrainEditor = new TerrainEditor(this);
		
		this.width = width;
		this.height = height;
		this.array = new int[width * height];
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
		entities.clear();
		entities.addAll(humans);
		entities.addAll(items);
		entities.addAll(interactives);
		
		//interactives.clear();
		//interactives.addAll(doors);
		//interactives.addAll(trees);
		
		structures.clear();
		
		for(Entity e : entities) {
			e.update(delta);
		}
		
		for(Structure s : structures) {
			s.update();
		}
	}
	
	public void render(Renderer renderer) {
		loadShaderUniforms(renderer);
		
		renderTiles(renderer);
		renderStructures(renderer);
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
		
		Renderer.STRUCTURE.enable();
		Renderer.STRUCTURE.setUniformMat4f("vw_matrix", viewMatrix);
		Renderer.STRUCTURE.setUniform2f("resolution", new Vector2f(Renderer.screenWidth, Renderer.screenHeight));
		Renderer.STRUCTURE.setUniform4f("global_color", new Vector4f(global.color.x, global.color.y, global.color.z, global.intensity));
		Renderer.STRUCTURE.setUniform3f("global_direction", new Vector3f(global.direction.x, global.direction.y, global.direction.z));
		Renderer.STRUCTURE.setUniform4f("ambient_color", new Vector4f(ambient.color.x, ambient.color.y, ambient.color.z, ambient.intensity));
		for(int i = 0; i < lights.size(); i++) {
			PointLight light = lights.get(i);
			Renderer.STRUCTURE.setUniform3f("point_position[" + i + "]", light.position);
			Renderer.STRUCTURE.setUniform1f("point_radius[" + i + "]", light.radius);
			Renderer.STRUCTURE.setUniform4f("point_color[" + i + "]", new Vector4f(light.color.x, light.color.y, light.color.z, light.intensity));
		}
		Renderer.STRUCTURE.disable();
		
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
				
				if(tile.blendWeight == null) {
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
					
					if(tile.blendType == Tile.BLEND_TYPE_RECESSIVE) {
						for(int i = 0; i < tiles.size(); i++) {
							if(tiles.get(i).blendType == Tile.BLEND_TYPE_DOMINANT) {
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
					if(tile.blendType == Tile.BLEND_TYPE_RECESSIVE) {
						blend = true;
						blendIndices[0] = 1;
						tilef = Game.tiles.get(getTile(x + tile.blendWeight.x, y + tile.blendWeight.y));
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
			if(b.blend) tile.render(b.positions, b.blendDatas, renderer);
			else tile.render(b.positions, renderer);
		}
		Renderer.TILE.disable();
	}
	
	public void renderEntities(Renderer renderer) {
		Renderer.DEFAULT.enable();
		
		Tree.MODEL.getMesh().bind();
		Tree.MODEL.getTexture().bind(Tree.TEXTURE_LOCATION);
		Tree.MODEL.getNormalMap().bind(Tree.NORMAL_MAP_LOCATION);
		for(Tree e : trees) {
			e.prepare();
			Tree.MODEL.draw();
		}
		Tree.MODEL.getMesh().unbind();
		Tree.MODEL.getTexture().unbind(Tree.TEXTURE_LOCATION);
		Tree.MODEL.getNormalMap().unbind(Tree.NORMAL_MAP_LOCATION);
		
		Door.MODEL.getMesh().bind();
		Door.MODEL.getTexture().bind(Door.TEXTURE_LOCATION);
		for(Door e : doors) {
			e.prepare();
			Door.MODEL.draw();
		}
		Door.MODEL.getMesh().unbind();
		Door.MODEL.getTexture().unbind(Door.TEXTURE_LOCATION);
		
		//ITEMS
		for(Item e : items) {
			e.prepare();
			e.render();
		}
		
		Renderer.DEFAULT.disable();
		
		Renderer.MOB.enable();
		Human.MODEL.getMesh().bind();
		Human.MODEL.getTexture().bind(Human.TEXTURE_LOCATION);
		for(Human e : humans) {
			e.prepare();
			Human.MODEL.draw();
		}
		Human.MODEL.getMesh().unbind();
		Human.MODEL.getTexture().unbind(Human.TEXTURE_LOCATION);
		Renderer.MOB.disable();
	}
	
	public void renderStructures(Renderer renderer) {
		Renderer.STRUCTURE.enable();
		
		//RENDER BASE STRUCTURES
		Collections.sort(structures, structureSorter);
		int id = -1;
		Structure prevStruct = null;
		for(Structure e : structures) {
			if(e.id != id) {
				if(prevStruct != null) prevStruct.unbind();
				e.bind();
				id = e.id;
			}
			e.prepare();
			e.model.draw();
		}
		
		Renderer.STRUCTURE.disable();
		
		Renderer.DEFAULT.enable();
		
		id = -1;
		for(Structure e : structures) {
			if(e.overlay == null || !e.usingOverlay()) continue;
			
			if(e.id != id) {
				e.bindOverlay();
				id = e.id;
			}
			e.prepareOverlay();
			e.overlay.draw();
		}
		
		
	}
	
	public boolean checkSolid(int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= height) return true;
		
		Tile tile = Game.tiles.get(getTile(x, y));
		if(tile.solid || getStructure(x, y) > 0) return true;
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
	
	public Structure selectStructure(float x, float y) {
		AABB box = new AABB(x, y, 1, 1);
		Structure structure = null;
		for(Structure e : game.world.structures) {
			AABB structBox = e.getAABB();
			if(Physics.getCollision(box, structBox) && (structure == null || structure.position.z < e.position.z)) structure = e;
		}
		return structure;
	}
	
	public Structure getNearestStructure(int type, float x, float y) {
		Structure struct = null;
		float structDist = Float.MAX_VALUE;
		for(Structure e : structures) {
			if(e.id != type) continue;
			
			float dist = Math.abs(e.position.x - x) + Math.abs(e.position.y - y);
			if(dist < structDist) struct = e;
		}
		return struct;
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
	
	public ArrayList<Vector2i> getLayerSolidTiles(int layer, int x, int y, int radius) {
		ArrayList<Vector2i> tiles = new ArrayList<Vector2i>();
		int x1 = x - radius;
		int y1 = y - radius;
		int x2 = x + radius;
		int y2 = y + radius;
		for(int i = x1; i < x2; i++) {
			for(int j = y1; j < y2; j++) {
				if(getStructureTop(i, j) != layer) {
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
	
	//GENERATE WORLD
	public void generate(int seed) {
		System.out.println("WORLD GENERATED WITH SEED: " + seed);
		
		for(int i = 0; i < width * height; i++) {
			array[i] = Tile.DEFAULT;
		}
	}
	
	private float generateNoiseValue(int seed, int x, int y) {
		Biome b = biomes.get(getBiome(x, y));
		Noise2D noise = new Noise2D(seed, b.octaves, b.persistance, b.frequency);
		float noiseValue = noise.generate(x, y);
		return noiseValue;
	}
	
	private void generateElevationArray(int seed, float[] layers) {
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				float value = generateNoiseValue(seed, x, y);
				setElevation(value, x, y);
			}
		}
		
		//CLEAN UP
		int octaves = 3;
		for(int i = 0; i < octaves; i++) {
			for(int x = 0; x < width; x++) {
				for(int y = 0; y < height; y++) {
					float value = getElevation(x, y);
					float up = getElevation(x, y - 1);
					float down = getElevation(x, y + 1);
					float left = getElevation(x - 1, y);
					float right = getElevation(x + 1, y);
					float[] neighbors = new float[] {up, down, left, right};
					
					for(int j = 0; j < layers.length; j++) {
						float layer = layers[j];
						if(value < layer) continue;
						
						int sum = 0;
						for(int k = 0; k < neighbors.length; k++) {
							if(neighbors[k] > layer) sum++;
						}
						
						if(sum < 2) setElevation(layer - .0001f, x, y);
						else {
							if(up < layer && down < layer && left > layer && right > layer) setElevation(layer - .0001f, x, y);
							if(up > layer && down > layer && left < layer && right < layer) setElevation(layer - .0001f, x, y);
						}
					}
				}
			}
		}
	}
	
	private void generateBiomeArray(int seed) {
		//int seed, int octaves, float persistance, float frequency
		Noise2D tempNoise = new Noise2D(seed + 1, 4, .4f, 1/64f);
		Noise2D humNoise = new Noise2D(seed + 2, 4, .4f, 1/32f);
		
		for(int x = 0; x < width; x++) {
			for(int y = 0; y < height; y++) {
				float tempValue = tempNoise.generate(x, y);
				float humValue = humNoise.generate(x, y);
				
				boolean exit = false;
				for(Biome b : biomes) {
					if(!exit && b.valid(tempValue, humValue)) {
						exit = true;
						setBiome(b.id, x, y);
					}
				}
			}
		}
	}
	
	public void generateCliff(int x, int y, float[] layers) {
		float value = getElevation(x, y);
		float up = getElevation(x, y - 1);
		float down = getElevation(x, y + 1);
		float left = getElevation(x - 1, y);
		float right = getElevation(x + 1, y);
		float upleft = getElevation(x - 1, y - 1);
		float upright = getElevation(x + 1, y - 1);
		float downleft = getElevation(x - 1, y + 1);
		float downright = getElevation(x + 1, y + 1);
		
		boolean upBoolean = false;
		boolean downBoolean = false;
		boolean complete = false;
		for(int i = 0; i < layers.length; i++) {
			float layer = layers[i];
			if(value > layer) {
				if(down > layer && up < layer) {
					complete = true;
					upBoolean = true;
					setTile(Tile.CLIFF_UP, x, y);
				}
				if(up > layer && down < layer) {
					complete = true;
					downBoolean = true;
					setTile(Tile.CLIFF_DOWN, x, y);
				}
				if(right > layer && left < layer) {
					complete = true;
					setTile(Tile.CLIFF_LEFT, x, y);
					if(upBoolean) setTile(Tile.CLIFF_UPLEFT, x, y);
					if(downBoolean) setTile(Tile.CLIFF_DOWNLEFT, x, y);
				}
				if(left > layer && right < layer) {
					complete = true;
					setTile(Tile.CLIFF_RIGHT, x, y);
					if(upBoolean) setTile(Tile.CLIFF_UPRIGHT, x, y);
					if(downBoolean) setTile(Tile.CLIFF_DOWNRIGHT, x, y);
				}
				if(!complete) {
					if(upleft < layer) setTile(Tile.CLIFF_UPLEFT_IN, x, y);
					if(upright < layer) setTile(Tile.CLIFF_UPRIGHT_IN, x, y);
					if(downleft < layer) setTile(Tile.CLIFF_DOWNLEFT_IN, x, y);
					if(downright < layer) setTile(Tile.CLIFF_DOWNRIGHT_IN, x, y);
				}
			}
		}
	}
	
	private float getRandomNoise(int seed, int x, int y) {
		Random rng = new Random(hash((int) hash(x, y), seed));
		int r = rng.nextInt();
		return (float)(r & 0x7fff)/(float)0x7fff;
	}
	
	private long hash(int x, int y) {
		int a = x >= 0 ? 2 * x : -2 * x - 1;
		int b = y >= 0 ? 2 * y : -2 * y - 1;
		return a >= b ? a * a + a + b : a + b * b;
	}

}

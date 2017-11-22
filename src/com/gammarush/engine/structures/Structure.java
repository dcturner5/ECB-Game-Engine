package com.gammarush.engine.structures;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.interactives.Door;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.math.matrix.Matrix4f;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector2i;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.physics.AABB;
import com.gammarush.engine.tiles.Tile;

public class Structure {
	
	public static final int STONE_TOWER = 1;
	public static final int LUMBER_SHACK = 2;
	public static final int FARM_SHACK = 3;
	public static final int TOWN_HALL = 4;
	public static final int STONE_GATE = 5;
	public static final int STONE_STAIR = 6;
	public static final int STONE_STAIR_FRONT = 7;
	
	public static final int TEXTURE_LOCATION = 0;
	public static final int NORMAL_MAP_LOCATION = 1;
	public static final int OVERLAY_LOCATION = 2;
	
	public Game game;
	public int id;
	public Vector3f position;
	public int width;
	public int height;
	public int layer;
	public Model model;
	public Model overlay;
	
	public Door enterDoor = null;
	public Door exitDoor = null;
	
	public Vector3f enterDoorPosition = null;
	public Vector2f enterDoorPlacement = null;
	public Vector3f exitDoorPosition = null;
	public Vector2f exitDoorPlacement = null;
	
	public Structure(int id, Vector3f position, int width, int height, int layer, Model model, Game game) {
		this.game = game;
		this.id = id;
		this.position = position;
		this.width = width;
		this.height = height;
		this.layer = layer;
		this.model = model;
	}
	
	public void update() {
		
	}
	
	public void render() {
		model.getMesh().bind();
		model.getTexture().bind(TEXTURE_LOCATION);
		model.draw();
		model.getTexture().unbind(TEXTURE_LOCATION);
	}
	
	public void prepare() {
		Renderer.STRUCTURE.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.scale(new Vector3f(width / model.WIDTH, height / model.HEIGHT, 0)))
				.add(new Vector3f(width / 2, height / 2, 0)));
	}
	
	public void bind() {
		model.getMesh().bind();
		if(model.getTexture() != null) model.getTexture().bind(TEXTURE_LOCATION);
		if(model.getNormalMap() != null) model.getNormalMap().bind(NORMAL_MAP_LOCATION);
	}
	
	public void unbind() {
		model.getMesh().unbind();
		if(model.getTexture() != null) model.getTexture().unbind(TEXTURE_LOCATION);
		if(model.getNormalMap() != null) model.getNormalMap().unbind(NORMAL_MAP_LOCATION);
	}
	
	public boolean usingOverlay() {
		return true;
	}
	
	public void setOverlay(Model overlay) {
		this.overlay = overlay;
	}
	
	public void prepareOverlay() {
		Renderer.STRUCTURE.setUniformMat4f("ml_matrix", Matrix4f.translate(position.add(0, 0, .0001f)).multiply(Matrix4f.scale(new Vector3f(width / model.WIDTH, height / model.HEIGHT, 0)))
				.add(new Vector3f(width / 2, height / 2, 0)));
	}
	
	public void bindOverlay() {
		if(overlay != null) {
			model.getMesh().bind();
			if(overlay.getTexture() != null) overlay.getTexture().bind(TEXTURE_LOCATION);
			if(overlay.getNormalMap() != null) overlay.getNormalMap().bind(NORMAL_MAP_LOCATION);
		}
	}
	
	public void remove() {
		StructureData data = getStructureData(id);
		int tileXOffsetLeft = data.placement.x;
		int tileXOffsetRight = data.tileWidth - tileXOffsetLeft - 1;
		int tileYOffsetUp = data.placement.y - data.baseOffset - 1;
		int tileYOffsetDown = data.baseHeight - tileYOffsetUp - 1;
		
		int tileX = (int) Math.floor(position.x / Tile.WIDTH) + data.placement.x;
		int tileY = (int) Math.floor(position.y / Tile.HEIGHT) + data.placement.y - 1;
		
		for(int x = tileX - tileXOffsetLeft; x <= tileX + tileXOffsetRight; x++) {
			for(int y = tileY - tileYOffsetUp; y <= tileY + tileYOffsetDown; y++) {
				game.world.setStructure(0, x, y);
				game.world.setStructureTop(0, x, y);
			}
		}
		
		game.world.doors.remove(enterDoor);
		game.world.doors.remove(exitDoor);
		game.world.structures.remove(this);
	}
	
	public void setDoorPositions(Vector3f enterDoorPosition, Vector2f enterDoorPlacement, Vector3f exitDoorPosition, Vector2f exitDoorPlacement) {
		this.enterDoorPosition = enterDoorPosition;
		this.enterDoorPlacement = enterDoorPlacement;
		this.exitDoorPosition = exitDoorPosition;
		this.exitDoorPlacement = exitDoorPlacement;
	}
	
	public boolean usingDoors() {
		if(enterDoorPosition == null || enterDoorPlacement == null || exitDoorPlacement == null || exitDoorPlacement == null) return false;
		return true;
	}
	
	public void createDoors() {
		if(enterDoorPosition == null || enterDoorPlacement == null || exitDoorPlacement == null || exitDoorPlacement == null ||
				enterDoor != null || exitDoor != null) {
			return;
		}
		
		enterDoor = new Door(enterDoorPosition, enterDoorPlacement, false, this, game);
		exitDoor = new Door(exitDoorPosition, exitDoorPlacement, true, this, game);
		enterDoor.setDestination(exitDoor);
		exitDoor.setDestination(enterDoor);
		
		game.world.doors.add(enterDoor);
		game.world.doors.add(exitDoor);
	}
	
	public void removeDoors() {
		if(enterDoor == null && exitDoor == null) {
			return;
		}
		
		game.world.doors.remove(enterDoor);
		game.world.doors.remove(exitDoor);
		
		enterDoor = null;
		exitDoor = null;
	}
	
	public AABB getAABB() {
		return new AABB(position.x, position.y, width, height);
	}
	
	public boolean checkPlacement(int worldX, int worldY, int localX, int localY) {
		if(game.world.checkSolid(worldX, worldY)) return false;
		return true;
	}
	
	public static StructureData getStructureData(int type) {
		Structure struct = null;
		String structName = "";
		int structWidth = 0, structHeight = 0, structLayer = 0, structBaseHeight = 0, structBaseOffset = 0;
		int[] structBaseCollisionArray = new int[] {}, structTopCollisionArray = new int[] {};
		Vector2i structPlacement = new Vector2i();
		
		switch(type) {
		case STONE_TOWER:
			struct = new StoneTower(null, null);
			structName = StoneTower.NAME;
			structWidth = StoneTower.WIDTH;
			structHeight = StoneTower.HEIGHT;
			structLayer = StoneTower.LAYER;
			structBaseHeight = StoneTower.BASE_HEIGHT;
			structBaseOffset = StoneTower.BASE_OFFSET;
			structBaseCollisionArray = StoneTower.BASE_COLLISION_ARRAY;
			structTopCollisionArray = StoneTower.TOP_COLLISION_ARRAY;
			structPlacement = StoneTower.PLACEMENT;
			break;
		case STONE_GATE:
			struct = new StoneGate(null, null);
			structName = StoneGate.NAME;
			structWidth = StoneGate.WIDTH;
			structHeight = StoneGate.HEIGHT;
			structLayer = StoneGate.LAYER;
			structBaseHeight = StoneGate.BASE_HEIGHT;
			structBaseOffset = StoneGate.BASE_OFFSET;
			structBaseCollisionArray = StoneGate.BASE_COLLISION_ARRAY;
			structTopCollisionArray = StoneGate.TOP_COLLISION_ARRAY;
			structPlacement = StoneGate.PLACEMENT;
			break;
		case STONE_STAIR:
			struct = new StoneStair(null, null);
			structName = StoneStair.NAME;
			structWidth = StoneStair.WIDTH;
			structHeight = StoneStair.HEIGHT;
			structLayer = StoneStair.LAYER;
			structBaseHeight = StoneStair.BASE_HEIGHT;
			structBaseOffset = StoneStair.BASE_OFFSET;
			structBaseCollisionArray = StoneStair.BASE_COLLISION_ARRAY;
			structTopCollisionArray = StoneStair.TOP_COLLISION_ARRAY;
			structPlacement = StoneStair.PLACEMENT;
			break;
		case STONE_STAIR_FRONT:
			struct = new StoneStairFront(null, null);
			structName = StoneStairFront.NAME;
			structWidth = StoneStairFront.WIDTH;
			structHeight = StoneStairFront.HEIGHT;
			structLayer = StoneStairFront.LAYER;
			structBaseHeight = StoneStairFront.BASE_HEIGHT;
			structBaseOffset = StoneStairFront.BASE_OFFSET;
			structBaseCollisionArray = StoneStairFront.BASE_COLLISION_ARRAY;
			structTopCollisionArray = StoneStairFront.TOP_COLLISION_ARRAY;
			structPlacement = StoneStairFront.PLACEMENT;
			break;
		case LUMBER_SHACK:
			struct = new LumberShack(null, null);
			structName = LumberShack.NAME;
			structWidth = LumberShack.WIDTH;
			structHeight = LumberShack.HEIGHT;
			structLayer = LumberShack.LAYER;
			structBaseHeight = LumberShack.BASE_HEIGHT;
			structBaseOffset = LumberShack.BASE_OFFSET;
			structBaseCollisionArray = LumberShack.BASE_COLLISION_ARRAY;
			structTopCollisionArray = LumberShack.TOP_COLLISION_ARRAY;
			structPlacement = LumberShack.PLACEMENT;
			break;
		case FARM_SHACK:
			struct = new FarmShack(null, null);
			structName = FarmShack.NAME;
			structWidth = FarmShack.WIDTH;
			structHeight = FarmShack.HEIGHT;
			structLayer = FarmShack.LAYER;
			structBaseHeight = FarmShack.BASE_HEIGHT;
			structBaseOffset = FarmShack.BASE_OFFSET;
			structBaseCollisionArray = FarmShack.BASE_COLLISION_ARRAY;
			structTopCollisionArray = FarmShack.TOP_COLLISION_ARRAY;
			structPlacement = FarmShack.PLACEMENT;
			break;
		case TOWN_HALL:
			struct = new TownHall(null, null);
			structName = TownHall.NAME;
			structWidth = TownHall.WIDTH;
			structHeight = TownHall.HEIGHT;
			structLayer = TownHall.LAYER;
			structBaseHeight = TownHall.BASE_HEIGHT;
			structBaseOffset = TownHall.BASE_OFFSET;
			structBaseCollisionArray = TownHall.BASE_COLLISION_ARRAY;
			structTopCollisionArray = TownHall.TOP_COLLISION_ARRAY;
			structPlacement = TownHall.PLACEMENT;
			break;
		}
		
		return new StructureData(struct, structName, structWidth, structHeight, structLayer, structBaseHeight, structBaseOffset, structBaseCollisionArray, structTopCollisionArray, structPlacement);
	}

}

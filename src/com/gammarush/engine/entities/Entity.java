package com.gammarush.engine.entities;

import com.gammarush.engine.entities.components.Component;
import com.gammarush.engine.entities.components.ComponentHashMap;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.input.InputManager;
import com.gammarush.engine.math.matrix.Matrix4f;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector2i;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.physics.AABB;
import com.gammarush.engine.physics.Physics;
import com.gammarush.engine.player.PlayerManager;
import com.gammarush.engine.quests.QuestManager;
import com.gammarush.engine.scripts.ScriptManager;
import com.gammarush.engine.tiles.Tile;
import com.gammarush.engine.ui.UIManager;
import com.gammarush.engine.world.Chunk;
import com.gammarush.engine.world.World;
import com.gammarush.engine.world.WorldManager;

public class Entity {
	
	private World world;
	private Vector2i chunkPosition;
	
	public Vector3f position;
	public int width;
	public int height;
	public Model model;
	
	public float rotation = 0.0f;
	
	public Physics physics;
	public Vector2f velocity = new Vector2f();
	
	private boolean solid = false;
	private AABB collisionBox;
	
	private ComponentHashMap components = new ComponentHashMap();
	
	public static final int TEXTURE_LOCATION = 0;
	public static final int NORMAL_MAP_LOCATION = 1;
	
	public static final int DIRECTION_UP = 0;
	public static final int DIRECTION_DOWN = 1;
	public static final int DIRECTION_LEFT = 2;
	public static final int DIRECTION_RIGHT = 3;
	
	public Entity(Vector3f position, int width, int height, Model model) {
		this.position = position;
		this.width = width;
		this.height = height;
		this.model = model;
		this.world = null;
		this.chunkPosition = getChunkPosition();
		
		this.physics = new Physics(width, height, world);
		this.setCollisionBox(new AABB(0, 0, width, height));
	}
	
	public Entity(Vector2f position, int width, int height, Model model) {
		this.position = new Vector3f(position.x, position.y, Renderer.ENTITY_LAYER);
		this.width = width;
		this.height = height;
		this.model = model;
		this.world = null;
		this.chunkPosition = getChunkPosition();
		
		this.physics = new Physics(width, height, world);
		this.setCollisionBox(new AABB(0, 0, width, height));
	}
	
	public void update(double delta) {
		for(Component c : components.getArray()) {
			c.update(delta);
		}
		
		Vector2i cp = getChunkPosition();
		Vector2i lcp = getLastChunkPosition();
		if(!cp.equals(lcp)) {
			world.syncEntity(this);
		}
	}
	
	public void render() {
		/*renderComponents();
		
		model.getMesh().bind();
		model.getTexture().bind(TEXTURE_LOCATION);
		model.draw();
		model.getMesh().unbind();
		model.getTexture().unbind(TEXTURE_LOCATION);*/
	}
	
	public void prepare() {
		Renderer.DEFAULT.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(rotation).add(new Vector3f(width / 2, height / 2, 0)))
				.multiply(Matrix4f.scale(new Vector3f(width, height, 0))));
	}
	
	public void addComponent(Component component) {
		components.put(component);
	}
	
	public Component getComponent(String name) {
		return components.get(name);
	}
	
	public void removeComponent(String name) {
		components.remove(name);
	}
	
	public void renderComponents() {
		for(Component c : components.getArray()) {
			c.render();
		}
	}
	
	public boolean getScreenPresence() {
		Renderer renderer = world.getRenderer();
		AABB screen = new AABB(-renderer.getCamera().position.x, -renderer.getCamera().position.y, renderer.getWidth() / renderer.getCamera().getZoom(), renderer.getHeight() / renderer.getCamera().getZoom());
		AABB entity = new AABB(position.x, position.y, width, height);
		return Physics.getCollision(screen, entity);
	}
	
	public World getWorld() {
		return world;
	}
	
	public Vector2f getPosition() {
		return new Vector2f(position.x, position.y);
	}
	
	public Vector2i getChunkPosition() {
		return new Vector2i((int) (position.x / Tile.WIDTH / Chunk.WIDTH), (int) (position.y / Tile.HEIGHT / Chunk.HEIGHT));
	}
	
	public Vector2f getLocalChunkPosition() {
		float x = position.x / Tile.WIDTH / Chunk.WIDTH;
		float y = position.y / Tile.HEIGHT / Chunk.HEIGHT;
		return new Vector2f((float) (x - Math.floor(x)) * Chunk.WIDTH, (float) (y - Math.floor(y)) * Chunk.HEIGHT);
	}
	
	public Vector2i getLastChunkPosition() {
		return chunkPosition;
	}
	
	public Vector2i getTilePosition() {
		return new Vector2i((int) (position.x / Tile.WIDTH), (int) (position.y / Tile.HEIGHT));
	}
	
	public AABB getAABB() {
		return new AABB(position.x + collisionBox.x, position.y + collisionBox.y, collisionBox.width, collisionBox.height);
	}
	
	public void setCollisionBox(AABB collisionBox) {
		this.collisionBox = collisionBox;
		this.physics = new Physics(collisionBox.width, collisionBox.height, physics.getWorld());
	}

	public AABB getCollisionBox() {
		return collisionBox;
	}

	public boolean getSolid() {
		return solid;
	}
	
	public void setLastChunkPosition(Vector2i chunkPosition) {
		this.chunkPosition = chunkPosition;
	}
	
	public void setSolid(boolean solid) {
		this.solid = solid;
	}
	
	public void setWorld(World world) {
		this.world = world;
	}
	
	public void setPosition(Vector2f position) {
		this.position.x = position.x;
		this.position.y = position.y;
	}
	
	public InputManager getInputManager() {
		return getWorld().getWorldManager().getInputManager();
	}
	
	public QuestManager getQuestManager() {
		return getWorld().getWorldManager().getQuestManager();
	}
	
	public Renderer getRenderer() {
		return getWorld().getWorldManager().getRenderer();
	}
	
	public PlayerManager getPlayerManager() {
		return getWorld().getWorldManager().getPlayerManager();
	}
	
	public ScriptManager getScriptManager() {
		return getWorld().getWorldManager().getScriptManager();
	}
	
	public UIManager getUIManager() {
		return getWorld().getWorldManager().getUIManager();
	}
	
	public WorldManager getWorldManager() {
		return getWorld().getWorldManager();
	}

}

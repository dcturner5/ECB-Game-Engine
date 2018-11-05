package com.gammarush.engine;

import com.gammarush.engine.entities.items.ItemHashMap;
import com.gammarush.engine.entities.items.ItemLoader;
import com.gammarush.engine.entities.items.ItemTemplate;
import com.gammarush.engine.entities.items.clothing.ClothingHashMap;
import com.gammarush.engine.entities.items.clothing.ClothingLoader;
import com.gammarush.engine.entities.items.clothing.ClothingTemplate;
import com.gammarush.engine.entities.mobs.MobHashMap;
import com.gammarush.engine.entities.mobs.MobLoader;
import com.gammarush.engine.entities.mobs.MobTemplate;
import com.gammarush.engine.entities.mobs.actors.Actor;
import com.gammarush.engine.entities.mobs.actors.ActorHashMap;
import com.gammarush.engine.entities.mobs.actors.ActorLoader;
import com.gammarush.engine.entities.statics.StaticHashMap;
import com.gammarush.engine.entities.statics.StaticLoader;
import com.gammarush.engine.entities.statics.StaticTemplate;
import com.gammarush.engine.entities.vehicles.VehicleHashMap;
import com.gammarush.engine.entities.vehicles.VehicleLoader;
import com.gammarush.engine.entities.vehicles.VehicleTemplate;
import com.gammarush.engine.events.EventManager;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.input.InputManager;
import com.gammarush.engine.player.PlayerManager;
import com.gammarush.engine.quests.QuestManager;
import com.gammarush.engine.scripts.ScriptManager;
import com.gammarush.engine.tiles.Tile;
import com.gammarush.engine.tiles.TileHashMap;
import com.gammarush.engine.tiles.TileLoader;
import com.gammarush.engine.ui.UIManager;
import com.gammarush.engine.world.WorldManager;

public class GameManager {
	
	public static ActorHashMap actors;
	public static MobHashMap mobs;
	public static ItemHashMap items;
	public static ClothingHashMap clothings;
	public static VehicleHashMap vehicles;
	public static StaticHashMap statics;
	public static TileHashMap tiles;
	
	private EventManager eventManager;
	private InputManager inputManager;
	private PlayerManager playerManager;
	private QuestManager questManager;
	private Renderer renderer;
	private ScriptManager scriptManager;
	private UIManager uiManager;
	private WorldManager worldManager;
	
	public GameManager(Game game) {
		tiles = TileLoader.load("res/tiles/data.json");
		statics = StaticLoader.load("res/entities/statics/data.json");
		mobs = MobLoader.load("res/entities/mobs/data.json");
		items = ItemLoader.load("res/entities/items/data.json");
		clothings = ClothingLoader.load("res/entities/items/data.json");
		vehicles = VehicleLoader.load("res/entities/vehicles/data.json");
		
		questManager = new QuestManager(this);
		actors = ActorLoader.load("res/actors/data.json", questManager);
		
		worldManager = new WorldManager(this);
		scriptManager = new ScriptManager(this);
		renderer = new Renderer(game.width, game.height, game.scale, this);
		uiManager = new UIManager(this);
		inputManager = new InputManager(game.window, this);
		playerManager = new PlayerManager(this);
		
		questManager.loadScripts();
		questManager.getQuest("main").start();
		
		eventManager = new EventManager(this);
	}
	
	public void update(double delta) {
		playerManager.update(delta);
		uiManager.update(delta);
		worldManager.update(delta);
	}
	
	public void render() {
		renderer.clear();
		renderer.render();
	}
	
	public static Actor getActor(int id) {
		return actors.get(id);
	}
	
	public static Actor getActor(String name) {
		return actors.get(name);
	}
	
	public static MobTemplate getMob(int id) {
		return mobs.get(id);
	}
	
	public static MobTemplate getMob(String name) {
		return mobs.get(name);
	}
	
	public static ItemTemplate getItem(int id) {
		return items.get(id);
	}
	
	public static ItemTemplate getItem(String name) {
		return items.get(name);
	}
	
	public static ClothingTemplate getClothing(int id) {
		return clothings.get(id);
	}
	
	public static ClothingTemplate getClothing(String name) {
		return clothings.get(name);
	}
	
	public static VehicleTemplate getVehicle(int id) {
		return vehicles.get(id);
	}
	
	public static VehicleTemplate getVehicle(String name) {
		return vehicles.get(name);
	}
	
	public static StaticTemplate getStatic(int id) {
		return statics.get(id);
	}
	
	public static StaticTemplate getStatic(String name) {
		return statics.get(name);
	}
	
	public static Tile getTile(int id) {
		return tiles.get(id);
	}
	
	public static Tile getTile(String name) {
		return tiles.get(name);
	}
	
	public EventManager getEventManager() {
		return eventManager;
	}
	
	public InputManager getInputManager() {
		return inputManager;
	}
	
	public PlayerManager getPlayerManager() {
		return playerManager;
	}
	
	public QuestManager getQuestManager() {
		return questManager;
	}
	
	public Renderer getRenderer() {
		return renderer;
	}
	
	public ScriptManager getScriptManager() {
		return scriptManager;
	}
	
	public UIManager getUIManager() {
		return uiManager;
	}
	
	public WorldManager getWorldManager() {
		return worldManager;
	}

}

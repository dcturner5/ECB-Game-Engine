package com.gammarush.engine.quests;

import java.util.UUID;

import com.gammarush.axil.memory.AxilMemory;
import com.gammarush.engine.GameManager;
import com.gammarush.engine.entities.items.clothing.ClothingTemplate;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.mobs.actors.Actor;
import com.gammarush.engine.entities.mobs.behaviors.AttackBehavior;
import com.gammarush.engine.entities.mobs.components.AIComponent;
import com.gammarush.engine.entities.mobs.components.ClothingComponent;
import com.gammarush.engine.entities.mobs.components.ProjectileComponent;
import com.gammarush.engine.events.EventManager;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.input.InputManager;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.player.PlayerManager;
import com.gammarush.engine.scripts.ScriptManager;
import com.gammarush.engine.tiles.Tile;
import com.gammarush.engine.ui.UIManager;
import com.gammarush.engine.utils.json.JSONLoader;
import com.gammarush.engine.world.WorldManager;

public class QuestManager {
	
	private GameManager gameManager;
	
	private QuestHashMap quests = new QuestHashMap();
	private DialogueHashMap dialogues = new DialogueHashMap();
	
	public QuestManager(GameManager gameManager) {
		this.gameManager = gameManager;
	}
	
	public void addDialogue(Dialogue dialogue) {
		dialogues.put(dialogue);
	}
	
	public Quest getQuest(String name) {
		return quests.get(name);
	}
	
	public Dialogue getDialogue(String name) {
		return dialogues.get(name);
	}
	
	public void loadMethods() {
		getScriptManager().addMethod("console", 2, (int[] args, AxilMemory memory) -> {
			String value = memory.getString(args[0]);
			getUIManager().console.print(value);
			return -1;
		});
		
		getScriptManager().addMethod("scale", 2, (int[] args, AxilMemory memory) -> {
			int address = args[1];
			int value = memory.getInt(args[0]) * Renderer.SCALE;
			memory.setInt(address, value);
			return -1;
		});
		
		getScriptManager().addMethod("getActor", 2, (int[] args, AxilMemory memory) -> {
			int address = args[1];
			String name = memory.getString(args[0]);
			Actor actor = GameManager.getActor(name);
			memory.setString(address, actor.getUUID().toString());
			return -1;
		});
		
		getScriptManager().addMethod("getPlayer", 1, (int[] args, AxilMemory memory) -> {
			int address = args[0];
			Mob mob = getPlayerManager().getMob();
			memory.setString(address, mob.getUUID().toString());
			return -1;
		});
		
		getScriptManager().addMethod("setPlayer", 2, (int[] args, AxilMemory memory) -> {
			int address = args[1];
			Mob mob = (Mob) getWorldManager().getWorld().getEntity(UUID.fromString(memory.getString(args[0])));
			if(mob == null) {
				memory.setBoolean(address, false);
				return -1;
			}
			getPlayerManager().setMob(mob);
			memory.setBoolean(address, true);
			return -1;
		});
		
		getScriptManager().addMethod("getWorld", 1, (int[] args, AxilMemory memory) -> {
			int address = args[0];
			String value = getWorldManager().getWorld().getName();
			memory.setString(address, value);
			return -1;
		});
		
		getScriptManager().addMethod("setWorld", 2, (int[] args, AxilMemory memory) -> {
			String name = memory.getString(args[0]);
			getWorldManager().getWorld().removeMob(getPlayerManager().getMob());
			getWorldManager().setWorld(name);
			getWorldManager().getWorld().addMob(getPlayerManager().getMob());
			getPlayerManager().getMob().setWorld(getWorldManager().getWorld());
			return -1;
		});
		
		getScriptManager().addMethod("spawn", 4, (int[] args, AxilMemory memory) -> {
			String type = memory.getString(args[0]);
			int x = memory.getInt(args[1]);
			int y = memory.getInt(args[2]);
			Mob e = new Mob(GameManager.getMob(type), new Vector2f(x, y));
			e.setWorld(getWorldManager().getWorld());
			getWorldManager().getWorld().addMob(e);
			
			int address = args[3];
			memory.setString(address, e.getUUID().toString());
			return -1;
		});
		
		getScriptManager().addMethod("shoot", 7, (int[] args, AxilMemory memory) -> {
			String type = memory.getString(args[0]);
			int x = memory.getInt(args[1]);
			int y = memory.getInt(args[2]);
			float vx = memory.getFloat(args[3]);
			float vy = memory.getFloat(args[4]);
			Mob owner = (Mob) getWorldManager().getWorld().getEntity(UUID.fromString(memory.getString(args[5])));
			
			Mob e = new Mob(GameManager.getMob(type), new Vector2f(x, y));
			ProjectileComponent pc = (ProjectileComponent) e.getComponent("projectile");
			pc.setVelocity(new Vector2f(vx, vy));
			pc.setOwner(owner);
			
			e.setWorld(getWorldManager().getWorld());
			getWorldManager().getWorld().addMob(e);
			
			int address = args[5];
			memory.setString(address, e.getUUID().toString());
			return -1;
		});
		
		getScriptManager().addMethod("attack", 3, (int[] args, AxilMemory memory) -> {
			int address = args[2];
			Mob mob = (Mob) getWorldManager().getWorld().getEntity(UUID.fromString(memory.getString(args[0])));
			if(mob == null) {
				memory.setBoolean(address, false);
				return -1;
			}
			AIComponent ac = (AIComponent) mob.getComponent("ai");
			if(ac == null) {
				memory.setBoolean(address, false);
				return -1;
			}
			Mob target = (Mob) getWorldManager().getWorld().getEntity(UUID.fromString(memory.getString(args[1])));
			if(target == null) {
				memory.setBoolean(address, false);
				return -1;
			}
			ac.addBehavior(new AttackBehavior(target));
			memory.setBoolean(address, true);
			return -1;
		});
		
		getScriptManager().addMethod("printPosition", 1, (int[] args, AxilMemory memory) -> {
			int x = (int) Math.floor(getPlayerManager().getMob().position.x / Tile.WIDTH) * Tile.WIDTH;
			int y = (int) Math.floor(getPlayerManager().getMob().position.y / Tile.HEIGHT) * Tile.HEIGHT;
			getUIManager().console.print(x + ", " + y);
			return -1;
		});
		
		getScriptManager().addMethod("getAttribute", 3, (int[] args, AxilMemory memory) -> {
			int address = args[2];
			Mob mob = (Mob) getWorldManager().getWorld().getEntity(UUID.fromString(memory.getString(args[0])));
			if(mob == null) {
				memory.setInt(address, 0);
				return -1;
			}
			String attribute = memory.getString(args[1]);
			if(attribute.equals("x")) {
				memory.setInt(address, (int) mob.position.x);
			}
			if(attribute.equals("y")) {
				memory.setInt(address, (int) mob.position.y);
			}
			if(attribute.equals("direction")) {
				memory.setInt(address, mob.direction);
			}
			return -1;
		});
		
		getScriptManager().addMethod("getX", 1, (int[] args, AxilMemory memory) -> {
			int address = args[0];
			int value = (int) getPlayerManager().getMob().position.x;
			memory.setInt(address, value);
			return -1;
		});
		
		getScriptManager().addMethod("getY", 1, (int[] args, AxilMemory memory) -> {
			int address = args[0];
			int value = (int) getPlayerManager().getMob().position.y;
			memory.setInt(address, value);
			return -1;
		});
		
		getScriptManager().addMethod("getTileX", 1, (int[] args, AxilMemory memory) -> {
			int address = args[0];
			int value = (int) Math.floor(getPlayerManager().getMob().position.x / Tile.WIDTH) * Tile.WIDTH;
			memory.setInt(address, value);
			return -1;
		});
		
		getScriptManager().addMethod("getTileY", 1, (int[] args, AxilMemory memory) -> {
			int address = args[0];
			int value = (int) Math.floor(getPlayerManager().getMob().position.y / Tile.HEIGHT) * Tile.HEIGHT;
			memory.setInt(address, value);
			return -1;
		});
		
		getScriptManager().addMethod("addClothing", 3, (int[] args, AxilMemory memory) -> {
			int address = args[2];
			Mob mob = (Mob) getWorldManager().getWorld().getEntity(UUID.fromString(memory.getString(args[0])));
			if(mob == null) {
				memory.setBoolean(address, false);
				return -1;
			}
			ClothingComponent cc = (ClothingComponent) mob.getComponent("clothing");
			if(cc == null) {
				memory.setBoolean(address, false);
				return -1;
			}
			ClothingTemplate ct = GameManager.clothings.get(memory.getString(args[1]));
			if(ct == null) {
				memory.setBoolean(address, false);
				return -1;
			}
			cc.outfit.add(ct);
			memory.setBoolean(address, true);
			return -1;
		});
		
		getScriptManager().addMethod("removeClothing", 3, (int[] args, AxilMemory memory) -> {
			int address = args[2];
			Mob mob = (Mob) getWorldManager().getWorld().getEntity(UUID.fromString(memory.getString(args[0])));
			if(mob == null) {
				memory.setBoolean(address, false);
				return -1;
			}
			ClothingComponent cc = (ClothingComponent) mob.getComponent("clothing");
			if(cc == null) {
				memory.setBoolean(address, false);
				return -1;
			}
			ClothingTemplate ct = GameManager.clothings.get(memory.getString(args[1]));
			if(ct == null) {
				memory.setBoolean(address, false);
				return -1;
			}
			cc.outfit.remove(ct);
			memory.setBoolean(address, true);
			return -1;
		});
	}
	
	public void loadScripts() {
		for(String name : JSONLoader.load("res/scripts/data.json").getStringArray("scripts")) {
			getScriptManager().getQueue().add(name);
		}
		getScriptManager().compile();
		
		quests = QuestLoader.load("res/quests/data.json", this);
		for(Quest q : quests.getArray()) {
			getScriptManager().getQueue().add(q.getScriptPath());
		}
		getScriptManager().compile();
		
		DialogueLoader.load("res/dialogues/data.json", this);
	}
	
	public EventManager getEventManager() {
		return gameManager.getEventManager();
	}
	
	public InputManager getInputManager() {
		return gameManager.getInputManager();
	}
	
	public PlayerManager getPlayerManager() {
		return gameManager.getPlayerManager();
	}
	
	public Renderer getRenderer() {
		return gameManager.getRenderer();
	}
	
	public ScriptManager getScriptManager() {
		return gameManager.getScriptManager();
	}
	
	public UIManager getUIManager() {
		return gameManager.getUIManager();
	}
	
	public WorldManager getWorldManager() {
		return gameManager.getWorldManager();
	}

}

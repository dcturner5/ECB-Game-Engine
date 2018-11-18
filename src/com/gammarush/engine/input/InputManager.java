package com.gammarush.engine.input;

import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowSizeCallback;
import static org.lwjgl.opengl.GL11.glViewport;

import com.gammarush.engine.GameManager;
import com.gammarush.engine.entities.mobs.components.AttackComponent;
import com.gammarush.engine.events.EventManager;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector2i;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.player.PlayerManager;
import com.gammarush.engine.quests.QuestManager;
import com.gammarush.engine.scripts.ScriptManager;
import com.gammarush.engine.tiles.Tile;
import com.gammarush.engine.ui.UIManager;
import com.gammarush.engine.ui.components.UIComponent;
import com.gammarush.engine.ui.containers.UIContainer;
import com.gammarush.engine.ui.event.EventType;
import com.gammarush.engine.world.WorldManager;

public class InputManager {
	
	private GameManager gameManager;
	
	public Vector2f mousePosition = new Vector2f();
	private Vector2f prevMouseWorldPosition = new Vector2f();
	private boolean leftMouseDown = false;
	
	public InputManager(long window, GameManager gameManager) {
		this.gameManager = gameManager;
		 
		glfwSetWindowSizeCallback(window, new WindowSizeCallback(this));
		glfwSetKeyCallback(window, new KeyCallback(this));
		glfwSetMouseButtonCallback(window, new MouseButtonCallback(this));
		glfwSetCursorPosCallback(window, new CursorPosCallback(this));
		glfwSetScrollCallback(window, new ScrollCallback(this));
	}
	
	public Vector2f getMouseWorldPosition() {
		return new Vector2f(
				(float) (mousePosition.x / getRenderer().getCamera().getZoom()) - getRenderer().getCamera().position.x, 
				(float) (mousePosition.y / getRenderer().getCamera().getZoom()) - getRenderer().getCamera().position.y
			);
	}
	
	public UIContainer getUIContainer(Vector2f position) {
		UIContainer container = null;
		for(UIContainer cont : getUIManager().getContainers()) {
			if(cont.getVisible() && cont.getCollision(position.x, position.y) && (container == null || container.getPosition().z < cont.getPosition().z)) {
				container = cont;
			}
		}
		return container;
	}
	
	public UIComponent getUIComponent(Vector2f position, UIContainer container) {
		UIComponent component = null;
		if(container != null) {
			for(UIComponent comp : container.getComponents()) {
				if(comp.getCollision(mousePosition.x, mousePosition.y)) {
					component = comp;
				}
				else {
					if(comp.getLeftClick()) {
						comp.setLeftClick(false);
						comp.activate(EventType.LEFTRELEASE);
					}
					if(comp.getRightClick()) {
						comp.setRightClick(false);
						comp.activate(EventType.RIGHTRELEASE);
					}
					if(comp.getFocus()) {
						comp.setFocus(false);
					}
					if(comp.getHover()) {
						comp.setHover(false);
						comp.activate(EventType.HOVEREXIT);
					}
				}
			}
		}
		else {
			for(UIContainer cont : getUIManager().getContainers()) {
				if(cont.getVisible()) {
					for(UIComponent comp : cont.getComponents()) {
						if(!comp.getCollision(mousePosition.x, mousePosition.y)) {
							if(comp.getLeftClick()) {
								comp.setLeftClick(false);
								comp.activate(EventType.LEFTRELEASE);
							}
							if(comp.getRightClick()) {
								comp.setRightClick(false);
								comp.activate(EventType.RIGHTRELEASE);
							}
							if(comp.getFocus() && comp.getFocusable()) {
								comp.setFocus(false);
							}
							if(comp.getHover()) {
								comp.setHover(false);
								comp.activate(EventType.HOVEREXIT);
							}
						}
					}
				}
			}
		}
		return component;
	}
	
	public boolean keyInput(int key) {
		boolean result = false;
		for(UIContainer cont : getUIManager().getContainers()) {
			if(cont.getVisible()) {
				for(UIComponent comp : cont.getComponents()) {
					if(comp.getFocus() && comp.getEditable()) {
						comp.activate(EventType.KEYINPUT, key);
						result = true;
					}
				}
			}
		}
		return result;
	}
	
	public void leftClick() {
		leftMouseDown = true;
		UIContainer container = getUIContainer(mousePosition);
		if(container != null) {
			UIComponent component = getUIComponent(mousePosition, container);
			if(component != null) {
				 if(component.getClickable()) {
					if(component.getFocusable()) component.setFocus(true);
					component.setLeftClick(true);
					component.activate(EventType.LEFTCLICK);
				}
			}
		}
		if(container == null || !container.getSolid()) {
			AttackComponent ac = (AttackComponent) getPlayerManager().getMob().getComponent("attack");
			if(ac != null) ac.attack();
		}
		Vector2f mousePos = getMouseWorldPosition();
		prevMouseWorldPosition = mousePos;
	}
	
	public void rightClick() {
		UIContainer container = getUIContainer(mousePosition);
		if(container != null) {
			UIComponent component = getUIComponent(mousePosition, container);
			if(component != null) {
				if(component.getClickable()) {
					component.setRightClick(true);
					component.activate(EventType.RIGHTCLICK);
				}
			}
		}
		if(container == null || !container.getSolid()) {
			Vector2f mousePos = getMouseWorldPosition();
			mousePos.x = (float) (Math.floor(mousePos.x / Tile.WIDTH) * Tile.WIDTH);
			mousePos.y = (float) (Math.floor(mousePos.y / Tile.HEIGHT) * Tile.HEIGHT);
			
			//AIComponent ai = (AIComponent) GameManager.getActor("NPC 2").getComponent("ai");
			//ai.addBehavior(new TravelBehavior(new Vector2i((int) mousePos.x / Tile.WIDTH, (int) mousePos.y / Tile.HEIGHT)));
			//ai.addBehavior(new AttackBehavior(getPlayerManager().getMob()));
			//worldManager.getWorld().addItem(new Item(Game.items.getRandom(), mousePos));
		}
	}
	
	public void leftRelease() {
		leftMouseDown = false;
		UIContainer container = getUIContainer(mousePosition);
		if(container != null) {
			UIComponent component = getUIComponent(mousePosition, container);
			if(component != null) {
				if(component.getLeftClick()) {
					component.setLeftClick(false);
					component.activate(EventType.LEFTRELEASE);
				}
			}
		}
		if(container == null || !container.getSolid()) {
			
		}
	}
	
	public void rightRelease() {
		UIContainer container = getUIContainer(mousePosition);
		if(container != null) {
			UIComponent component = getUIComponent(mousePosition, container);
			if(component != null) {
				if(component.getRightClick()) {
					component.setRightClick(false);
					component.activate(EventType.RIGHTRELEASE);
				}
			}
		}
		if(container == null || !container.getSolid()) {
			
		}
	}
	
	public void move(float x, float y) {
		mousePosition.x = x / getRenderer().getScreenWidth() * getRenderer().getWidth();
		mousePosition.y = y / getRenderer().getScreenHeight() * getRenderer().getHeight();
		
		UIContainer container = getUIContainer(mousePosition);
		UIComponent component = getUIComponent(mousePosition, container);
		if(component != null) {
			if(!component.getHover()) {
				component.setHover(true);
				component.activate(EventType.HOVERENTER);
			}
		}
		
		if(container == null || !container.getSolid()) {
			if(leftMouseDown) leftDrag(x, y);
		}
	}
	
	public void leftDrag(float x, float y) {
		Vector2f mousePos = getMouseWorldPosition();
		Vector2i worldPosDelta = new Vector2i(mousePos.sub(prevMouseWorldPosition));
		getRenderer().getCamera().position = getRenderer().getCamera().position.add(new Vector3f(worldPosDelta.x, worldPosDelta.y, 0f));
	}
	
	public void scroll(float deltaX, float deltaY) {
		float zoom = getRenderer().getCamera().getZoom();
		if(deltaY < 0) zoom /= 2;
		if(deltaY > 0) zoom *= 2;
		getRenderer().getCamera().setZoom(zoom);
	}
	
	public void windowSize(int width, int height) {
		getRenderer().setScreenSize(width, height);
        glViewport(0, 0, width, height);
	}
	
	public EventManager getEventManager() {
		return gameManager.getEventManager();
	}
	
	public QuestManager getQuestManager() {
		return gameManager.getQuestManager();
	}
	
	public Renderer getRenderer() {
		return gameManager.getRenderer();
	}
	
	public PlayerManager getPlayerManager() {
		return gameManager.getPlayerManager();
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

package com.gammarush.engine;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFWVidMode;

import com.gammarush.engine.SystemManager;
import com.gammarush.engine.entities.interactives.vehicles.Vehicle;
import com.gammarush.engine.entities.interactives.vehicles.VehicleHashMap;
import com.gammarush.engine.entities.interactives.vehicles.VehicleLoader;
import com.gammarush.engine.entities.items.ItemHashMap;
import com.gammarush.engine.entities.items.ItemLoader;
import com.gammarush.engine.entities.items.clothing.ClothingHashMap;
import com.gammarush.engine.entities.items.clothing.ClothingLoader;
import com.gammarush.engine.entities.mobs.MobHashMap;
import com.gammarush.engine.entities.mobs.MobLoader;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.gui.UIManager;
import com.gammarush.engine.input.Input;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.player.Player;
import com.gammarush.engine.scripts.ScriptManager;
import com.gammarush.engine.tiles.TileHashMap;
import com.gammarush.engine.tiles.TileLoader;
import com.gammarush.engine.world.World;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Game implements Runnable {
	
	public static SystemManager system = new SystemManager();

	public int width = 1280;
	public int height = 720;
	public float scale = 2f;
	
	private Thread thread;
	private boolean running = false;
	
	public long window;
	
	public Input input;
	public Renderer renderer;
	public World world;
	public Player player;
	public UIManager gui;
	public ScriptManager scriptManager;
	
	public static MobHashMap mobs;
	public static ItemHashMap items;
	public static ClothingHashMap clothings;
	public static VehicleHashMap vehicles;
	
	public static TileHashMap tiles;
	
	public void start() {
		running = true;
		thread = new Thread(this, "Game");
		thread.start();
	}
	
	private void init() {
		GLFWErrorCallback.createPrint(System.err).set();
		
		if(!glfwInit()) {
			throw new IllegalStateException("Unable to initialize GLFW");
		}
		
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
		window = glfwCreateWindow((int) (width * scale), (int) (height * scale), "", NULL, NULL);
		
		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1);
			IntBuffer pHeight = stack.mallocInt(1);

			glfwGetWindowSize(window, pWidth, pHeight);

			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
		}

		glfwMakeContextCurrent(window);
		//VSYNC: 0 = OFF, 1 = ON
		glfwSwapInterval(1);
		glfwShowWindow(window);
		
		input = new Input(this);
		
		GL.createCapabilities();
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		System.out.println("OpenGL: " + glGetString(GL_VERSION));
		
		renderer = new Renderer(width, height, this);
		renderer.setClearColor(0x000000);
		
		tiles = TileLoader.load("res/tiles/data.json");
		mobs = MobLoader.load("res/entities/mobs/data.json");
		items = ItemLoader.load("res/entities/items/data.json");
		clothings = ClothingLoader.load("res/entities/items/data.json");
		vehicles = VehicleLoader.load("res/entities/interactives/vehicles/data.json");
		
		world = new World(128, 128, this);
		world.generate((int)(Math.random() * 10000));
		
		player = new Player(new Vector3f(1024, 512, Renderer.ENTITY_LAYER), this);
		
		world.vehicles.add(new Vehicle(vehicles.get("mercury"), new Vector3f(128, 256, Renderer.ENTITY_LAYER), Vehicle.DIRECTION_LEFT, this));
		
		gui = new UIManager(this);
		scriptManager = new ScriptManager();
	}
	
	public void run() {
		init();
		
		long lastTime = System.nanoTime();
		double delta = 0.0;
		double ns = 1000000000.0 / 60.0;
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;
		
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1.0) {
				update(delta);
				//System.out.println(delta);
				updates++;
				delta--;
			}
			render();
			frames++;
			
			if(System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				glfwSetWindowTitle(window, system.MemInfo() + "        UPS: " + updates + ", FPS: " + frames);
				updates = 0;
				frames = 0;
			}
			
			if(glfwWindowShouldClose(window)) {
				running = false;
			}
		}
	}
	
	private void update(double delta) {
		glfwPollEvents();
		
		world.update(delta);
		player.update(delta);
		gui.update(delta);
	}
	
	private void render() {
		renderer.clear();
		renderer.render();
		glfwSwapBuffers(window);
	}

	public static void main(String[] args) {
		new Game().start();
	}

}
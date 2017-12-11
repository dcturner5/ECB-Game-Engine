package com.gammarush.engine;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.IntBuffer;
import java.util.HashMap;

import org.lwjgl.glfw.GLFWVidMode;

import com.gammarush.engine.SystemManager;
import com.gammarush.engine.entities.vehicles.Mercury;
import com.gammarush.engine.entities.vehicles.Vehicle;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.model.Texture;
import com.gammarush.engine.gui.UIManager;
import com.gammarush.engine.input.Input;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.player.Player;
import com.gammarush.engine.tiles.Tile;
import com.gammarush.engine.tiles.TileLoader;
import com.gammarush.engine.utils.json.JSON;
import com.gammarush.engine.utils.json.JSONLoader;
import com.gammarush.engine.world.World;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Game implements Runnable {
	
	public static SystemManager system = new SystemManager();

	public int width = 1280;
	public int height = 720;
	public float scale = 1.5f;
	
	private Thread thread;
	private boolean running = false;
	
	public long window;
	
	public Input input;
	public Renderer renderer;
	public World world;
	public Player player;
	public UIManager gui;
	
	public static HashMap<Integer, Tile> tiles = new HashMap<Integer, Tile>();
	public static HashMap<Integer, Tile> clothing = new HashMap<Integer, Tile>();
	
	
	public void start() {
		running = true;
		thread = new Thread(this, "Game");
		thread.start();
	}
	
	private void init() {
		JSON json = JSONLoader.load("res/test.json");
		System.out.println(json.getJSON("test.test1[2].test2").getClass());
		
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
		
		tiles.put(Tile.DEFAULT, new Tile(new Texture("res/tiles/forest_ground.png"), false, Tile.BLEND_TYPE_NEUTRAL));
		/*
		tiles.put(Tile.WATER, new Tile(new Texture("res/tiles/water.png"), false, Tile.BLEND_TYPE_NEUTRAL));
		tiles.put(Tile.TREE, new Tile(new Texture("res/tiles/forest_ground.png"), true, Tile.BLEND_TYPE_NEUTRAL));
		
		tiles.put(Tile.PLAINS_GROUND, new Tile(new Texture("res/tiles/plains_ground.png"), false, Tile.BLEND_TYPE_DOMINANT));
		tiles.put(Tile.FOREST_GROUND, new Tile(new Texture("res/tiles/forest_ground.png"), false, Tile.BLEND_TYPE_DOMINANT));
		tiles.put(Tile.DESERT_GROUND, new Tile(new Texture("res/tiles/desert_ground.png"), false, Tile.BLEND_TYPE_DOMINANT));
		
		tiles.put(Tile.CLIFF, new Tile(new Texture("res/tiles/cliff.png"), new TextureArray("res/tiles/blend/blend.png", 8), true, Tile.BLEND_TYPE_RECESSIVE));
		tiles.put(Tile.CLIFF_UP, new Tile(new Texture("res/tiles/cliff_up.png"), new TextureArray("res/tiles/blend/cliff_up.png", 1), new Vector2i(0, -1), true, Tile.BLEND_TYPE_RECESSIVE));
		tiles.put(Tile.CLIFF_DOWN, new Tile(new Texture("res/tiles/cliff_down.png"), new TextureArray("res/tiles/blend/cliff_down.png", 1), new Vector2i(0, 1), true, Tile.BLEND_TYPE_RECESSIVE));
		tiles.put(Tile.CLIFF_LEFT, new Tile(new Texture("res/tiles/cliff_left.png"), new TextureArray("res/tiles/blend/cliff_left.png", 1), new Vector2i(-1, 0), true, Tile.BLEND_TYPE_RECESSIVE));
		tiles.put(Tile.CLIFF_RIGHT, new Tile(new Texture("res/tiles/cliff_right.png"), new TextureArray("res/tiles/blend/cliff_right.png", 1), new Vector2i(1, 0), true, Tile.BLEND_TYPE_RECESSIVE));
		tiles.put(Tile.CLIFF_UPLEFT, new Tile(new Texture("res/tiles/cliff_upleft.png"), new TextureArray("res/tiles/blend/cliff_upleft.png", 1), new Vector2i(0, -1), true, Tile.BLEND_TYPE_RECESSIVE));
		tiles.put(Tile.CLIFF_UPRIGHT, new Tile(new Texture("res/tiles/cliff_upright.png"), new TextureArray("res/tiles/blend/cliff_upright.png", 1), new Vector2i(0, -1), true, Tile.BLEND_TYPE_RECESSIVE));
		tiles.put(Tile.CLIFF_DOWNLEFT, new Tile(new Texture("res/tiles/cliff_downleft.png"), new TextureArray("res/tiles/blend/cliff_downleft.png", 1), new Vector2i(0, 1), true, Tile.BLEND_TYPE_RECESSIVE));
		tiles.put(Tile.CLIFF_DOWNRIGHT, new Tile(new Texture("res/tiles/cliff_downright.png"), new TextureArray("res/tiles/blend/cliff_downright.png", 1), new Vector2i(0, 1), true, Tile.BLEND_TYPE_RECESSIVE));
		tiles.put(Tile.CLIFF_UPLEFT_IN, new Tile(new Texture("res/tiles/cliff_upleft_in.png"), new TextureArray("res/tiles/blend/cliff_upleft_in.png", 1), new Vector2i(-1, -1), true, Tile.BLEND_TYPE_RECESSIVE));
		tiles.put(Tile.CLIFF_UPRIGHT_IN, new Tile(new Texture("res/tiles/cliff_upright_in.png"), new TextureArray("res/tiles/blend/cliff_upright_in.png", 1), new Vector2i(1, -1), true, Tile.BLEND_TYPE_RECESSIVE));
		tiles.put(Tile.CLIFF_DOWNLEFT_IN, new Tile(new Texture("res/tiles/cliff_downleft_in.png"), new TextureArray("res/tiles/blend/cliff_downleft_in.png", 1), new Vector2i(-1, 1), true, Tile.BLEND_TYPE_RECESSIVE));
		tiles.put(Tile.CLIFF_DOWNRIGHT_IN, new Tile(new Texture("res/tiles/cliff_downright_in.png"), new TextureArray("res/tiles/blend/cliff_downright_in.png", 1), new Vector2i(1, 1), true, Tile.BLEND_TYPE_RECESSIVE));
		*/
		world = new World(32, 32, this);
		world.generate((int)(Math.random() * 10000));
		
		player = new Player(new Vector3f(0, 0, Renderer.ENTITY_LAYER), this);
		
		world.vehicles.add(new Mercury(new Vector3f(128, 256, Renderer.ENTITY_LAYER), Vehicle.DIRECTION_LEFT, this));
		
		gui = new UIManager(this);
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
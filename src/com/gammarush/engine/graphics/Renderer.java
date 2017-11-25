package com.gammarush.engine.graphics;

import static org.lwjgl.opengl.GL11.*;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.vehicles.Vehicle;
import com.gammarush.engine.gui.UIManager;
import com.gammarush.engine.gui.fonts.Font;
import com.gammarush.engine.math.matrix.Matrix4f;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.structures.Structure;
import com.gammarush.engine.tiles.Tile;

public class Renderer {
	
	private Game game;
	public Camera camera;
	public int width;
	public int height;
	
	public Font font = new Font();
	
	public static int screenWidth;
	public static int screenHeight;
	
	public static Matrix4f projectionMatrix;
	
	public static Shader DEFAULT;
	public static Shader FONT;
	public static Shader GUI;
	public static Shader MOB;
	public static Shader STRUCTURE;
	public static Shader TILE;
	public static Shader VEHICLE;
	
	public static final int TILE_LAYER = 0;
	public static final int ENTITY_LAYER = 1;
	public static final int STRUCTURE_LAYER = 1;
	public static final int GUI_LAYER = 7;
	
	public static final int SCALE = 4;
	
	public Renderer(int width, int height, Game game) {
		this.game = game;
		this.width = width;
		this.height = height;
		
		Renderer.screenWidth = (int) (game.width * game.scale);
		Renderer.screenHeight = (int) (game.height * game.scale);
		
		camera = new Camera(new Vector3f(0.0f, 0.0f, 0.0f), this);
		projectionMatrix = Matrix4f.orthographic(0.0f, this.width, this.height, 0.0f, Camera.MIN_DEPTH, Camera.MAX_DEPTH);
		
		loadShaders();
		setProjectionMatrix(projectionMatrix);
	}
	
	public static void loadShaders() {
		DEFAULT = new Shader("shaders/default.vert", "shaders/default.frag");
		DEFAULT.enable();
		DEFAULT.setUniform1i("sprite", Entity.TEXTURE_LOCATION);
		DEFAULT.setUniform1i("normal_map", Entity.NORMAL_MAP_LOCATION);
		DEFAULT.disable();
		
		FONT = new Shader("shaders/font.vert", "shaders/font.frag");
		FONT.enable();
		FONT.setUniformMat4f("pr_matrix", projectionMatrix);
		FONT.setUniform1i("sprite", Font.TEXTURE_LOCATION);
		FONT.disable();
		
		GUI = new Shader("shaders/gui.vert", "shaders/gui.frag");
		GUI.enable();
		GUI.setUniformMat4f("pr_matrix", projectionMatrix);
		GUI.setUniform1i("sprite", UIManager.TEXTURE_LOCATION);
		GUI.disable();
		
		MOB = new Shader("shaders/mob.vert", "shaders/mob.frag");
		MOB.enable();
		MOB.setUniform1i("sprite", Mob.TEXTURE_LOCATION);
		MOB.disable();
		
		STRUCTURE = new Shader("shaders/structure.vert", "shaders/structure.frag");
		STRUCTURE.enable();
		STRUCTURE.setUniform1i("sprite", Structure.TEXTURE_LOCATION);
		STRUCTURE.setUniform1i("normal_map", Structure.NORMAL_MAP_LOCATION);
		STRUCTURE.disable();
		
		TILE = new Shader("shaders/tile.vert", "shaders/tile.frag");
		TILE.enable();
		TILE.setUniform1i("sprite", Tile.TEXTURE_LOCATION);
		TILE.setUniform1i("normal_map", Tile.NORMAL_MAP_LOCATION);
		TILE.setUniform1i("blend_map", Tile.BLEND_MAP_MASK_LOCATION);
		TILE.setUniform1i("blend_sprite", Tile.BLEND_MAP_TEXTURE_LOCATION);
		TILE.disable();
		
		VEHICLE = new Shader("shaders/vehicle.vert", "shaders/vehicle.frag");
		VEHICLE.enable();
		VEHICLE.setUniform1i("sprite", Vehicle.TEXTURE_LOCATION);
		VEHICLE.disable();
	}
	
	public static void setProjectionMatrix(Matrix4f projectionMatrix) {
		Renderer.projectionMatrix = projectionMatrix;
		
		DEFAULT.enable();
		DEFAULT.setUniformMat4f("pr_matrix", projectionMatrix);
		DEFAULT.disable();
		
		MOB.enable();
		MOB.setUniformMat4f("pr_matrix", projectionMatrix);
		MOB.disable();
		
		STRUCTURE.enable();
		STRUCTURE.setUniformMat4f("pr_matrix", projectionMatrix);
		STRUCTURE.disable();
		
		TILE.enable();
		TILE.setUniformMat4f("pr_matrix", projectionMatrix);
		TILE.disable();
		
		VEHICLE.enable();
		VEHICLE.setUniformMat4f("pr_matrix", projectionMatrix);
		VEHICLE.disable();
	}
	
	public void render() {
		game.world.render(this);
		game.player.render();
		game.gui.render(this);
	}
	
	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	public void setClearColor(int color) {
		int r = (color & 0xff0000) >> 16;
	    int g = (color & 0xff00) >> 8;
	    int b = (color & 0xff);
		glClearColor(r, g, b, 1.0f);
	}

}

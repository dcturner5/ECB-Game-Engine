package com.gammarush.engine.graphics;

import static org.lwjgl.opengl.GL11.*;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.interactives.vehicles.Vehicle;
import com.gammarush.engine.gui.UIManager;
import com.gammarush.engine.gui.fonts.Font;
import com.gammarush.engine.lights.PointLight;
import com.gammarush.engine.math.matrix.Matrix4f;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.math.vector.Vector4f;
import com.gammarush.engine.tiles.Tile;

public class Renderer {
	
	public static int screenWidth;
	public static int screenHeight;
	
	public static Matrix4f projectionMatrix;
	
	public static Shader DEFAULT;
	public static Shader FONT;
	public static Shader GUI;
	public static Shader MOB;
	public static Shader TILE;
	public static Shader VEHICLE;
	
	public static final int TILE_LAYER = 0;
	public static final int ENTITY_LAYER = 1;
	public static final int STRUCTURE_LAYER = 1;
	public static final int GUI_LAYER = 7;
	
	public static final int SCALE = 4;
	
	private Game game;
	public Camera camera;
	public int width;
	public int height;
	public Font font = new Font();
	
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
		
		TILE.enable();
		TILE.setUniformMat4f("pr_matrix", projectionMatrix);
		TILE.disable();
		
		VEHICLE.enable();
		VEHICLE.setUniformMat4f("pr_matrix", projectionMatrix);
		VEHICLE.disable();
	}
	
	public void render() {
		loadShaderUniforms();
		game.world.render();
		game.gui.render();
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
	
	public void loadShaderUniforms() {
		Matrix4f viewMatrix = Matrix4f.translate(camera.position);
		
		DEFAULT.enable();
		DEFAULT.setUniformMat4f("vw_matrix", viewMatrix);
		DEFAULT.setUniform2f("resolution", new Vector2f(Renderer.screenWidth, Renderer.screenHeight));
		DEFAULT.setUniform4f("global_color", new Vector4f(game.world.global.color.x, game.world.global.color.y, game.world.global.color.z, game.world.global.intensity));
		DEFAULT.setUniform3f("global_direction", new Vector3f(game.world.global.direction.x, game.world.global.direction.y, game.world.global.direction.z));
		DEFAULT.setUniform4f("ambient_color", new Vector4f(game.world.ambient.color.x, game.world.ambient.color.y, game.world.ambient.color.z, game.world.ambient.intensity));
		for(int i = 0; i < game.world.lights.size(); i++) {
			PointLight light = game.world.lights.get(i);
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
		Renderer.TILE.setUniform4f("global_color", new Vector4f(game.world.global.color.x, game.world.global.color.y, game.world.global.color.z, game.world.global.intensity));
		Renderer.TILE.setUniform3f("global_direction", new Vector3f(game.world.global.direction.x, game.world.global.direction.y, game.world.global.direction.z));
		Renderer.TILE.setUniform4f("ambient_color", new Vector4f(game.world.ambient.color.x, game.world.ambient.color.y, game.world.ambient.color.z, game.world.ambient.intensity));
		for(int i = 0; i < game.world.lights.size(); i++) {
			PointLight light = game.world.lights.get(i);
			Renderer.TILE.setUniform3f("point_position[" + i + "]", light.position);
			Renderer.TILE.setUniform1f("point_radius[" + i + "]", light.radius);
			Renderer.TILE.setUniform4f("point_color[" + i + "]", new Vector4f(light.color.x, light.color.y, light.color.z, light.intensity));
		}
		Renderer.TILE.disable();
		
		Renderer.VEHICLE.enable();
		Renderer.VEHICLE.setUniformMat4f("vw_matrix", viewMatrix);
		Renderer.VEHICLE.disable();
	}

}

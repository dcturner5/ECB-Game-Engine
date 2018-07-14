package com.gammarush.engine.graphics;

import static org.lwjgl.opengl.GL11.*;

import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.entities.vehicles.Vehicle;
import com.gammarush.engine.gui.UIManager;
import com.gammarush.engine.gui.fonts.Font;
import com.gammarush.engine.math.matrix.Matrix4f;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.math.vector.Vector4f;
import com.gammarush.engine.tiles.Tile;
import com.gammarush.engine.world.World;
import com.gammarush.engine.world.WorldManager;

public class Renderer {
	
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
	
	private Camera camera;
	private int width;
	private int height;
	private int screenWidth;
	private int screenHeight;
	
	private UIManager uiManager;
	private WorldManager worldManager;
	
	public Renderer(int width, int height, UIManager uiManager, WorldManager worldManager) {
		this.width = width;
		this.height = height;
		
		this.uiManager = uiManager;
		this.worldManager = worldManager;
		this.worldManager.setRenderer(this);
		
		camera = new Camera(new Vector3f(0.0f, 0.0f, 0.0f), this);
		projectionMatrix = Matrix4f.orthographic(0.0f, this.width, this.height, 0.0f, Camera.MIN_DEPTH, Camera.MAX_DEPTH);
		
		loadShaders();
		setProjectionMatrix(projectionMatrix);
	}
	
	public Camera getCamera() {
		return camera;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getScreenWidth() {
		return screenWidth;
	}
	
	public int getScreenHeight() {
		return screenHeight;
	}
	
	public void setScreenSize(int width, int height) {
		this.screenWidth = width;
		this.screenHeight = height;
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
		worldManager.render();
		uiManager.render();
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
		World world = worldManager.getWorld();
		
		DEFAULT.enable();
		DEFAULT.setUniformMat4f("vw_matrix", viewMatrix);
		DEFAULT.setUniform2f("resolution", new Vector2f(screenWidth, screenHeight));
		DEFAULT.setUniform4f("global_color", new Vector4f(world.getGlobalLight().color.x, world.getGlobalLight().color.y, world.getGlobalLight().color.z, world.getGlobalLight().intensity));
		DEFAULT.setUniform3f("global_direction", new Vector3f(world.getGlobalLight().direction.x, world.getGlobalLight().direction.y, world.getGlobalLight().direction.z));
		DEFAULT.setUniform4f("ambient_color", new Vector4f(world.getAmbientLight().color.x, world.getAmbientLight().color.y, world.getAmbientLight().color.z, world.getAmbientLight().intensity));
		/*for(int i = 0; i < game.world.lights.size(); i++) {
			PointLight light = game.world.lights.get(i);
			DEFAULT.setUniform3f("point_position[" + i + "]", light.position);
			DEFAULT.setUniform1f("point_radius[" + i + "]", light.radius);
			DEFAULT.setUniform4f("point_color[" + i + "]", new Vector4f(light.color.x, light.color.y, light.color.z, light.intensity));
		}*/
		DEFAULT.disable();
		
		MOB.enable();
		MOB.setUniformMat4f("vw_matrix", viewMatrix);
		MOB.setUniform2f("resolution", new Vector2f(screenWidth, screenHeight));
		MOB.setUniform4f("global_color", new Vector4f(world.getGlobalLight().color.x, world.getGlobalLight().color.y, world.getGlobalLight().color.z, world.getGlobalLight().intensity));
		MOB.setUniform3f("global_direction", new Vector3f(world.getGlobalLight().direction.x, world.getGlobalLight().direction.y, world.getGlobalLight().direction.z));
		MOB.setUniform4f("ambient_color", new Vector4f(world.getAmbientLight().color.x, world.getAmbientLight().color.y, world.getAmbientLight().color.z, world.getAmbientLight().intensity));
		/*for(int i = 0; i < game.world.lights.size(); i++) {
			PointLight light = game.world.lights.get(i);
			MOB.setUniform3f("point_position[" + i + "]", light.position);
			MOB.setUniform1f("point_radius[" + i + "]", light.radius);
			MOB.setUniform4f("point_color[" + i + "]", new Vector4f(light.color.x, light.color.y, light.color.z, light.intensity));
		}*/
		MOB.disable();
		
		TILE.enable();
		TILE.setUniformMat4f("vw_matrix", viewMatrix);
		TILE.setUniform2f("resolution", new Vector2f(screenWidth, screenHeight));
		TILE.setUniform4f("global_color", new Vector4f(world.getGlobalLight().color.x, world.getGlobalLight().color.y, world.getGlobalLight().color.z, world.getGlobalLight().intensity));
		TILE.setUniform3f("global_direction", new Vector3f(world.getGlobalLight().direction.x, world.getGlobalLight().direction.y, world.getGlobalLight().direction.z));
		TILE.setUniform4f("ambient_color", new Vector4f(world.getAmbientLight().color.x, world.getAmbientLight().color.y, world.getAmbientLight().color.z, world.getAmbientLight().intensity));
		/*for(int i = 0; i < game.world.lights.size(); i++) {
			PointLight light = game.world.lights.get(i);
			TILE.setUniform3f("point_position[" + i + "]", light.position);
			TILE.setUniform1f("point_radius[" + i + "]", light.radius);
			TILE.setUniform4f("point_color[" + i + "]", new Vector4f(light.color.x, light.color.y, light.color.z, light.intensity));
		}*/
		TILE.disable();
		
		VEHICLE.enable();
		VEHICLE.setUniformMat4f("vw_matrix", viewMatrix);
		VEHICLE.setUniform2f("resolution", new Vector2f(screenWidth, screenHeight));
		VEHICLE.setUniform4f("global_color", new Vector4f(world.getGlobalLight().color.x, world.getGlobalLight().color.y, world.getGlobalLight().color.z, world.getGlobalLight().intensity));
		VEHICLE.setUniform3f("global_direction", new Vector3f(world.getGlobalLight().direction.x, world.getGlobalLight().direction.y, world.getGlobalLight().direction.z));
		VEHICLE.setUniform4f("ambient_color", new Vector4f(world.getAmbientLight().color.x, world.getAmbientLight().color.y, world.getAmbientLight().color.z, world.getAmbientLight().intensity));
		/*for(int i = 0; i < game.world.lights.size(); i++) {
			PointLight light = game.world.lights.get(i);
			VEHICLE.setUniform3f("point_position[" + i + "]", light.position);
			VEHICLE.setUniform1f("point_radius[" + i + "]", light.radius);
			VEHICLE.setUniform4f("point_color[" + i + "]", new Vector4f(light.color.x, light.color.y, light.color.z, light.intensity));
		}*/
		VEHICLE.disable();
	}

}

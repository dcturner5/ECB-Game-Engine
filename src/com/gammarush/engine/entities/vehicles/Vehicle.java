package com.gammarush.engine.entities.vehicles;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

import java.util.ArrayList;
import java.util.Arrays;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.Entity;
import com.gammarush.engine.entities.interactives.static_vehicles.StaticMercury;
import com.gammarush.engine.entities.mobs.Human;
import com.gammarush.engine.entities.mobs.Mob;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.input.KeyCallback;
import com.gammarush.engine.math.matrix.Matrix4f;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.physics.Physics;
import com.gammarush.engine.tiles.Tile;

public class Vehicle extends Entity {
	
	public Model interiorModel;
	protected ArrayList<Mob> mobs = new ArrayList<Mob>();
	public ArrayList<Vector2f> mobPositions = new ArrayList<Vector2f>(Arrays.asList(
			null, new Vector2f(0, 1),
			new Vector2f(0, -3), null,
			new Vector2f(1, 0), new Vector2f(14, 0),
			new Vector2f(-1, 0), new Vector2f(-14, 0)
			));
	protected int occupancy = 2;
	
	public Physics physics;
	public float speed = 8;
	public int direction = 2;
	
	public boolean moving = true;
	
	protected int animationIndex = 0;
	protected int animationFrame = 0;
	protected int animationMaxFrame = 8;
	protected int animationWidth = 1;

	public Vehicle(Vector3f position, int width, int height, Model model, Model interiorModel, Game game) {
		super(position, width, height, model, game);
		this.interiorModel = interiorModel;
		physics = new Physics(width, height, game.world);
	}
	
	@Override
	public void update(double delta) {
		
	}
	
	@Override
	public void render() {
		//CAR
		model.getMesh().bind();
		model.getTexture().bind(TEXTURE_LOCATION);
		model.draw();
		model.getMesh().unbind();
		model.getTexture().unbind(TEXTURE_LOCATION);
		//INTERIOR
		prepareInterior();
		interiorModel.getMesh().bind();
		interiorModel.getTexture().bind(TEXTURE_LOCATION);
		interiorModel.draw();
		interiorModel.getMesh().unbind();
		interiorModel.getTexture().unbind(TEXTURE_LOCATION);
		//MOBS
		for(int i = 0; i < mobs.size(); i++) {
			Mob e = mobs.get(i);
			Vector2f position = mobPositions.get(i + direction * occupancy);
			if(position == null) continue;
			
			prepareMob(e, position);
			e.model.getMesh().bind();
			e.model.getTexture().bind(TEXTURE_LOCATION);
			e.model.draw();
			e.model.getMesh().unbind();
			e.model.getTexture().unbind(TEXTURE_LOCATION);
		}
	}
	
	@Override
	public void prepare() {
		Renderer.VEHICLE.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(rotation).add(new Vector3f(width / 2, height / 2, 0)))
				.multiply(Matrix4f.scale(new Vector3f(width / model.WIDTH, height / model.HEIGHT, 0))));
		Renderer.VEHICLE.setUniform1i("sprite_index", animationIndex + direction * animationWidth);
	}
	
	public void prepareInterior() {
		Renderer.VEHICLE.setUniformMat4f("ml_matrix", Matrix4f.translate(position.add(0, 0, -.0002f)).multiply(Matrix4f.rotate(rotation).add(new Vector3f(width / 2, height / 2, 0)))
				.multiply(Matrix4f.scale(new Vector3f(width / model.WIDTH, height / model.HEIGHT, 0))));
		Renderer.VEHICLE.setUniform1i("sprite_index", animationIndex + direction * animationWidth);
	}
	
	public void prepareMob(Mob mob, Vector2f offset) {
		Renderer.VEHICLE.setUniformMat4f("ml_matrix", 
				Matrix4f.translate(position.add(offset.x * Renderer.SCALE, offset.y * Renderer.SCALE, -.0001f))
				.multiply(Matrix4f.rotate(rotation).add(new Vector3f(width / 2, height / 2, 0)))
				.multiply(Matrix4f.scale(new Vector3f(mob.width / mob.model.WIDTH, mob.height / mob.model.HEIGHT, 0))));
		Renderer.VEHICLE.setUniform1i("sprite_index", direction * mob.animationWidth);
	}
	
	public void control() {
		Vector2f initial = new Vector2f(velocity);
		boolean w = KeyCallback.isKeyDown(GLFW_KEY_W);
		boolean s = KeyCallback.isKeyDown(GLFW_KEY_S);
		boolean a = KeyCallback.isKeyDown(GLFW_KEY_A);
		boolean d = KeyCallback.isKeyDown(GLFW_KEY_D);
		
		if(w) {
			velocity.y -= speed;
			direction = Mob.DIRECTION_UP;
		}
		if(s) {
			velocity.y += speed;
			direction = DIRECTION_DOWN;
		}
		if(a && !(w || s)) {
			velocity.x -= speed;
			direction = DIRECTION_LEFT;
		}
		if(d && !(w || s)) {
			velocity.x += speed;
			direction = DIRECTION_RIGHT;
		}
		if(KeyCallback.isKeyDown(GLFW_KEY_SPACE)) {
			removeMob(game.player.getMob());
		}
		
		if(velocity.x != 0 || velocity.y != 0) moving = true;
		else moving = false;
		
		Vector2f position2D = new Vector2f(position.x, position.y);
		position2D = position2D.add(velocity);
		
		Vector2f translation = physics.collision(position2D);
		position.z = Renderer.ENTITY_LAYER + (position.y / Tile.HEIGHT) / game.world.height;
		
		position2D = position2D.add(translation);
		
		position.x = position2D.x;
		position.y = position2D.y;
		
		velocity = initial;
	}
	
	public boolean addMob(Mob mob) {
		if(mobs.size() < occupancy) {
			mobs.add(mob);
			mobs.add(new Human(position, game));
			return true;
		}
		return false;
	}
	
	public boolean removeMob(Mob mob) {
		if(mobs.remove(mob)) {
			if(mobs.size() == 0) {
				game.world.interactives.add(new StaticMercury(position, direction, game));
				game.world.vehicles.remove(this);
			}
		}
		return false;
	}
	
	public boolean isDriving(Mob mob) {
		if(mobs.size() == 0) return false;
		return mobs.get(0).equals(mob);
	}
	
	public boolean isRiding(Mob mob) {
		return mobs.contains(mob);
	}

}

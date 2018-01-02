package com.gammarush.engine.entities.interactives.vehicles;

import java.util.ArrayList;

import com.gammarush.engine.entities.EntityTemplate;
import com.gammarush.engine.entities.animations.Animation;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.graphics.model.Model;
import com.gammarush.engine.graphics.model.Texture;
import com.gammarush.engine.graphics.model.TextureArray;
import com.gammarush.engine.math.matrix.Matrix4f;
import com.gammarush.engine.math.vector.Vector2f;
import com.gammarush.engine.math.vector.Vector3f;
import com.gammarush.engine.utils.json.JSON;

public class VehicleTemplate extends EntityTemplate {
	
	private int width;
	private int height;
	private float acceleration;
	private int occupancy;
	private int wheelSize;
	private boolean wheelExposed;
	
	private Model exterior;
	private Model interior;
	
	private ArrayList<Vector2f> mobPositions = new ArrayList<Vector2f>();
	private ArrayList<Vector2f> wheelPositions = new ArrayList<Vector2f>();
	
	public VehicleTemplate(int id, JSON json) {
		super(id, json);
		
		this.width = json.getInteger("width") * Renderer.SCALE;
		this.height = json.getInteger("height") * Renderer.SCALE;
		this.acceleration = json.getFloat("acceleration");
		
		ArrayList<JSON> mobPositions = json.getArray("mobs.positions");
		for(JSON position : mobPositions) {
			if(position == null) {
				this.mobPositions.add(null);
			}
			else {
				this.mobPositions.add(new Vector2f(position.getFloat("x") * Renderer.SCALE, position.getFloat("y") * Renderer.SCALE));
			}
		}
		
		this.occupancy = this.mobPositions.size() / 4;
		
		Texture exteriorTexture = new TextureArray("res/entities/interactives/vehicles/" + json.getString("name") + "/exterior.png", 8);
		this.exterior = new Model(exteriorTexture);
		Texture interiorTexture = new TextureArray("res/entities/interactives/vehicles/" + json.getString("name") + "/interior.png", 4);
		this.interior = new Model(interiorTexture);
		
		this.wheelSize = json.getInteger("wheel.size") * Renderer.SCALE;
		this.wheelExposed = json.getBoolean("wheel.exposed");
		
		ArrayList<JSON> wheelPositions = json.getArray("wheel.positions");
		for(JSON position : wheelPositions) {
			if(position == null) {
				this.wheelPositions.add(null);
			}
			else {
				this.wheelPositions.add(new Vector2f(position.getFloat("x") * Renderer.SCALE - wheelSize / 2, position.getFloat("y") * Renderer.SCALE - wheelSize / 2));
			}
		}
	}
	
	public void render(VehicleBatch batch) {
		exterior.getMesh().bind();
		exterior.getTexture().bind(Vehicle.TEXTURE_LOCATION);
		for(int i = 0; i < batch.positions.size(); i++) {
			prepareExterior(batch.positions.get(i), batch.animations.get(i));
			exterior.draw();
		}
		exterior.getMesh().unbind();
		exterior.getTexture().unbind(Vehicle.TEXTURE_LOCATION);
		
		interior.getMesh().bind();
		interior.getTexture().bind(Vehicle.TEXTURE_LOCATION);
		for(int i = 0; i < batch.positions.size(); i++) {
			prepareInterior(batch.positions.get(i), batch.animations.get(i));
			interior.draw();
		}
		interior.getMesh().unbind();
		interior.getTexture().unbind(Vehicle.TEXTURE_LOCATION);
	}
	
	public void prepareExterior(Vector3f position, Animation animation) {
		Renderer.VEHICLE.setUniformMat4f("ml_matrix", Matrix4f.translate(position).multiply(Matrix4f.rotate(0).add(new Vector3f(width / 2, height / 2, 0)))
				.multiply(Matrix4f.scale(new Vector3f(width, height, 0))));
		Renderer.VEHICLE.setUniform1i("sprite_index", animation.getIndex());
	}
	
	public void prepareInterior(Vector3f position, Animation animation) {
		Renderer.VEHICLE.setUniformMat4f("ml_matrix", Matrix4f.translate(position.add(0, 0, -.0002f)).multiply(Matrix4f.rotate(0).add(new Vector3f(width / 2, height / 2, 0)))
				.multiply(Matrix4f.scale(new Vector3f(width, height, 0))));
		Renderer.VEHICLE.setUniform1i("sprite_index", animation.getDirection());
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public float getAcceleration() {
		return acceleration;
	}
	
	public int getOccupancy() {
		return occupancy;
	}
	
	public Model getModel() {
		return exterior;
	}
	
	public ArrayList<Vector2f> getMobPositions() {
		return mobPositions;
	}

	public ArrayList<Vector2f> getWheelPositions() {
		return wheelPositions;
	}

	public int getWheelSize() {
		return wheelSize;
	}

	public boolean getWheelExposed() {
		return wheelExposed;
	}

}

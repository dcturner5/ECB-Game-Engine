package com.gammarush.engine.entities.interactives.vehicles;

import java.util.ArrayList;

import com.gammarush.engine.Game;
import com.gammarush.engine.entities.components.AnimationComponent;
import com.gammarush.engine.graphics.Renderer;
import com.gammarush.engine.math.vector.Vector2f;

public class VehicleBatchManager {
	
	public ArrayList<VehicleBatch> batches = new ArrayList<VehicleBatch>();
	public ArrayList<WheelBatch> wheelBatches = new ArrayList<WheelBatch>();
	
	public void add(Vehicle vehicle) {
		VehicleBatch batch = null;
		boolean exists = false;
		for(VehicleBatch b : batches) {
			if(b.getId() == vehicle.getTemplate().getId()) {
				batch = b;
				exists = true;
			}
		}
		if(!exists) {
			batch = new VehicleBatch(vehicle.getTemplate().getId());
			batches.add(batch);
		}
		
		AnimationComponent ac = (AnimationComponent) vehicle.getComponent("animation");
		batch.add(vehicle.position, ac.getAnimation());
		
		if(ac.getAnimation().getDirection() == Vehicle.DIRECTION_LEFT || ac.getAnimation().getDirection() == Vehicle.DIRECTION_RIGHT) {
			WheelBatch wheelBatch = null;
			boolean wheelExists = false;
			for(WheelBatch b : wheelBatches) {
				if(b.getId() == vehicle.getWheelTemplate().getId()) {
					wheelBatch = b;
					wheelExists = true;
				}
			}
			if(!wheelExists) {
				wheelBatch = new WheelBatch(vehicle.getWheelTemplate().getId());
				wheelBatches.add(wheelBatch);
			}
			
			ArrayList<Vector2f> wheelPositions = vehicle.getTemplate().getWheelPositions();
			int start = ac.getAnimation().getDirection() == Vehicle.DIRECTION_LEFT ? 0 : wheelPositions.size() / 2;
			int end = ac.getAnimation().getDirection() == Vehicle.DIRECTION_LEFT ? wheelPositions.size() / 2 : wheelPositions.size();
			for(int i = start; i < end; i++) {
				Vector2f position = wheelPositions.get(i);
				wheelBatch.add(vehicle.position.add(position.x, position.y, vehicle.getTemplate().getWheelExposed() ? .0001f : -.0001f), vehicle.getTemplate().getWheelSize(), vehicle.wheelRotation);
			}
		}
	}
	
	public void process(ArrayList<Vehicle> vehicles) {
		for(Vehicle e : vehicles) {
			if(!e.getScreenPresence()) continue;
			add(e);
		}
	}
	
	public void render() {
		for(VehicleBatch b : batches) {
			VehicleTemplate t = Game.vehicles.get(b.getId());
			t.render(b);
		}
		batches.clear();
		
		Renderer.DEFAULT.enable();
		for(WheelBatch b : wheelBatches) {
			WheelTemplate t = Game.vehicles.getWheel(b.getId());
			t.render(b);
		}
		wheelBatches.clear();
		Renderer.DEFAULT.disable();
	}

}

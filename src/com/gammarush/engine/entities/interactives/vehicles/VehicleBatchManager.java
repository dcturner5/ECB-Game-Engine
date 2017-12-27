package com.gammarush.engine.entities.interactives.vehicles;

import java.util.ArrayList;

import com.gammarush.engine.Game;
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
		batch.add(vehicle.position, vehicle.animation);
		
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
		for(Vector2f position : wheelPositions) {
			wheelBatch.add(vehicle.position.add(position.x, position.y, .00001f), vehicle.getTemplate().getWheelSize(), vehicle.wheelRotation);
		}
	}
	
	public void render() {
		for(VehicleBatch b : batches) {
			VehicleTemplate t = Game.vehicles.get(b.getId());
			t.render(b);
		}
		
		batches.clear();
	}
	
	public void renderWheels() {
		for(WheelBatch b : wheelBatches) {
			WheelTemplate t = Game.vehicles.getWheel(b.getId());
			t.render(b);
		}
		wheelBatches.clear();
	}

}

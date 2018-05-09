package com.gammarush.engine.entities.animations;

import java.util.ArrayList;

import com.gammarush.engine.utils.json.JSON;

public class Animation {
	
	private String name;
	private boolean loop;
	private int maxFrame;
	private ArrayList<ArrayList<Integer>> indices;
	
	private int index = 0;
	private int arrayIndex = 0;
	private int frame = 0;
	private int direction = 0;
	private boolean running = false;
	
	public Animation(JSON json) {
		this.name = json.getString("name");
		this.loop = json.getBoolean("loop");
		this.maxFrame = json.getInteger("maxFrame");
		this.indices = json.getInteger2DArray("indices");
	}
	
	public void update(double delta) {
		if(running) {
			if(frame < maxFrame) {
                frame += 1;
            } else {
                frame = 0;
                if(arrayIndex < indices.get(direction).size() - 1) {
                    arrayIndex += 1;
                } else {
                    arrayIndex = 0;
                    if(!loop) stop();
                }
                index = indices.get(direction).get(arrayIndex);
            }
		} else {
			frame = 0;
            index = indices.get(direction).get(0);
		}
	}
	
	public void start() {
		if(!running) {
			running = true;
			frame = 0;
			index = indices.get(direction).get(0);
		}
	}
	
	public void stop() {
		running = false;
	}
	
	public int getDirection() {
		return direction;
	}
	
	public int getIndex() {
		return index;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public void setDirection(int direction) {
		if(this.direction == direction) return;
		
		this.direction = direction;
		arrayIndex = 0;
		index = indices.get(direction).get(arrayIndex);
	}
	
	public void setMaxFrame(int maxFrame) {
		this.maxFrame = maxFrame;
	}
	
}

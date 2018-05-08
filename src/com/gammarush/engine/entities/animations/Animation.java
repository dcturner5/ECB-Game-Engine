package com.gammarush.engine.entities.animations;

public class Animation {
	
	private String name;
	private boolean loop;
	private int startIndex;
	private int endIndex;
	private int width;
	private int maxFrame;
	
	private int index = 0;
	private int frame = 0;
	private int direction = 0;
	private boolean running = false;
	
	public Animation(String name, boolean loop, int startIndex, int endIndex, int maxFrame) {
		this.name = name;
		this.loop = loop;
		this.startIndex = startIndex;
		this.width = (endIndex - startIndex) / 4;
		this.endIndex = startIndex + width;
		this.maxFrame = maxFrame;
	}
	
	public void update(double delta) {
		if(running) {
			if(frame < maxFrame) {
                frame += 1;
            } else {
                frame = 0;
                if(index < endIndex - 1) {
                    index += 1;
                } else {
                    index = startIndex;
                    if(!loop) {
                    	stop();
                    }
                }
            }
		} else {
			frame = 0;
            index = startIndex;
		}
	}
	
	public void start() {
		running = true;
	}
	
	public void stop() {
		running = false;
	}
	
	public int getDirection() {
		return direction;
	}
	
	public int getIndex() {
		return index + direction * width;
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	public void setMaxFrame(int maxFrame) {
		this.maxFrame = maxFrame;
	}
	
}

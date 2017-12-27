package com.gammarush.engine.entities.mobs.animations;

public class AnimationData {
	
	private int index = 0;
	private int frame = 0;
	private int maxFrame;
	private int width;
	private int direction = 2;
	
	public AnimationData(int width, int maxFrame) {
		this.width = width;
		this.maxFrame = maxFrame;
	}
	
	public void update(boolean moving) {
		if(moving) {
			if(frame < maxFrame) {
                frame += 1;
            } else {
                frame = 0;
                if(index < width - 1) {
                    index += 1;
                } else {
                    index = 0;
                }
            }
		} else {
			frame = 0;
            index = 0;
		}
	}
	
	public int getIndex() {
		return index + direction * width;
	}
	
	public int getDirection() {
		return direction;
	}
	
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	public void setMaxFrame(int maxFrame) {
		this.maxFrame = maxFrame;
	}
	
}

package com.gammarush.engine.utils;

public class Timer {
	
	private int index;
	private int cooldown;
	
	public Timer(int cooldown) {
		this.index = 0;
		this.cooldown = cooldown;
	}
	
	public Timer(int cooldown, boolean ready) {
		this.index = ready ? cooldown : 0;
		this.cooldown = cooldown;
	}
	
	public void update() {
		if(index < cooldown) index++;
	}
	
	public void reset() {
		index = 0;
	}
	
	public void forceReady() {
		index = cooldown;
	}
	
	public boolean isReady() {
		return index >= cooldown;
	}
	
}

package com.gammarush.engine.gui.event;

//ENUM CLASS FOR EVENT TYPES

public enum EventType {
	INVALID(-1), LEFTCLICK(0), RIGHTCLICK(1), LEFTRELEASE(2), RIGHTRELEASE(3), HOVERENTER(4), HOVEREXIT(5), KEYINPUT(6);
	public int id;
	private EventType(int id) {
		this.id = id;
	}
	
	public static EventType get(int id) {
		for(EventType event : EventType.values()) {
			if(event.id == id) return event;
		}
		return INVALID;
	}
}
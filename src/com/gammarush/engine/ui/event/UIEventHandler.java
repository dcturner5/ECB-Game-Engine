package com.gammarush.engine.ui.event;

//ABSTRACT CLASS USED TO ROUTE UIEVENTS TO GUI COMPONENTS

public abstract class UIEventHandler {
	public void activate(int type) {
		EventType event = EventType.get(type);
		switch(event) {
			case INVALID:
				break;
			case LEFTCLICK:
				leftClick();
				break;
			case RIGHTCLICK:
				rightClick();
				break;
			case LEFTRELEASE:
				leftRelease();
				break;
			case RIGHTRELEASE:
				rightRelease();
				break;
			case HOVERENTER:
				hoverEnter();
				break;
			case HOVEREXIT:
				hoverExit();
				break;
			case KEYINPUT:
				keyInput(0);
				break;
		}
	}
	public abstract void leftClick();
	public abstract void rightClick();
	public abstract void leftRelease();
	public abstract void rightRelease();
	public abstract void hoverEnter();
	public abstract void hoverExit();
	public abstract void keyInput(int key);
}
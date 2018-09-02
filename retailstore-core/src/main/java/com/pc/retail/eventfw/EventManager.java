package com.pc.retail.eventfw;

public interface EventManager {

	public void registerEventListener(EventType event, EventListener eventListener);
	
	public void fireEvent(Event event);
}

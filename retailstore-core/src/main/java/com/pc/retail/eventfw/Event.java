package com.pc.retail.eventfw;

public class Event {

	private EventType eventType;
	private Object valueObject;
	
	public Event(EventType eventType, Object valueObject){
		this.eventType = eventType;
		this.valueObject = valueObject;
	}

	public EventType getEventType() {
		return eventType;
	}

	public Object getValueObject() {
		return valueObject;
	}
	
	
}

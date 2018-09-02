package com.pc.retail.eventfw;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EventManagerImpl implements EventManager {

	public Map<EventType, List<EventListener>> eventMap;
	
	
	private static EventManagerImpl eventManager;
	private static volatile boolean initialized = false;
	
	private EventManagerImpl(){
		
	}
	
	public static EventManagerImpl getInstance(){
		if(initialized == false){
			synchronized(EventManagerImpl.class){
				if(!initialized){
					initialized = initialize();
				}
				return eventManager;
			}
		}else{
			return eventManager;
		}
	}
	
	private static boolean initialize(){
		eventManager = new EventManagerImpl();
		return true;
	}
	
	@Override
	public void fireEvent(Event event) {
		// TODO Auto-generated method stub
		List<EventListener> eventListenerList = eventMap.get(event.getEventType());
		if(eventListenerList != null && eventListenerList.size() > 0){
			for(Iterator<EventListener>  iter = eventListenerList.iterator(); iter.hasNext();){
				EventListener eListener = iter.next();
				eListener.onEvent(event.getValueObject());
			}
		}

	}

	@Override
	public synchronized void registerEventListener(EventType eventType, EventListener eventListener) {
		List<EventListener> eventListenerList = eventMap.get(eventType);
		if(eventListenerList == null){
			eventListenerList = new ArrayList<EventListener>();
			eventMap.put(eventType, eventListenerList);
		}
		eventListenerList.add(eventListener);
	}

}

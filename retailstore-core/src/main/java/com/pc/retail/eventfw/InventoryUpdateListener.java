package com.pc.retail.eventfw;

import com.pc.retail.cache.InventoryCache;

public class InventoryUpdateListener implements EventListener{

	private InventoryCache invCache;
	
	public InventoryUpdateListener(InventoryCache invCache){
		this.invCache = invCache;
	}
	@Override
	public void onEvent(Object eventObject) {
		String productId = (String)eventObject;
		//invCache.refreshCache(productId);
		
	}

	
}

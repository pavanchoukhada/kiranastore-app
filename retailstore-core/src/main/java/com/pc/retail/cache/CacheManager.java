package com.pc.retail.cache;

import java.util.HashMap;
import java.util.Map;

public class CacheManager {

	private Map<String, Cache> cacheStore = new HashMap<String, Cache>();
	private static CacheManager cacheManager = new CacheManager();

	private CacheManager(){

    }
	
	public Cache getCache(String cacheKey){
		return cacheStore.get(cacheKey);
	}

	public static  CacheManager getInstance(){
	    return cacheManager;
    }
}

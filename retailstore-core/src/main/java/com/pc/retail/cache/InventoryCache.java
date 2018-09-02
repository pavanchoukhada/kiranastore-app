package com.pc.retail.cache;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.pc.retail.dao.DataAccessException;
import com.pc.retail.dao.ProductInvDAO;
import com.pc.retail.eventfw.EventManagerImpl;
import com.pc.retail.eventfw.EventType;
import com.pc.retail.eventfw.InventoryUpdateListener;
import com.pc.retail.vo.Product;
import com.pc.retail.vo.ProductCurrentInvDetail;
import com.pc.retail.vo.ProductInventory;

public class InventoryCache {

    public static final String CACHE_NAME = "InventoryCache";
    private static InventoryCache invCache;
	private static volatile boolean initialized = false;
	private Map<String, ProductCurrentInvDetail> productMap;
	private static String CACHE_KEY = "barcode";
	private static ProductInvDAO invDAO;
	
	private InventoryCache(){
		productMap = new ConcurrentHashMap<String, ProductCurrentInvDetail>();
	}
	
	public static InventoryCache getInstance() throws DataAccessException{
		if(initialized == false){
			synchronized(InventoryCache.class){
				if(!initialized){
					initialized = initialize();
				}
				return invCache;
			}
		}else{
			return invCache;
		}
	}
	
	private static boolean initialize() throws DataAccessException{
		invCache = new InventoryCache();
		
		//invDAO = DAOFactory.getInventoryDAO();
		poulateCache(invCache);
		EventManagerImpl.getInstance().
			registerEventListener(EventType.UPDATE_INVENTORY, 
								new InventoryUpdateListener(invCache));
		return true;
	}
	
	public static void poulateCache(InventoryCache invCacheV) throws DataAccessException{
		List<ProductCurrentInvDetail> invDtlList = invDAO.getProductInvDtlList();
		for(Iterator<ProductCurrentInvDetail> iter = invDtlList.iterator(); iter.hasNext();){
			ProductCurrentInvDetail invDtl = iter.next();
			invCacheV.productMap.put(invDtl.getBarCode(), invDtl);
		}
		
	}
	
	public void refreshCache(String productId) throws DataAccessException{
		Product prd = invDAO.getProduct(productId);
	}
	
	public void refreshCache(){
		
	}

	public void updateInventory(ProductInventory prdInvEntry){
		//ProductInvDAO.getProductInvEntry(prdInvEntry.get, barCode)
	}
	

	public String getKey(){
		return InventoryCache.CACHE_KEY;
	}
	
}

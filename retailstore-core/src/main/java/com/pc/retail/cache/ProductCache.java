package com.pc.retail.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.pc.retail.dao.DataAccessException;
import com.pc.retail.dao.ProductInvDAO;
import com.pc.retail.dao.DataSourceManager;
import com.pc.retail.interactor.ProductFilter;
import com.pc.retail.vo.Product;
import com.pc.retail.vo.ProductCurrentInvDetail;


public class ProductCache extends AbstractCache{

	private static ProductCache prdCache;
	private static volatile boolean initialized = false;
	private Map<String, Product> productMap;
    private Map<Integer, Product> productIdProductMap;

	private List<Product> prdList;
	private ProductCache(){
		productMap = new ConcurrentHashMap<>();
        productIdProductMap  = new ConcurrentHashMap<>();
		prdList = new ArrayList<>();
	}
	
	public static ProductCache getInstance() throws DataAccessException{
		if(initialized == false){
			synchronized(ProductCache.class){
				if(!initialized){
					initialized = initialize();
				}
				return prdCache;
			}
		}else{
			return prdCache;
		}
	}
	
	private static boolean initialize() throws DataAccessException{
		prdCache = new ProductCache();
		prdCache.poulateCache();
		return true;
	}
	
	public void poulateCache() throws DataAccessException{
		prdList = getDataSource().getAllProducts();
		List<ProductCurrentInvDetail> productCurrentInvDetailList = getDataSource().getAllCurrentInventoryDetail();
		for(Iterator<Product> iter = prdList.iterator(); iter.hasNext();){
			Product prd = iter.next();
			productMap.put(prd.getBarcode(), prd);
            productIdProductMap.put(prd.getProductId(), prd);
		}
		for(ProductCurrentInvDetail productCurrentInvDetail : productCurrentInvDetailList) {
            Product product = productMap.get(productCurrentInvDetail.getBarCode());
            product.setProductCurrentInvDetail(productCurrentInvDetail);
        }
	}

    private ProductInvDAO getDataSource() throws DataAccessException{
        try {
            return DataSourceManager.getInstance().getProductInvDAO();
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    public void refreshCache(String barCode) throws DataAccessException{
		try{
			getWriteLock().lock();
			Product prd = getDataSource().getProduct(barCode);
			productMap.put(prd.getBarcode(), prd);
		}finally{
			getWriteLock().unlock();
		}
	}

	public void updateCache(Product product){
		try{
			getWriteLock().lock();
			productMap.put(product.getBarcode(), product);
            productIdProductMap.put(product.getProductId(), product);
		}finally{
			getWriteLock().unlock();
		}
	}
	
	

	public Map<String, String> productLookupQuery(String searchQuery){
		Map<String, String> resultMap = new HashMap<String, String>();
		for(Iterator<Product> iter = prdList.iterator(); iter.hasNext();){
			Product prd = iter.next();
			if(prd.getSearchKey().contains(searchQuery)){ //make it regular expression
				resultMap.put(prd.getBarcode(), prd.getPrdDesc());
			}
		}
		return resultMap;
	}

    public Product getProductFromBarCode(String barCode) {
        return productMap.get(barCode);
    }

    public Product getProduct(ProductFilter productFilter) {
	    if(productFilter.getBarCode() != null){
            return productMap.get(productFilter.getBarCode());
        }else{
            return productIdProductMap.get(productFilter.getProductId());
        }
    }

    public List<Product> getAllProducts() {
	    return new ArrayList<>(productMap.values());
    }
}

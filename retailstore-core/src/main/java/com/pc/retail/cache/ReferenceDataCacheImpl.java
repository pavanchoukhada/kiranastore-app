package com.pc.retail.cache;

import com.pc.retail.dao.DataAccessException;
import com.pc.retail.dao.DataSourceManager;
import com.pc.retail.dao.ReferenceDataDAO;
import com.pc.retail.dao.ReferenceDataDAOImpl;
import com.pc.retail.vo.ProductUOM;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pavanc on 6/11/17.
 */
public class ReferenceDataCacheImpl {

    private Map cacheMap = new HashMap();
    public static final String PRODUCT_CATEGORY_CACHE = "product_category";
    public static final String PRODUCT_UOM_CACHE = "product_uom";
    public static final String PRODUCT_COMPANY_CACHE = "product_company";

    private static ReferenceDataCacheImpl referenceDataCache = new ReferenceDataCacheImpl();

    private ReferenceDataCacheImpl(){

    }

    public static ReferenceDataCacheImpl getInstance(){
        return referenceDataCache;
    }

    public void put(String cacheName, Map cache){
        cacheMap.put( cacheName, cache);
    }

    public Map<String, ?> get(String cacheName){
        return (Map)cacheMap.get( cacheName);
    }

    public List<String> getList(String cacheName) {
        return (List)cacheMap.get(cacheName);
    }

    public Map<String,ProductUOM> getUOMMap() {
        Map<String, ProductUOM> uomMap = (Map<String, ProductUOM>)cacheMap.get(PRODUCT_UOM_CACHE);
        if(uomMap == null){
            try {
                uomMap = initializeUOMMap();
            } catch (DataAccessException e) {
                e.printStackTrace();
            }
            cacheMap.put(PRODUCT_UOM_CACHE, uomMap);
        }
        return uomMap;
    }

    private Map<String, ProductUOM> initializeUOMMap() throws DataAccessException {
        ReferenceDataDAO referenceDataDAO = DataSourceManager.getInstance().getReferenceDataDAO();
        Map<String, ProductUOM> productUOMMap = new HashMap<>();
        List<ProductUOM> productUOMList = referenceDataDAO.getProductUOMList();
        for(ProductUOM productUOM : productUOMList){
            productUOMMap.put(productUOM.getUomCode(), productUOM);
        }
        return productUOMMap;
    }
}

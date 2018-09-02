package com.pc.retail.cache;

import com.pc.retail.dao.DataAccessException;
import com.pc.retail.dao.DataSourceManager;
import com.pc.retail.dao.ProductInvDAO;
import com.pc.retail.dao.ReferenceDataDAO;
import com.pc.retail.vo.Product;
import com.pc.retail.vo.ProductSupplier;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by pavanc on 7/23/17.
 */
public class SupplierCache extends AbstractCache {

    private static SupplierCache supplierCache;
    private static volatile boolean initialized = false;
    private Map<Integer, ProductSupplier> productSuppliers;
    public static String CACHE_NAME = "SupplierCache";

    private SupplierCache(){
        productSuppliers = new ConcurrentHashMap<>();
    }

    public static SupplierCache getInstance() throws DataAccessException {
        if(initialized == false){
            synchronized(ProductCache.class){
                if(!initialized){
                    initialized = initialize();
                }
                return supplierCache;
            }
        }else{
            return supplierCache;
        }
    }

    private static boolean initialize() throws DataAccessException{
        supplierCache = new SupplierCache();
        supplierCache.poulateCache();
        return true;
    }

    public  void poulateCache() throws DataAccessException{
        List<ProductSupplier> supplierList = getDAO().getSuppliers();
        for(ProductSupplier productSupplier : supplierList){
            productSuppliers.put(productSupplier.getId(), productSupplier);
        }

    }

    private ReferenceDataDAO getDAO() throws DataAccessException{
        try {
            return DataSourceManager.getInstance().getReferenceDataDAO();
        } catch (DataAccessException e) {
            throw new DataAccessException(e.getMessage());
        }
    }



    public ProductSupplier getProductSupplier(int supplierId) {
        return productSuppliers.get(supplierId);
    }

    public List<ProductSupplier> getAll(){
        return new ArrayList<>(productSuppliers.values());
    }

    public void updateCache(ProductSupplier productSupplier) {
        productSuppliers.put(productSupplier.getId(), productSupplier);
    }
}

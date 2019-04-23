package com.pc.retail.client.services;

import com.pc.retail.dao.util.FilterModel;
import com.pc.retail.interactor.KiranaStoreException;
import com.pc.retail.vo.Product;

import java.util.HashMap;
import java.util.List;

/**
 * Created by pavanc on 4/23/19.
 */
public class LocalCacheService {

    private static HashMap<String, String> productCodeToBarCodeMap;

    public static HashMap<String,String> getProductCodeToBarCodeMap() {
        if(productCodeToBarCodeMap == null){
            synchronized (LocalCacheService.class) {
                productCodeToBarCodeMap = new HashMap<>();
                RetailAppClientLocator retailAppClientLocator = new RetailAppClientLocator();
                RetailAppClient retailAppClient = retailAppClientLocator.getRetailAppClient();
                ProductInventoryService productInventoryService = retailAppClient.getProductInventoryService();
                try {
                    FilterModel filterModel = new FilterModel();
                    List<Product> products = productInventoryService.getProducts(filterModel);
                    products.forEach(p -> productCodeToBarCodeMap.put(p.getPrdCode() + "," + p.getPrdDesc(), p.getBarcode()));
                } catch (KiranaStoreException e) {
                    e.printStackTrace();
                }
            }
        }
        return productCodeToBarCodeMap;
    }
}

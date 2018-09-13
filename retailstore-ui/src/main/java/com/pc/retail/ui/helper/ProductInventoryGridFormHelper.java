package com.pc.retail.ui.helper;

import com.pc.retail.client.services.ProductInventoryService;
import com.pc.retail.client.services.ProductInventoryServiceImpl;
import com.pc.retail.interactor.KiranaStoreException;
import com.pc.retail.vo.ProductInventory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pavanc on 8/3/17.
 */
public class ProductInventoryGridFormHelper {

   public List<ProductInventory> getInventories(int productId){
        try {
            ProductInventoryService productInventoryService = new ProductInventoryServiceImpl();
            return productInventoryService.getInventoryTransactionsForProduct(productId);
        } catch (KiranaStoreException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


}

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

   public List<ProductInventoryRow> getInventories(int productId){
        try {
            ProductInventoryService productInventoryService = new ProductInventoryServiceImpl();
            List<ProductInventory> productInventoryList =
                    productInventoryService.getInventoryTransactionsForProduct(productId);
            ProductInvMasterEventHandler productInvMasterEventHandler = new ProductInvMasterEventHandler();
            List<ProductInventoryRow> productInventoryRowList = new ArrayList<>();
            for(ProductInventory productInventory : productInventoryList){
                ProductInventoryRow productInventoryRow = new ProductInventoryRow(productInventory);
                productInventoryRow.setOnMouseClickedAction(productInvMasterEventHandler);
                productInventoryRowList.add(productInventoryRow);
            }
            return productInventoryRowList;
        } catch (KiranaStoreException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


}

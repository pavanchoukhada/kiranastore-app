package com.pc.retail.ui.event.handler;

import com.pc.retail.client.services.ProductInventoryService;
import com.pc.retail.client.services.RetailAppClient;
import com.pc.retail.client.services.RetailAppClientLocator;
import com.pc.retail.interactor.KiranaStoreException;
import com.pc.retail.vo.ProductDO;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Created by pavanc on 7/8/18.
 */
public class ProductFilter {

    public void handle(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)){
            RetailAppClientLocator retailAppClientLocator = new RetailAppClientLocator();
            RetailAppClient retailAppClient = retailAppClientLocator.getRetailAppClient();
            ProductInventoryService productInventoryService =  retailAppClient.getProductInventoryService();
            //productInventoryService.getAllProductsWithInvDetails();
        }
    }
}

package com.pc.retail.ui.event.handler;

import com.pc.retail.api.FilterKeyConstants;
import com.pc.retail.client.services.ProductInventoryService;
import com.pc.retail.client.services.RetailAppClient;
import com.pc.retail.client.services.RetailAppClientLocator;
import com.pc.retail.dao.util.FilterModel;
import com.pc.retail.interactor.KiranaStoreException;
import com.pc.retail.vo.ProductDO;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


/**
 * Created by pavanc on 7/15/17.
 */
public abstract class ProductIdEventHandler implements EventHandler<KeyEvent> {


    @Override
    public void handle(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)){
            RetailAppClientLocator retailAppClientLocator = new RetailAppClientLocator();
            RetailAppClient retailAppClient = retailAppClientLocator.getRetailAppClient();
            ProductInventoryService productInventoryService =  retailAppClient.getProductInventoryService();
            try {
                String productId = getProductId();
                if(productId != null && !"".equals(productId)) {
                    FilterModel filterModel = new FilterModel();
                    filterModel.addFilter(FilterKeyConstants.PRODUCT_ID, productId);
                    ProductDO product = productInventoryService.getProductDetails(filterModel);
                    updateForm(product);
                }
            } catch (KiranaStoreException e) {

            }

        }
    }

    protected abstract String getProductId();

    abstract void updateForm(ProductDO product);
}

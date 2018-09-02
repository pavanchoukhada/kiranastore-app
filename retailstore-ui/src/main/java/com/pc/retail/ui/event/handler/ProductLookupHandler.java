package com.pc.retail.ui.event.handler;

import com.pc.retail.client.services.ProductInventoryService;
import com.pc.retail.client.services.RetailAppClient;
import com.pc.retail.client.services.RetailAppClientLocator;
import com.pc.retail.dao.util.FilterModel;
import com.pc.retail.interactor.KiranaStoreException;
import com.pc.retail.ui.controller.BillingFormController;
import com.pc.retail.vo.ProductAndInvDO;
import javafx.event.EventHandler;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by pavanc on 7/15/17.
 */
public class ProductLookupHandler implements EventHandler<KeyEvent> {

    private static final String SEARCH_IN_ALL_ATTRIBUTES = "ALL_ATTRIBUTES";

    private BillingFormController billingFormController;

    public ProductLookupHandler(BillingFormController billingFormController){
        this.billingFormController = billingFormController;
    }

    @Override
    public void handle(KeyEvent event) {
        if (!event.getCode().equals(KeyCode.ENTER)){
            RetailAppClientLocator retailAppClientLocator = new RetailAppClientLocator();
            RetailAppClient retailAppClient = retailAppClientLocator.getRetailAppClient();
            ProductInventoryService productInventoryService =  retailAppClient.getProductInventoryService();
            try {
                String value = billingFormController.getPrdSearchTextTxt().getEditor().getText();
                if(value != null && !"".equals(value)) {
                    FilterModel filterModel = new FilterModel();
                    filterModel.addFilter(SEARCH_IN_ALL_ATTRIBUTES,value);
                    List<ProductAndInvDO> productAndInvDOList = productInventoryService.getAllProductsWithInvDetails(filterModel);
                    billingFormController.getPrdSearchTextTxt().getItems().clear();
                    billingFormController.getPrdSearchTextTxt().getItems().addAll(transform(productAndInvDOList));

                }
            } catch (KiranaStoreException e) {

            }

        }
    }

    private List<String> transform(List<ProductAndInvDO> productAndInvDOList) {
        List<String> prdList = new ArrayList<>();
        for(ProductAndInvDO productAndInvDO : productAndInvDOList){
            prdList.add(productAndInvDO.getBarcode() + "-" + productAndInvDO.getPrdCode());
        }
        return prdList;
    }

}

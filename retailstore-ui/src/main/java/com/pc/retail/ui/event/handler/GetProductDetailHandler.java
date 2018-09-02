package com.pc.retail.ui.event.handler;

import com.pc.retail.client.services.ProductInventoryService;
import com.pc.retail.client.services.RetailAppClient;
import com.pc.retail.client.services.RetailAppClientLocator;
import com.pc.retail.dao.util.FilterModel;
import com.pc.retail.interactor.KiranaStoreException;
import com.pc.retail.vo.ProductDO;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;



/**
 * Created by pavanc on 7/15/17.
 */
public class GetProductDetailHandler implements EventHandler<KeyEvent> {

    private UpdateProductDetail updateProductDetail;
    private String lookupKey;
    private TextField lookupValue;

    public GetProductDetailHandler(UpdateProductDetail updateProductDetail, String lookupKey, TextField textField){
        this.updateProductDetail = updateProductDetail;
        this.lookupKey = lookupKey;
        this.lookupValue= textField;
    }

    @Override
    public void handle(KeyEvent event) {
        if (event.getCode().equals(KeyCode.ENTER)){
            RetailAppClientLocator retailAppClientLocator = new RetailAppClientLocator();
            RetailAppClient retailAppClient = retailAppClientLocator.getRetailAppClient();
            ProductInventoryService productInventoryService =  retailAppClient.getProductInventoryService();
            try {
                String value = getProductLookupKeyValue();
                if(value != null && !"".equals(value)) {
                    FilterModel filterModel = new FilterModel();
                    filterModel.addFilter(lookupKey, value);
                    ProductDO productDO = productInventoryService.getProductDetails(filterModel);
                    updateForm(productDO);
                }
            } catch (KiranaStoreException e) {

            }

        }
    }

    protected String getProductLookupKeyValue(){
        return lookupValue.getText();
    }

    void updateForm(ProductDO product){
        updateProductDetail.updateForm(product);
    }
}

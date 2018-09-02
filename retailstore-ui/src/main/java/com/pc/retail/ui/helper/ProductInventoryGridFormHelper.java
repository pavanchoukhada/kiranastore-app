package com.pc.retail.ui.helper;

import com.pc.retail.client.services.ProductInventoryService;
import com.pc.retail.client.services.ProductInventoryServiceImpl;
import com.pc.retail.client.services.ReferenceDataService;
import com.pc.retail.client.services.ReferenceDataServiceImpl;
import com.pc.retail.dao.DataAccessException;
import com.pc.retail.dao.InvoiceStatus;
import com.pc.retail.interactor.KiranaAppResult;
import com.pc.retail.interactor.KiranaStoreException;
import com.pc.retail.interactor.ResultType;
import com.pc.retail.ui.controller.InventoryEntryOnlyGridFormController;
import com.pc.retail.ui.controller.ProductInventoryEntryGridFormController;
import com.pc.retail.util.DataUtil;
import com.pc.retail.vo.ProductInventory;
import com.pc.retail.vo.ProductInvoiceMaster;
import com.pc.retail.vo.ProductSupplier;
import com.pc.retail.vo.ProductUOM;
import javafx.event.ActionEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by pavanc on 8/3/17.
 */
public class ProductInventoryGridFormHelper {

   public List<ProductInventory> getInventories(int productId){
        try {
            ProductInventoryService productInventoryService = new ProductInventoryServiceImpl();
            return productInventoryService.getInventoryDetailForProduct(productId);
        } catch (KiranaStoreException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


}

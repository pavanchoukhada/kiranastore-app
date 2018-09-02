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
import com.pc.retail.ui.controller.ProductInvEntryGridFormController;
import com.pc.retail.util.DataUtil;
import com.pc.retail.vo.ProductInvoiceMaster;
import com.pc.retail.vo.ProductSupplier;
import com.pc.retail.vo.ProductUOM;
import com.sun.org.apache.regexp.internal.RE;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by pavanc on 8/3/17.
 */
public class ProductInvEntryGridFormHelper {

    public List<String> getUOMList(){
        try {
            ReferenceDataService referenceDataService = new ReferenceDataServiceImpl();
            Collection<ProductUOM> uomList = referenceDataService.getUOMList();
            List<String> prdUOMValues = new ArrayList<>();
            for(ProductUOM productUOM : uomList){
                prdUOMValues.add(productUOM.getUomCode());
            }
            return prdUOMValues;
        }catch (DataAccessException ex){
            ex.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<String> getInvoiceStatusList() {
        List<String> invoiceStatusList = new ArrayList<>();
        invoiceStatusList.add(InvoiceStatus.NEW.getName());
        invoiceStatusList.add(InvoiceStatus.INVENTORY_VERIFIED.getName());
        invoiceStatusList.add(InvoiceStatus.CLOSED.getName());
        return invoiceStatusList;
    }

    public List<ProductSupplier> getSupplierList() {
        ReferenceDataService referenceDataService = new ReferenceDataServiceImpl();
        try {
            return referenceDataService.getSuppliers();
        } catch (DataAccessException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public KiranaAppResult submit(ProductInvEntryGridFormController productInvEntryGridFormController){
        ProductInvoiceMaster productInvoiceMaster = getInvoiceMaster(productInvEntryGridFormController);
        ProductInventoryService productInventoryService = new ProductInventoryServiceImpl();
        try {
            return productInventoryService.saveProductInventoryWithInvoiceDetail(productInvoiceMaster);
        } catch (KiranaStoreException e) {
            return new KiranaAppResult(ResultType.APP_ERROR, e.getMessage());
        }
    }

    private ProductInvoiceMaster getInvoiceMaster(ProductInvEntryGridFormController productInvEntryGridFormController) {
        ProductInvoiceMaster productInvoiceMaster = new ProductInvoiceMaster();
        productInvoiceMaster.setInvoiceId(productInvEntryGridFormController.getInvoiceId());
        productInvoiceMaster.setInvoiceRefId(productInvEntryGridFormController.getInvoiceNoTxt().getText());
        productInvoiceMaster.setInvoiceStatus(getInvoiceStatus(productInvEntryGridFormController));
        productInvoiceMaster.setInvoiceDate(DataUtil.getDate(productInvEntryGridFormController.getInvoiceDateDP().getValue()));
        return productInvoiceMaster;
    }

    private InvoiceStatus getInvoiceStatus(ProductInvEntryGridFormController productInvEntryGridFormController) {
        String invoiceStatus = productInvEntryGridFormController.getInvoiceStatusCB().getValue();
        if(DataUtil.isEmpty(invoiceStatus)){
            return InvoiceStatus.NEW;
        }
        return InvoiceStatus.getInvoiceStatus(invoiceStatus);
    }
}

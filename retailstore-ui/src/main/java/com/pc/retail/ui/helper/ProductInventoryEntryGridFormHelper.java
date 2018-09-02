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
import com.pc.retail.ui.controller.ProductInvEntryGridFormController;
import com.pc.retail.ui.controller.ProductInventoryEntryGridFormController;
import com.pc.retail.util.DataUtil;
import com.pc.retail.vo.ProductInventory;
import com.pc.retail.vo.ProductInvoiceMaster;
import com.pc.retail.vo.ProductSupplier;
import com.pc.retail.vo.ProductUOM;
import javafx.event.ActionEvent;
import org.apache.tools.ant.util.DateUtils;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import static java.time.temporal.ChronoField.MILLI_OF_SECOND;

/**
 * Created by pavanc on 8/3/17.
 */
public class ProductInventoryEntryGridFormHelper {

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

    public KiranaAppResult submit(ProductInventoryEntryGridFormController productInvEntryGridFormController){
        ProductInvoiceMaster productInvoiceMaster = getInvoiceMaster(productInvEntryGridFormController);
        ProductInventoryService productInventoryService = new ProductInventoryServiceImpl();
        try {
            List<ProductInventory> productInventories = new ArrayList<>(productInvEntryGridFormController.getProductInventoryList());
            productInventories.addAll(productInvEntryGridFormController.getDeletedProductInventoryList());
            productInvoiceMaster.setProductInventoryList(productInventories);
            KiranaAppResult kiranaAppResult = productInventoryService.saveProductInventoryWithInvoiceDetail(productInvoiceMaster);
            productInvEntryGridFormController.resetForm(new ActionEvent());
            return kiranaAppResult;
        } catch (KiranaStoreException e) {
            return new KiranaAppResult(ResultType.APP_ERROR, e.getMessage());
        }
    }

    private ProductInvoiceMaster getInvoiceMaster(ProductInventoryEntryGridFormController productInvEntryGridFormController) {
        ProductInvoiceMaster productInvoiceMaster = new ProductInvoiceMaster();
        productInvoiceMaster.setInvoiceId(productInvEntryGridFormController.getInvoiceId());
        productInvoiceMaster.setInvoiceRefId(productInvEntryGridFormController.getInvoiceNoTxt().getText());
        productInvoiceMaster.setInvoiceStatus(getInvoiceStatus(productInvEntryGridFormController));
        productInvoiceMaster.setInvoiceDate(new Date(productInvEntryGridFormController.getInvoiceDateDP().getValue().getLong(MILLI_OF_SECOND)));
        productInvoiceMaster.setTotalInvAmt(DataUtil.getDouble(productInvEntryGridFormController.getTotalInvoiceAmountTxt().getText()));
        if(productInvEntryGridFormController.getSupplierCB().getSelectionModel() != null) {
            productInvoiceMaster.setSupplierId(productInvEntryGridFormController.getSupplierCB().getSelectionModel().getSelectedItem().getId());
        }
        return productInvoiceMaster;
    }

    private InvoiceStatus getInvoiceStatus(ProductInventoryEntryGridFormController productInvEntryGridFormController) {
        String invoiceStatus = productInvEntryGridFormController.getInvoiceStatusCB().getValue();
        if(DataUtil.isEmpty(invoiceStatus)){
            return InvoiceStatus.NEW;
        }
        return InvoiceStatus.getInvoiceStatus(invoiceStatus);
    }

    public KiranaAppResult submit(InventoryEntryOnlyGridFormController inventoryEntryOnlyGridFormController) {
        return null;
    }

    public void loadForm(int invoiceId, ProductInventoryEntryGridFormController productInventoryEntryGridFormController) {
        ProductInventoryService productInventoryService = new ProductInventoryServiceImpl();
        try {
            ProductInvoiceMaster productInvoiceMaster = productInventoryService.getProductInvoiceDetail(invoiceId);
            productInventoryEntryGridFormController.getInvoiceNoTxt().setText(productInvoiceMaster.getInvoiceRefId());
            productInventoryEntryGridFormController.getTotalInvoiceAmountTxt().setText(DataUtil.convertToText(productInvoiceMaster.getTotalInvAmt()));
            Date invoiceDate = productInvoiceMaster.getInvoiceDate();
            productInventoryEntryGridFormController.getInvoiceDateDP().setValue(DataUtil.getDateToLocalDate(invoiceDate));
            List<ProductSupplier> productSuppliers = productInventoryEntryGridFormController.getSupplierCB().getItems();
            for(ProductSupplier productSupplier : productSuppliers){
                if(productSupplier.getId() == productInvoiceMaster.getSupplierId()){
                    productInventoryEntryGridFormController.getSupplierCB().setValue(productSupplier);
                }
            }
            productInventoryEntryGridFormController.getProductInventoryList().addAll(productInvoiceMaster.getProductInventoryList());
        } catch (KiranaStoreException e) {
            e.printStackTrace();
        }

    }
}

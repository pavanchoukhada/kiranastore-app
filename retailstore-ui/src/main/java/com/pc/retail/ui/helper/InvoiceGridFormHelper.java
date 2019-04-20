package com.pc.retail.ui.helper;

import com.pc.retail.api.FilterKeyConstants;
import com.pc.retail.client.services.*;
import com.pc.retail.dao.InvoiceStatus;
import com.pc.retail.dao.util.FilterModel;
import com.pc.retail.interactor.KiranaStoreException;
import com.pc.retail.ui.controller.InvoiceGridFormController;
import com.pc.retail.util.DataUtil;
import com.pc.retail.vo.ProductInvoiceMasterDO;
import com.pc.retail.vo.ProductSupplier;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pavanc on 7/23/17.
 */
public class InvoiceGridFormHelper {

    private final ReferenceDataHelper referenceDataHelper = new ReferenceDataHelper();

    public List<String> getInvoiceStatusList() {
        List<String> invoiceStatusList = new ArrayList<>();
        invoiceStatusList.add(InvoiceStatus.ALL.getName());
        invoiceStatusList.add(InvoiceStatus.NEW.getName());
        invoiceStatusList.add(InvoiceStatus.INVENTORY_VERIFIED.getName());
        invoiceStatusList.add(InvoiceStatus.CLOSED.getName());
        return invoiceStatusList;
    }

    public List<ProductSupplier> getSupplierList() {
        return referenceDataHelper.getSupplierList();
    }

    public List<ProductInvoiceMasterDO> getInvoiceMasterRecord(InvoiceGridFormController invoiceGridFormController) throws KiranaStoreException {
        RetailAppClientLocator retailAppClientLocator = new RetailAppClientLocator();
        RetailAppClient retailAppClient = retailAppClientLocator.getRetailAppClient();
        ProductInventoryService productInventoryService =  retailAppClient.getProductInventoryService();
        FilterModel filterModel = new FilterModel();

        LocalDate fromLocalDate = invoiceGridFormController.getFromInvoiceDateDP().getValue();
        filterModel.addFilter(FilterKeyConstants.FROM_INVOICE_DATE, DataUtil.getDateStr(fromLocalDate));

        LocalDate toLocalDate = invoiceGridFormController.getToInvoiceDateDP().getValue();
        filterModel.addFilter(FilterKeyConstants.TO_INVOICE_DATE, convertToStringFormat(toLocalDate));

        String invoiceStatus = invoiceGridFormController.getInvoiceStatusCB().getValue();
        if(isNotEmpty(invoiceStatus) && !invoiceStatus.equals(InvoiceStatus.ALL)){
            filterModel.addFilter(FilterKeyConstants.INVOICE_STATUS, invoiceStatus);
        }

        if(invoiceGridFormController.getSupplierCB().getValue() != null) {
            int supplierId = invoiceGridFormController.getSupplierCB().getValue().getId();
            if (supplierId > 0) {
                filterModel.addFilter(FilterKeyConstants.SUPPLIER_ID, String.valueOf(supplierId));
            }
        }
        return productInventoryService.getProductInvoices(filterModel);
    }

    private boolean isNotEmpty(String invoiceStatus) {
        return invoiceStatus != null && !"".equals(invoiceStatus);
    }

    private String convertToStringFormat(LocalDate localDate) {
        return DataUtil.getDateStr(localDate);
    }


}


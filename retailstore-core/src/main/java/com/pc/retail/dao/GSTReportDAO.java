package com.pc.retail.dao;

import com.pc.retail.api.*;
import com.pc.retail.cache.GSTCache;
import com.pc.retail.cache.SupplierCache;
import com.pc.retail.dao.util.FilterModel;
import com.pc.retail.util.DataUtil;
import com.pc.retail.vo.GSTGroupModel;
import com.pc.retail.vo.ProductInventory;

import java.util.*;

/**
 * Created by pavanc on 3/31/19.
 */
public class GSTReportDAO {

    public static final String SUPPLIER_ID = "supplier_id";
    private StorageManager storageManager;

    public GSTReportDAO(StorageManager storageManager){
        this.storageManager = storageManager;
    }

    public Collection<SupplierGSTReportDO> getGSTReport(FilterModel filterModel) throws DataAccessException {
        List<ProductInventory> inventoryAndGSTDataList =
                storageManager.getReportClient().getInventoryAndGSTData(buildSqlParameters(filterModel));
        return transform(inventoryAndGSTDataList);
    }

    private List<SQLParameter> buildSqlParameters(FilterModel filterModel) {
        List<SQLParameter> sqlParameterList = new ArrayList<>();
        String supplierIdStr = filterModel.getFilterValue(FilterKeyConstants.SUPPLIER_ID);
        String fromDate = filterModel.getFilterValue(FilterKeyConstants.FROM_DATE);
        if (fromDate != null && !"".equals(fromDate)) {
            SQLParameter sqlParameter = new SQLParameter();
            sqlParameter.setParamDataType(DataType.DATE);
            sqlParameter.setParamValue(DataUtil.getSqlDateValue(fromDate));
            sqlParameter.setSqlOperator(SQLOperator.GREATER_THEN_EQUAL_TO);
            sqlParameter.setParamName("invoice_date");
            sqlParameterList.add(sqlParameter);
        }

        String toDate = filterModel.getFilterValue(FilterKeyConstants.TO_DATE);
        if (toDate != null && !"".equals(toDate)) {
            SQLParameter sqlParameter = new SQLParameter();
            sqlParameter.setParamDataType(DataType.DATE);
            sqlParameter.setParamValue(DataUtil.getSqlDateValue(toDate));
            sqlParameter.setSqlOperator(SQLOperator.LESS_THEN_EQUAL_TO);
            sqlParameter.setParamName("invoice_date");
            sqlParameterList.add(sqlParameter);
        }
        if(!DataUtil.isEmpty(supplierIdStr)) {
            SQLParameter sqlParameter = new SQLParameter();
            sqlParameter.setParamName(SUPPLIER_ID);
            sqlParameter.setParamDataType(DataType.INTEGER);
            sqlParameter.setParamValue(DataUtil.getInteger(supplierIdStr));
            sqlParameterList.add(sqlParameter);
        }
        return sqlParameterList;
    }

    private Collection<SupplierGSTReportDO> transform(List<ProductInventory> inventoryAndGSTDataList) throws DataAccessException {
        Map<Integer, SupplierGSTReportDO>  supplierGSTReportDOMap = new HashMap<>();
        Map<String, InvoiceGSTReportDO>  invoiceGSTReportDOMap = new HashMap<>();
        for(ProductInventory productInventory : inventoryAndGSTDataList){
            int supplierId = productInventory.getSupplierId();
            SupplierGSTReportDO supplierGSTReportDO = supplierGSTReportDOMap.get(supplierId);
            if(supplierGSTReportDO == null){
                supplierGSTReportDO = new SupplierGSTReportDO();
                supplierGSTReportDO.setSupplierCode(getSupplierCode(supplierId));
                supplierGSTReportDO.setSupplierGSTNo(getSupplierGSTNo(supplierId));
                supplierGSTReportDO.setInvoiceGSTReportDOList(new ArrayList<>());
                supplierGSTReportDOMap.put(supplierId, supplierGSTReportDO);
            }
            processForInvoiceList(supplierGSTReportDO, productInventory, invoiceGSTReportDOMap);
            addGSTFigures(supplierGSTReportDO, productInventory);
        }
        return supplierGSTReportDOMap.values();
    }

    private String getSupplierGSTNo(int supplierId) throws DataAccessException {
        return SupplierCache.getInstance().getProductSupplier(supplierId).getGstnId();
    }

    private void processForInvoiceList(SupplierGSTReportDO supplierGSTReportDO,
                                       ProductInventory productInventory,
                                       Map<String, InvoiceGSTReportDO> invoiceGSTReportDOMap)
                                                                                throws DataAccessException {
        String invoiceRef = productInventory.getSupplierId() + ":" + productInventory.getInvoiceRef();
        InvoiceGSTReportDO invoiceGSTReportDO = invoiceGSTReportDOMap.get(invoiceRef);
        if(invoiceGSTReportDO == null){
            invoiceGSTReportDO = new InvoiceGSTReportDO();
            invoiceGSTReportDO.setInvoiceRef(productInventory.getInvoiceRef());
            invoiceGSTReportDO.setGstReportDOList(new ArrayList<>());
            invoiceGSTReportDO.setInvoiceDate(DataUtil.getDateStr(productInventory.getInvoiceDate()));
            invoiceGSTReportDOMap.put(invoiceRef, invoiceGSTReportDO);
            supplierGSTReportDO.getInvoiceGSTReportDOList().add(invoiceGSTReportDO);
        }
        processForGSTList(invoiceGSTReportDO, productInventory);
        addGSTFigures(invoiceGSTReportDO, productInventory);
    }


    private void processForGSTList(InvoiceGSTReportDO invoiceGSTReportDO, ProductInventory productInventory) throws DataAccessException {
        List<GSTReportDO> gstReportDAOList = invoiceGSTReportDO.getGstReportDOList();
        GSTReportDO existingGSTReportDO = null;
        for(GSTReportDO gstReportDO : gstReportDAOList){
            GSTGroupModel gstGroupModel = GSTCache.getInstance().get(gstReportDO.getGstCode());
            if(gstGroupModel.getGroupCode().equals(productInventory.getGstGroupCode())){
                existingGSTReportDO = gstReportDO;
                break;
            }
        }
        if(existingGSTReportDO == null){
            existingGSTReportDO = new GSTReportDO();
            existingGSTReportDO.setProductGSTReportDOList(new ArrayList<>());
            existingGSTReportDO.setGstCode(productInventory.getGstGroupCode());
            invoiceGSTReportDO.getGstReportDOList().add(existingGSTReportDO);
        }
        addGSTFigures(existingGSTReportDO, productInventory);
        existingGSTReportDO.getProductGSTReportDOList().add(getProductGSTDO(productInventory));

    }

    private ProductGSTReportDO getProductGSTDO(ProductInventory productInventory) {
        ProductGSTReportDO productGSTReportDO = new ProductGSTReportDO();
        productGSTReportDO.setProductCode(productInventory.getProductCode());
        productGSTReportDO.setBarCode(productInventory.getBarCode());
        productGSTReportDO.setTotalInvoiceAmt(productInventory.getTotalCostIncludingGST());
        productGSTReportDO.setTaxableAmount(productInventory.getTotalCost());
        productGSTReportDO.setTotalCGSTAmount(productInventory.getTotalCGSTAmount());
        productGSTReportDO.setTotalSGSTAmount(productInventory.getTotalSGSTAmount());
        return productGSTReportDO;
    }

    private void addGSTFigures(GSTReportDO existingGSTReportDO, ProductInventory productInventory) {
        existingGSTReportDO.setTotalInvoiceAmt(existingGSTReportDO.getTotalInvoiceAmt() + productInventory.getTotalCostIncludingGST());
        existingGSTReportDO.setTotalTaxableAmt(existingGSTReportDO.getTotalTaxableAmt() + productInventory.getTotalCost());
        existingGSTReportDO.setTotalCGSTAmount(existingGSTReportDO.getTotalCGSTAmount() + productInventory.getTotalCGSTAmount());
        existingGSTReportDO.setTotalSGSTAmount(existingGSTReportDO.getTotalSGSTAmount() + productInventory.getTotalSGSTAmount());
        existingGSTReportDO.setTotalGSTAmount(existingGSTReportDO.getTotalGSTAmount() + productInventory.getTotalGSTAmountForInv());
    }

    private void addGSTFigures(InvoiceGSTReportDO invoiceGSTReportDO, ProductInventory productInventory) {
        invoiceGSTReportDO.setInvoiceAmt(invoiceGSTReportDO.getInvoiceAmt() + productInventory.getTotalCostIncludingGST());
        invoiceGSTReportDO.setTotalTaxableAmount(invoiceGSTReportDO.getTotalTaxableAmount() + productInventory.getTotalCost());
        invoiceGSTReportDO.setTotalCGSTAmount(invoiceGSTReportDO.getTotalCGSTAmount() + productInventory.getTotalCGSTAmount());
        invoiceGSTReportDO.setTotalSGSTAmount(invoiceGSTReportDO.getTotalSGSTAmount() + productInventory.getTotalSGSTAmount());
        invoiceGSTReportDO.setTotalGSTAmount(invoiceGSTReportDO.getTotalGSTAmount() + productInventory.getTotalGSTAmountForInv());
    }

    private void addGSTFigures(SupplierGSTReportDO supplierGSTReportDO, ProductInventory productInventory) {
        supplierGSTReportDO.setTotalInvoiceAmt(supplierGSTReportDO.getTotalInvoiceAmt() + productInventory.getTotalCostIncludingGST());
        supplierGSTReportDO.setTotalTaxableAmount(supplierGSTReportDO.getTotalTaxableAmount() + productInventory.getTotalCost());
        supplierGSTReportDO.setTotalCGSTAmount(supplierGSTReportDO.getTotalCGSTAmount() + productInventory.getTotalCGSTAmount());
        supplierGSTReportDO.setTotalSGSTAmount(supplierGSTReportDO.getTotalSGSTAmount() + productInventory.getTotalSGSTAmount());
        supplierGSTReportDO.setTotalGSTAmount(supplierGSTReportDO.getTotalGSTAmount() + productInventory.getTotalGSTAmountForInv());
    }

    private String getSupplierCode(int supplierId) throws DataAccessException {
        return SupplierCache.getInstance().getProductSupplier(supplierId).getCode();
    }
}

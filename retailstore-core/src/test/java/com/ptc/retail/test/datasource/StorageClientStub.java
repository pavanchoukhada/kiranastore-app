package com.ptc.retail.test.datasource;

import com.pc.retail.dao.*;
import com.pc.retail.dao.util.FilterModel;
import com.pc.retail.vo.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pavanc on 5/20/17.
 */
public class StorageClientStub implements StorageClient {

    private static Map<Integer, List<ProductInventory>> productInvDetailMap  = new HashMap<>();;
    private static Map<Integer, List<InventoryTransactionModel>> inventoryTransactionModelMap = new HashMap<>();;
    private static Map<Integer, Product> productMap = new HashMap<>();;
    private static Map<Integer, ProductCurrentInvDetail> productCurrencyInvDtlMap = new HashMap<>();;
    private static Map<String, ProductInvoiceMaster> invoiceMasterMap = new HashMap<>();

    public StorageClientStub() {
    }

    @Override
    public int saveProductInv(ProductInventory productInventoryDetail) throws DataAccessException {
        List<ProductInventory> productInventories = productInvDetailMap.get(productInventoryDetail.getProductId());
        if (productInventories == null){
            productInventories = new ArrayList<>();
            productInvDetailMap.put( productInventoryDetail.getProductId(),  productInventories);
        }
        productInventories.add( productInventoryDetail);
        return productInventoryDetail.getPrdInvId();
    }


    public void updateInventory(ProductInventory productInventory) throws DataAccessException {
        addToMapList(productInvDetailMap, productInventory, productInventory.getProductId());

    }

    private <T> void  addToMapList(Map<Integer, List<T>> map, T type, Integer key) {
        List<T> modelList =  map.get(key);
        if( modelList == null) {
            modelList = new ArrayList<>();
            map.put(key, modelList);
        }
        modelList.add(type);
    }

    @Override
    public int saveProduct(Product product) {
        productMap.put(product.getProductId(), product);
        return product.getProductId();
    }

    @Override
    public void addAuditInventoryTransaction(InventoryTransactionModel inventoryTransactionModel) {
        List<InventoryTransactionModel> inventoryTransactionModels = inventoryTransactionModelMap.get(inventoryTransactionModel.getProductId());
        if (inventoryTransactionModels == null) {
            inventoryTransactionModels = new ArrayList<>();
            inventoryTransactionModelMap.put(inventoryTransactionModel.getProductId(), inventoryTransactionModels);
        }
        inventoryTransactionModels.add(inventoryTransactionModel);
    }


    @Override
    public void commit() throws DataAccessException {

    }

    @Override
    public void rollBack() throws DataAccessException {

    }

    @Override
    public List<ProductCurrentInvDetail> getCurrentInventoryForProduct(int productId) throws DataAccessException {
        List<ProductCurrentInvDetail> productCurrentInvDetailList = new ArrayList<>();
        if(productCurrencyInvDtlMap.get(productId) != null)
            productCurrentInvDetailList.add( productCurrencyInvDtlMap.get(productId) );
        return productCurrentInvDetailList;
    }

    @Override
    public void saveCurrentInventoryForProduct(ProductCurrentInvDetail productCurrentInvDetail) throws DataAccessException {
        productCurrencyInvDtlMap.put( productCurrentInvDetail.getProductId(), productCurrentInvDetail);
    }

    @Override
    public ProductInvoiceMaster getInvoiceMasterWithDetail(String invoiceRef) throws DataAccessException {
        return invoiceMasterMap.get( invoiceRef );
    }

    @Override
    public List<Product> getProducts(int productId) throws DataAccessException {
        return null;
    }

    @Override
    public void saveInvoiceMaster(ProductInvoiceMaster productInvoiceMaster) throws DataAccessException {
        invoiceMasterMap.put( productInvoiceMaster.getInvoiceRefId(), productInvoiceMaster);
    }

    @Override
    public List<ProductInvoiceMaster> getProductInvoiceMasterList(FilterModel filterModel) throws DataAccessException {
        return null;
    }

    @Override
    public ProductInvoiceMaster getInvoiceMasterWithDetail(int invoiceId) throws DataAccessException {
        return null;
    }


    @Override
    public List<ProductInvoiceDetail> getProductInvoiceDetails(int invoiceId) throws DataAccessException {
        return null;
    }

    @Override
    public int generateBarCode() throws DataAccessException {
        return 0;
    }

    @Override
    public List<ProductInventory> getProductInventoriesForProduct(int productId) throws DataAccessException {
        return null;
    }

    @Override
    public void releaseConnection() {

    }

    public List<InventoryTransactionModel> getInvTransactions(int productId){
        return inventoryTransactionModelMap.get( productId );
    }
}

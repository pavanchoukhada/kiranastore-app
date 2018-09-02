package com.pc.retail.dao;

import com.pc.retail.dao.util.FilterModel;
import com.pc.retail.vo.*;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by pavanc on 5/14/17.
 */
public interface StorageClient {

    int saveProductInv(ProductInventory productInventoryDetail) throws DataAccessException;

    int saveProduct(Product product) throws DataAccessException;

    void addAuditInventoryTransaction(InventoryTransactionModel inventoryTransactionModel) throws DataAccessException, SQLException;

    List<InventoryTransactionModel> getInvTransactions(int productId) throws DataAccessException;

    void commit() throws DataAccessException;

    void rollBack() throws DataAccessException;

    List<ProductCurrentInvDetail> getCurrentInventoryForProduct(int productId) throws DataAccessException;

    void saveCurrentInventoryForProduct(ProductCurrentInvDetail productCurrentInvDetail) throws DataAccessException;

    void saveInvoiceMaster(ProductInvoiceMaster productInvoiceMaster) throws DataAccessException;

    List<ProductInvoiceMaster> getProductInvoiceMasterList(FilterModel filterModel) throws DataAccessException;

    ProductInvoiceMaster getInvoiceMasterWithDetail(int invoiceId) throws DataAccessException;

    ProductInvoiceMaster getInvoiceMasterWithDetail(String invoiceRefId) throws DataAccessException;

    List<Product> getProducts(int productId) throws DataAccessException;

    List<ProductInvoiceDetail> getProductInvoiceDetails(int invoiceId) throws DataAccessException;

    int generateBarCode() throws DataAccessException;

    List<ProductInventory> getProductInventoriesForProduct(int productId) throws DataAccessException;

    void releaseConnection();

}
